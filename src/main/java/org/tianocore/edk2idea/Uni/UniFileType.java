package org.tianocore.edk2idea.Uni;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class UniFileType extends LanguageFileType {
    public static final UniFileType INSTANCE = new UniFileType();

    private UniFileType() {
        super(UniLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "EDK2 Uni File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "EDK2 Unicode String File";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "uni";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return UniIcons.FILE;
    }
}
