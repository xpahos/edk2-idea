package org.tianocore.edk2idea.Dsc.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Edk2FileReference;
import com.intellij.openapi.util.TextRange;

public abstract class DscPathMixin extends ASTWrapperPsiElement {
    public DscPathMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return new PsiReference[] {
                new Edk2FileReference(this, new TextRange(0, getTextLength()))
        };
    }
}
