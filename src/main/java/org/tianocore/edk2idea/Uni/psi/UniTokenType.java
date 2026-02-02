package org.tianocore.edk2idea.Uni.psi;

import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Uni.UniLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class UniTokenType extends IElementType {
    public UniTokenType(@NotNull @NonNls String debugName) {
        super(debugName, UniLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "UniTokenType." + super.toString();
    }
}
