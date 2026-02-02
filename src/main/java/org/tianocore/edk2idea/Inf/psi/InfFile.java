package org.tianocore.edk2idea.Inf.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.tianocore.edk2idea.Inf.InfFileType;
import org.tianocore.edk2idea.Inf.InfLanguage;
import org.jetbrains.annotations.NotNull;

public class InfFile extends PsiFileBase {

    public InfFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, InfLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return InfFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "EDK2 Inf File";
    }

}
