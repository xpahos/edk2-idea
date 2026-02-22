package org.tianocore.edk2idea.search;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Dec.psi.DecGuidName;
import org.tianocore.edk2idea.Inf.psi.InfGuidName;
import org.tianocore.edk2idea.index.DecGuidIndex;

import java.util.Collections;

public class Edk2GuidReferenceSearcher extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {
    public Edk2GuidReferenceSearcher() {
        super(true);
    }

    @Override
    public void processQuery(@NotNull ReferencesSearch.SearchParameters queryParameters,
            @NotNull Processor<? super PsiReference> consumer) {
        PsiElement target = queryParameters.getElementToSearch();
        String guidName = null;

        if (target instanceof DecGuidName || target instanceof InfGuidName) {
            guidName = target.getText();
        }

        if (guidName == null) {
            return;
        }

        final String searchName = guidName;
        com.intellij.psi.search.SearchScope searchScope = queryParameters.getEffectiveSearchScope();
        Project project = target.getProject();
        GlobalSearchScope scope;

        if (searchScope instanceof GlobalSearchScope) {
            scope = (GlobalSearchScope) searchScope;
        } else if (searchScope instanceof com.intellij.psi.search.LocalSearchScope) {
            com.intellij.psi.search.LocalSearchScope localScope = (com.intellij.psi.search.LocalSearchScope) searchScope;
            java.util.List<com.intellij.openapi.vfs.VirtualFile> files = new java.util.ArrayList<>();
            for (PsiElement el : localScope.getScope()) {
                com.intellij.psi.PsiFile pf = el.getContainingFile();
                if (pf != null && pf.getVirtualFile() != null) {
                    files.add(pf.getVirtualFile());
                }
            }
            scope = GlobalSearchScope.filesScope(project, files);
        } else {
            scope = GlobalSearchScope.projectScope(project);
        }

        // 1. Query DEC files
        FileBasedIndex.getInstance().getFilesWithKey(DecGuidIndex.NAME, Collections.singleton(searchName), file -> {
            com.intellij.psi.PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile != null) {
                PsiTreeUtil.findChildrenOfType(psiFile, DecGuidName.class).forEach(element -> {
                    if (searchName.equals(element.getText()) && element != target) {
                        consumer.process(new Edk2GuidReference(element));
                    }
                });
            }
            return true;
        }, scope);

        // Note: For usages in INF files, we'd query an InfGuidIndex if we had one.
        // Currently we only have DecGuidIndex. Since `[Guids/Protocols]` usages
        // in INF files don't define the GUID themselves, they are references,
        // and standard 'Find Usages' finds them when it scans references.
    }
}
