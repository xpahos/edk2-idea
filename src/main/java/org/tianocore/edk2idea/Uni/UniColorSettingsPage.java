package org.tianocore.edk2idea.Uni;

import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class UniColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[] {
            new AttributesDescriptor("Comment", UniSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Keyword", UniSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("String", UniSyntaxHighlighter.STRING),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return UniIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new UniSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return """
                // Example UNI File
                #string STR_EXAMPLE            #language en-US "Example String"
                #string STR_JAPANESE           #language en-US "こんにちは"
                """;
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Uni";
    }
}
