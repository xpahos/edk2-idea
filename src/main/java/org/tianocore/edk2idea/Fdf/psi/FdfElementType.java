package org.tianocore.edk2idea.Fdf.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Fdf.FdfLanguage;

public class FdfElementType extends IElementType {
    public FdfElementType(@NotNull @NonNls String debugName) {
        super(debugName, FdfLanguage.INSTANCE);
    }
}
