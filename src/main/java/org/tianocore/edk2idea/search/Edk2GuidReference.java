package org.tianocore.edk2idea.search;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tianocore.edk2idea.Dec.psi.DecFile;
import org.tianocore.edk2idea.Dec.psi.DecGuidName;
import org.tianocore.edk2idea.index.DecGuidIndex;

import java.util.ArrayList;
import java.util.List;

public class Edk2GuidReference extends PsiReferenceBase.Poly<PsiElement> {

    private final String guidName;

    public Edk2GuidReference(@NotNull PsiElement element) {
        super(element, new com.intellij.openapi.util.TextRange(0, element.getTextLength()), false);
        this.guidName = element.getText();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        List<ResolveResult> results = new ArrayList<>();

        // Resolve definitions from DEC files
        FileBasedIndex.getInstance().processValues(DecGuidIndex.NAME, guidName, null,
                (file, value) -> {
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                    if (psiFile instanceof DecFile) {
                        // Normally we'd index the exact offset or store the physical DecGuidName
                        // element
                        // For a simple implementation, search the file tree for the matching name
                        psiFile.accept(new PsiRecursiveElementWalkingVisitor() {
                            @Override
                            public void visitElement(@NotNull PsiElement element) {
                                if (element instanceof DecGuidName && element.getText().equals(guidName)) {
                                    results.add(new PsiElementResolveResult(element));
                                }
                                super.visitElement(element);
                            }
                        });
                    }
                    return true;
                }, scope);

        return results.toArray(new ResolveResult[0]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length > 0 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
