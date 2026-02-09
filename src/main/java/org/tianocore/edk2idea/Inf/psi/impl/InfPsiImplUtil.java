package org.tianocore.edk2idea.Inf.psi.impl;

import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Edk2FileReference;
import org.tianocore.edk2idea.Inf.psi.InfPath;
import com.intellij.openapi.util.TextRange;

public class InfPsiImplUtil {
    public static PsiReference @NotNull [] getReferences(@NotNull InfPath element) {
        return new PsiReference[] {
                new Edk2FileReference(element, new TextRange(0, element.getTextLength()))
        };
    }
}
