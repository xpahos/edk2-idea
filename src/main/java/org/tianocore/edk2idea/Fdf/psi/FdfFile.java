package org.tianocore.edk2idea.Fdf.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Fdf.FdfFileType;
import org.tianocore.edk2idea.Fdf.FdfLanguage;

public class FdfFile extends PsiFileBase {

    public FdfFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, FdfLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return FdfFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "FDF File";
    }
}
