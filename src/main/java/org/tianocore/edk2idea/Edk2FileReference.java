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
        if (currentFile != null) {
            PsiDirectory currentDir = currentFile.getContainingDirectory();
            if (currentDir != null) {
                PsiFile relativeFile = currentDir.findFile(filePath);
                if (relativeFile != null) {
                    results.add(new PsiElementResolveResult(relativeFile));
                } else {
                    // Try resolving subdirectories if path contains separators
                    // Simple hack: use VirtualFile findFileByRelativePath
                    VirtualFile vFile = currentDir.getVirtualFile().findFileByRelativePath(filePath);
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

        // 2. Try to resolve by filename globally (fallback) if not found relatively?
        // Or should we always do this?
        // For EDK2, paths are often relative to the package root or workspace.
        // If we can't find it relatively, let's try finding files with valid names.
        // NOTE: This can be noisy. Let's limit to exact path suffix matching if
        // possible.
        // For now, let's stick to simple relative + maybe Project search if it's a
        // "known" absolute-ish path.

        // If results are empty, let's try to find any file with this name in the
        // project
        // This is useful for "Packages/MdePkg/MdePkg.dec" style paths where we might be
        // at root.
        if (results.isEmpty()) {
            // Extract just the filename
            String filename = filePath;
            if (filePath.contains("/")) {
                filename = filePath.substring(filePath.lastIndexOf("/") + 1);
            }
            if (filePath.contains("\\")) {
                filename = filePath.substring(filePath.lastIndexOf("\\") + 1);
            }

            Collection<VirtualFile> virtualFiles = FilenameIndex.getVirtualFilesByName(filename,
                    GlobalSearchScope.allScope(project));
            for (VirtualFile virtualFile : virtualFiles) {
                // Simple check: does the path end with our filePath?
                // Normalize separators
                String vPath = virtualFile.getPath().replace('\\', '/');
                String refPath = filePath.replace('\\', '/');
                if (vPath.endsWith(refPath)) {
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                    if (psiFile != null) {
                        results.add(new PsiElementResolveResult(psiFile));
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
