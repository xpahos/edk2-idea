package org.tianocore.edk2idea.Dsc;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DscFileType extends LanguageFileType {

    public static final DscFileType INSTANCE = new DscFileType();

    private DscFileType() {
        super(DscLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "EDK2 DSC File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "EDK2 description module file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "dsc";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return DscIcons.FILE;
    }
}
