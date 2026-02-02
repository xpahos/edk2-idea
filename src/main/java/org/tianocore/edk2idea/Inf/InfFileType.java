package org.tianocore.edk2idea.Inf;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class InfFileType extends LanguageFileType {

    public static final InfFileType INSTANCE = new InfFileType();

    private InfFileType() {
        super(InfLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "EDK2 Inf File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "EDK2 information module file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "inf";
    }

    @Override
    public Icon getIcon() {
        return InfIcons.FILE;
    }

}
