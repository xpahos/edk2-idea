package org.tianocore.edk2idea.Inf.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Inf.InfLanguage;

public class InfElementType extends IElementType {
    public InfElementType(@NotNull @NonNls String debugName) {
        super(debugName, InfLanguage.INSTANCE);
    }
}
