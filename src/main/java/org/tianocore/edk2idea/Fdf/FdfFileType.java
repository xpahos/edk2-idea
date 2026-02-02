package org.tianocore.edk2idea.Fdf;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FdfFileType extends LanguageFileType {

    public static final FdfFileType INSTANCE = new FdfFileType();

    private FdfFileType() {
        super(FdfLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "EDK2 FDF File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "EDK2 flash description file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "fdf";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return FdfIcons.FILE;
    }
}
