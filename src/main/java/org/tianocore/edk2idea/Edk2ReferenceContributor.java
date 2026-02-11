package org.tianocore.edk2idea;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ProcessingContext;
import org.tianocore.edk2idea.Dsc.DscTypes;
import org.tianocore.edk2idea.Inf.InfTypes;
import org.tianocore.edk2idea.Fdf.FdfTypes;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Inf.psi.InfPath;
import org.tianocore.edk2idea.Inf.psi.InfTokenType;
import com.intellij.psi.tree.IElementType;

public class Edk2ReferenceContributor extends PsiReferenceContributor implements DumbAware {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement().withLanguage(org.tianocore.edk2idea.Inf.InfLanguage.INSTANCE),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                            @NotNull ProcessingContext context) {
                        if (element instanceof InfPath ||
                                element.getNode().getElementType().toString().equals("PATH_STRING")) {
                            return new PsiReference[] {
                                    new Edk2FileReference(element, new TextRange(0, element.getTextLength())) };
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });

        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement().withLanguage(org.tianocore.edk2idea.Dsc.DscLanguage.INSTANCE),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                            @NotNull ProcessingContext context) {
                        // For DscPath (composite) or fallback to PATH_STRING if not composite yet
                        if (element.getNode().getElementType().toString().equals("PATH")) { // DscTypes.PATH
                            // Since DscPath is a composite, we might want the reference on the whole path
                            // But Edk2FileReference constructor takes element and uses its text.
                            return new PsiReference[] {
                                    new Edk2FileReference(element, new TextRange(0, element.getTextLength())) };
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });

        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement().withLanguage(org.tianocore.edk2idea.Dec.DecLanguage.INSTANCE),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                            @NotNull ProcessingContext context) {
                        if (element.getNode().getElementType().toString().equals("PATH")) {
                            return new PsiReference[] {
                                    new Edk2FileReference(element, new TextRange(0, element.getTextLength())) };
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}
