package org.tianocore.edk2idea.Dsc.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DscPcdNameMixin extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {
    public DscPcdNameMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return null;
    }

    @Override
    public PsiReference getReference() {
        return new com.intellij.psi.PsiReferenceBase<PsiElement>(this,
                new com.intellij.openapi.util.TextRange(0, getTextLength())) {
            @Nullable
            @Override
            public PsiElement resolve() {
                return DscPcdNameMixin.this;
            }

            @NotNull
            @Override
            public Object[] getVariants() {
                return new Object[0];
            }
        };
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        return new PsiReference[] { getReference() };
    }
}
