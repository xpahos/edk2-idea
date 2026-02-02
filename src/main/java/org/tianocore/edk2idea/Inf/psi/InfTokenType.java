package org.tianocore.edk2idea.Inf.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Inf.InfLanguage;

public class InfTokenType extends IElementType {

    public InfTokenType(@NotNull @NonNls String debugName) {
        super(debugName, InfLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "InfTokenType." + super.toString();
    }
}
