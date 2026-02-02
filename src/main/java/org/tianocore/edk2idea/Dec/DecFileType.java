package org.tianocore.edk2idea.Dec;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DecFileType extends LanguageFileType {
    public static final DecFileType INSTANCE = new DecFileType();

    private DecFileType() {
        super(DecLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "EDK2 Dec File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "EDK2 package declaration file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "dec";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return DecIcons.FILE;
    }
}
