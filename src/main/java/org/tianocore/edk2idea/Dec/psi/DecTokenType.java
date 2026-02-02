package org.tianocore.edk2idea.Dec.psi;

import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Dec.DecLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DecTokenType extends IElementType {
    public DecTokenType(@NotNull @NonNls String debugName) {
        super(debugName, DecLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "DecTokenType." + super.toString();
    }
}
