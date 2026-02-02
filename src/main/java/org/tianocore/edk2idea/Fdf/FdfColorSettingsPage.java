package org.tianocore.edk2idea.Fdf;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

final class FdfColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[] {
            new AttributesDescriptor("Comment", FdfSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Section header", FdfSyntaxHighlighter.SECTION_HEADER),
            new AttributesDescriptor("Keyword", FdfSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("Command", FdfSyntaxHighlighter.COMMAND),
    };

    @Override
    public Icon getIcon() {
        return FdfIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new FdfSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return """
                [Defines]
                !include OvmfPkg/Include/Fdf/OvmfPkgDefines.fdf.inc

                [FD.OVMF_VARS]
                BaseAddress   = $(FW_BASE_ADDRESS)
                Size          = $(VARS_SIZE)
                ErasePolarity = 1
                BlockSize     = $(BLOCK_SIZE)
                NumBlocks     = $(VARS_BLOCKS)

                0x00000000|$(FVMAIN_SIZE)
                FV = FVMAIN_COMPACT

                [FV.PEIFV]
                FvNameGuid         = 6938079B-B503-4E3D-9D24-B28337A25806
                BlockSize          = 0x10000
                ERASE_POLARITY     = 1

                APRIORI PEI {
                !if $(DEBUG_TO_MEM)
                  INF  OvmfPkg/MemDebugLogPei/MemDebugLogPei.inf
                !endif
                  INF  MdeModulePkg/Universal/PCD/Pei/Pcd.inf
                }

                INF  MdeModulePkg/Core/Pei/PeiMain.inf

                [Rule.Common.PEIM]
                  FILE PEIM = $(NAMED_GUID) {
                     PEI_DEPEX PEI_DEPEX Optional        $(INF_OUTPUT)/$(MODULE_NAME).depex
                     PE32      PE32   Align=Auto         $(INF_OUTPUT)/$(MODULE_NAME).efi
                     UI       STRING="$(MODULE_NAME)" Optional
                     VERSION  STRING="$(INF_VERSION)" Optional BUILD_NUM=$(BUILD_NUMBER)
                  }
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
        return "Fdf";
    }

}
