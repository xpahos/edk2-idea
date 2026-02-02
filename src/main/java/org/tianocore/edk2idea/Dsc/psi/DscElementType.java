package org.tianocore.edk2idea.Dsc.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Dsc.DscLanguage;

public class DscElementType extends IElementType {
    public DscElementType(@NotNull @NonNls String debugName) {
        super(debugName, DscLanguage.INSTANCE);
    }
}
