package org.tianocore.edk2idea.Dsc.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Dsc.DscLanguage;

public class DscTokenType extends IElementType {

    public DscTokenType(@NotNull @NonNls String debugName) {
        super(debugName, DscLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "DscTokenType." + super.toString();
    }
}
