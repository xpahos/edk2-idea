package org.tianocore.edk2idea.Fdf.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Fdf.FdfLanguage;

public class FdfTokenType extends IElementType {

    public FdfTokenType(@NotNull @NonNls String debugName) {
        super(debugName, FdfLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "FdfTokenType." + super.toString();
    }
}
