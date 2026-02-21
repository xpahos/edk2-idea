package org.tianocore.edk2idea.search;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tianocore.edk2idea.Dec.psi.DecPcdName;
import org.tianocore.edk2idea.Dsc.psi.DscPcdName;
import org.tianocore.edk2idea.Inf.psi.InfPcdName;
import org.tianocore.edk2idea.index.DecPcdIndex;
import org.tianocore.edk2idea.index.DscPcdIndex;
import org.tianocore.edk2idea.index.InfPcdIndex;

import java.util.Collections;

public class Edk2PcdReferenceSearcher extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {
    public Edk2PcdReferenceSearcher() {
        super(true);
    }

    @Override
    public void processQuery(@NotNull ReferencesSearch.SearchParameters queryParameters,
            @NotNull Processor<? super PsiReference> consumer) {
        PsiElement target = queryParameters.getElementToSearch();
        String pcdName = null;

        if (target instanceof DecPcdName || target instanceof DscPcdName || target instanceof InfPcdName) {
            pcdName = target.getText();
        }

        if (pcdName == null) {
            return;
        }

        final String searchName = pcdName;

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
        FileBasedIndex.getInstance().getFilesWithKey(DecPcdIndex.NAME, Collections.singleton(searchName), file -> {
            com.intellij.psi.PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile != null) {
                PsiTreeUtil.findChildrenOfType(psiFile, DecPcdName.class).forEach(element -> {
                    if (searchName.equals(element.getText()) && element != target) {
                        consumer.process(createRef(element, target));
                    }
                });
            }
            return true;
        }, scope);

        // 2. Query DSC files
        FileBasedIndex.getInstance().getFilesWithKey(DscPcdIndex.NAME, Collections.singleton(searchName), file -> {
            com.intellij.psi.PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile != null) {
                PsiTreeUtil.findChildrenOfType(psiFile, DscPcdName.class).forEach(element -> {
                    if (searchName.equals(element.getText()) && element != target) {
                        consumer.process(createRef(element, target));
                    }
                });
            }
            return true;
        }, scope);

        // 3. Query INF files
        FileBasedIndex.getInstance().getFilesWithKey(InfPcdIndex.NAME, Collections.singleton(searchName), file -> {
            com.intellij.psi.PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile != null) {
                PsiTreeUtil.findChildrenOfType(psiFile, InfPcdName.class).forEach(element -> {
                    if (searchName.equals(element.getText()) && element != target) {
                        consumer.process(createRef(element, target));
                    }
                });
            }
            return true;
        }, scope);
    }

    private PsiReference createRef(PsiElement source, PsiElement target) {
        return new PsiReferenceBase<PsiElement>(source, new TextRange(0, source.getTextLength())) {
            @Nullable
            @Override
            public PsiElement resolve() {
                return target;
            }

            @NotNull
            @Override
            public Object[] getVariants() {
                return new Object[0];
            }

            @Override
            public boolean isReferenceTo(@NotNull PsiElement element) {
                return element == target || super.isReferenceTo(element);
            }
        };
    }
}
