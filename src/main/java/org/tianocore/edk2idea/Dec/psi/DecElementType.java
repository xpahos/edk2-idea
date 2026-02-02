package org.tianocore.edk2idea.Dec.psi;

import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Dec.DecLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DecElementType extends IElementType {
    public DecElementType(@NotNull @NonNls String debugName) {
        super(debugName, DecLanguage.INSTANCE);
    }
}
