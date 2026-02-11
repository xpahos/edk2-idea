package org.tianocore.edk2idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Edk2FileReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private final String filePath;

    public Edk2FileReference(@NotNull PsiElement element, TextRange textRange) {
        super(element, textRange);
        this.filePath = element.getText().substring(textRange.getStartOffset(), textRange.getEndOffset());
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        List<ResolveResult> results = new ArrayList<>();

        // 1. Try to resolve relative to current file
        PsiFile currentFile = myElement.getContainingFile();

        // 0. Macro Expansion
        java.util.Map<String, String> macros = Edk2MacroResolver.getProjectMacros(project, currentFile);
        String expandedPath = Edk2MacroResolver.expand(filePath, macros);

        if (currentFile != null) {
            PsiDirectory currentDir = currentFile.getContainingDirectory();
            if (currentDir != null) {
                // ... (standard findFile logic) ...

                // Fallback using VirtualFile
                VirtualFile dirVFile = currentDir.getVirtualFile();
                PsiFile relativeFile = currentDir.findFile(expandedPath);
                if (relativeFile != null) {
                    results.add(new PsiElementResolveResult(relativeFile));
                } else {
                    VirtualFile vFile = dirVFile.findFileByRelativePath(expandedPath);
                    if (vFile != null) {
                        PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);
                        if (psiFile != null) {
                            results.add(new PsiElementResolveResult(psiFile));
                        } else {
                            PsiDirectory psiDir = PsiManager.getInstance(project).findDirectory(vFile);
                            if (psiDir != null) {
                                results.add(new PsiElementResolveResult(psiDir));
                            }
                        }
                    }
                }
            }
        }

        if (results.isEmpty()) {
            // Fuzzy matching for unresolved macros
            // Example: $(OPENSSL_PATH)/ms/uplink.h -> matches any file ending in
            // /ms/uplink.h
            if (expandedPath.contains("$(") && expandedPath.contains(")")) {
                String suffix = expandedPath.substring(expandedPath.lastIndexOf(")") + 1).replace('\\', '/');
                if (suffix.startsWith("/") || suffix.startsWith("\\")) {
                    String filename = suffix.substring(suffix.lastIndexOf('/') + 1);
                    Collection<VirtualFile> virtualFiles = FilenameIndex.getVirtualFilesByName(filename,
                            GlobalSearchScope.allScope(project));

                    for (VirtualFile virtualFile : virtualFiles) {
                        String vPath = virtualFile.getPath(); // VirtualFile paths use / as separator
                        if (vPath.endsWith(suffix)) {
                            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                            if (psiFile != null) {
                                results.add(new PsiElementResolveResult(psiFile));
                            }
                        }
                    }
                }
            }

            // Standard global search (fallback if fuzzy matching didn't return results, or
            // just separate logic?)
            // The original code had a global search block here, let's keep/merge it or
            // ensure we don't duplicate.
            // Original code logic:
            if (results.isEmpty()) {
                String filename = expandedPath;
                if (expandedPath.contains("/")) {
                    filename = expandedPath.substring(expandedPath.lastIndexOf("/") + 1);
                }
                if (expandedPath.contains("\\")) {
                    filename = expandedPath.substring(expandedPath.lastIndexOf("\\") + 1);
                }

                Collection<VirtualFile> virtualFiles = FilenameIndex.getVirtualFilesByName(filename,
                        GlobalSearchScope.allScope(project));
                for (VirtualFile virtualFile : virtualFiles) {
                    String vPath = virtualFile.getPath().replace('\\', '/');
                    String refPath = expandedPath.replace('\\', '/');
                    // Only match if refPath doesn't contain macros, otherwise we might match
                    // standard files wrongly?
                    // Actually original logic was: vPath.endsWith(refPath).
                    // If refPath is "$(VAR)/file.h", endsWith check fails.
                    // So we only need to preserve this for non-macro paths or fully resolved paths.
                    if (!expandedPath.contains("$(") && vPath.endsWith(refPath)) {
                        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                        if (psiFile != null) {
                            results.add(new PsiElementResolveResult(psiFile));
                        }
                    }
                }
            }
        }

        return results.toArray(new ResolveResult[0]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        // Auto-completion not yet implemented
        return new Object[0];
    }
}
