package org.tianocore.edk2idea.Uni.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.tianocore.edk2idea.Uni.UniFileType;
import org.tianocore.edk2idea.Uni.UniLanguage;
import org.jetbrains.annotations.NotNull;

public class UniFile extends PsiFileBase {
    public UniFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, UniLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return UniFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "EDK2 Uni File";
    }
}
