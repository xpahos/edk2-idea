package org.tianocore.edk2idea.Dec.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.tianocore.edk2idea.Dec.DecFileType;
import org.tianocore.edk2idea.Dec.DecLanguage;
import org.jetbrains.annotations.NotNull;

public class DecFile extends PsiFileBase {
    public DecFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, DecLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return DecFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "EDK2 Dec File";
    }
}
