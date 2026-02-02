package org.tianocore.edk2idea.Uni.psi;

import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Uni.UniLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class UniElementType extends IElementType {
    public UniElementType(@NotNull @NonNls String debugName) {
        super(debugName, UniLanguage.INSTANCE);
    }
}
