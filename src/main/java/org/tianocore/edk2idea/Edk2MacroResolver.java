package org.tianocore.edk2idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.tianocore.edk2idea.Dec.psi.DecProperty;
import org.tianocore.edk2idea.Dsc.psi.DscDefineEntry;
import org.tianocore.edk2idea.Dsc.psi.DscDefineStatement;
import org.tianocore.edk2idea.Inf.InfTypes;
import org.tianocore.edk2idea.Inf.psi.InfDefineEntry;
import org.tianocore.edk2idea.Inf.psi.InfDefineStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edk2MacroResolver {

    public static Map<String, String> getProjectMacros(Project project) {
        return getProjectMacros(project, null);
    }

    public static Map<String, String> getProjectMacros(Project project, PsiFile contextFile) {
        Map<String, String> macros = new HashMap<>();
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        // Helper to collect files including contextFile
        java.util.Set<PsiFile> infFiles = collectFiles(project, "inf", scope, contextFile);
        for (PsiFile file : infFiles) {
            Collection<InfDefineStatement> definitions = PsiTreeUtil.findChildrenOfType(file, InfDefineStatement.class);
            for (InfDefineStatement def : definitions) {
                InfDefineEntry entry = def.getDefineEntry();
                if (entry != null && entry.getDefineValue() != null) {
                    PsiElement identifier = null;
                    for (PsiElement child : entry.getChildren()) {
                        if (child.getNode().getElementType() == InfTypes.IDENTIFIER) {
                            identifier = child;
                            break;
                        }
                    }
                    if (identifier != null) {
                        macros.put(identifier.getText(), entry.getDefineValue().getText());
                    }
                }
            }
        }

        // Process DSC files
        java.util.Set<PsiFile> dscFiles = collectFiles(project, "dsc", scope, contextFile);
        for (PsiFile file : dscFiles) {
            Collection<DscDefineStatement> definitions = PsiTreeUtil.findChildrenOfType(file, DscDefineStatement.class);
            for (DscDefineStatement def : definitions) {
                DscDefineEntry entry = def.getDefineEntry();
                if (entry != null) {
                    String key = entry.getKey().getText();
                    String value = entry.getValueString().getText();
                    if (key.startsWith("DEFINE ")) {
                        key = key.substring(7).trim();
                    } else if (key.startsWith("SET ")) {
                        key = key.substring(4).trim();
                    }
                    macros.put(key, value);
                }
            }
        }

        // Process DEC files
        java.util.Set<PsiFile> decFiles = collectFiles(project, "dec", scope, contextFile);
        for (PsiFile file : decFiles) {
            Collection<DecProperty> properties = PsiTreeUtil.findChildrenOfType(file, DecProperty.class);
            for (DecProperty prop : properties) {
                String key = prop.getKey().getText();
                String value = prop.getValue().getText();
                if (key.startsWith("DEFINE ")) {
                    key = key.substring(7).trim();
                    macros.put(key, value);
                } else {
                    macros.put(key, value);
                }
            }
        }

        return macros;
    }

    private static java.util.Set<PsiFile> collectFiles(Project project, String ext, GlobalSearchScope scope,
            PsiFile contextFile) {
        java.util.Set<PsiFile> result = new java.util.HashSet<>();
        Collection<VirtualFile> virtualFiles = FilenameIndex.getAllFilesByExt(project, ext, scope);
        for (VirtualFile vFile : virtualFiles) {
            PsiFile file = PsiManager.getInstance(project).findFile(vFile);
            if (file != null) {
                result.add(file);
            }
        }
        if (contextFile != null) {
            VirtualFile vFile = contextFile.getVirtualFile();
            if (vFile != null && ext.equalsIgnoreCase(vFile.getExtension())) {
                result.add(contextFile);
            }
        }
        return result;
    }

    public static String expand(String path, Map<String, String> macros) {
        if (path == null || !path.contains("$(")) {
            return path;
        }

        Pattern pattern = Pattern.compile("\\$\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(path);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String variable = matcher.group(1);
            String replacement = macros.getOrDefault(variable, matcher.group(0));
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
