package org.tianocore.edk2idea.Inf.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tianocore.edk2idea.Inf.psi.InfGuidName;

public abstract class InfGuidNameMixin extends ASTWrapperPsiElement implements PsiNameIdentifierOwner, InfGuidName {

    public InfGuidNameMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        // Not supporting rename for now
        return this;
    }

    @Override
    public PsiReference getReference() {
        PsiReference[] refs = ReferenceProvidersRegistry.getReferencesFromProviders(this);
        if (refs.length > 0) {
            return refs[0];
        }
        return null;
    }
}
