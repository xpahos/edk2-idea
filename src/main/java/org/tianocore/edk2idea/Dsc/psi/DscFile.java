package org.tianocore.edk2idea.Dsc.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Dsc.DscFileType;
import org.tianocore.edk2idea.Dsc.DscLanguage;

public class DscFile extends PsiFileBase {

    public DscFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, DscLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return DscFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "DSC File";
    }
}
