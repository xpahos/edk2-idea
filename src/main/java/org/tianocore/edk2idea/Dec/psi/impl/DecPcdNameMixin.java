package org.tianocore.edk2idea.Dec.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DecPcdNameMixin extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {

    public DecPcdNameMixin(@NotNull ASTNode node) {
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
    public com.intellij.psi.PsiReference getReference() {
        return new com.intellij.psi.PsiReferenceBase<PsiElement>(this,
                new com.intellij.openapi.util.TextRange(0, getTextLength())) {
            @Nullable
            @Override
            public PsiElement resolve() {
                return DecPcdNameMixin.this;
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
    public com.intellij.psi.PsiReference[] getReferences() {
        return new com.intellij.psi.PsiReference[] { getReference() };
    }
}
