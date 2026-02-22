package org.tianocore.edk2idea.Dec;

import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class DecColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[] {
      new AttributesDescriptor("Section", DecSyntaxHighlighter.SECTION_HEADER),
      new AttributesDescriptor("Key", DecSyntaxHighlighter.KEY),
      new AttributesDescriptor("Value", DecSyntaxHighlighter.VALUE),
      new AttributesDescriptor("Comment", DecSyntaxHighlighter.COMMENT),
      new AttributesDescriptor("Path string", DecSyntaxHighlighter.PATH_STRING),
      new AttributesDescriptor("PCD variable", DecSyntaxHighlighter.PCD_NAME),
      new AttributesDescriptor("Guid variable", DecSyntaxHighlighter.GUID_NAME),
      new AttributesDescriptor("Protocol variable", DecSyntaxHighlighter.PROTOCOL_NAME),
      new AttributesDescriptor("Ppi variable", DecSyntaxHighlighter.PPI_NAME),
  };

  @Nullable
  @Override
  public Icon getIcon() {
    return DecIcons.FILE;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new DecSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return """
        [Defines]
          DEC_SPECIFICATION              = 0x00010005
          PACKAGE_NAME                   = MdePkg
          PACKAGE_GUID                   = 1E73767F-8F52-4603-AEB4-F29B510B6766
          PACKAGE_VERSION                = 1.08

        [Includes]
          Include

        [LibraryClasses]
          ##  @libraryclass  Provides most usb APIs
          UefiUsbLib|Include/Library/UefiUsbLib.h

        [Guids]
          <GuidName>gEfiGlobalVariableGuid</GuidName>         = { 0x8BE4DF61, 0x93CA, 0x11D2, { 0xAA, 0x0D, 0x00, 0xE0, 0x98, 0x03, 0x2B, 0x8C }}

        [Protocols]
          <ProtocolName>gPcdProtocolGuid</ProtocolName>               = { 0x11B34006, 0xD85B, 0x4D0A, { 0xA2, 0x90, 0xD5, 0xA5, 0x71, 0x31, 0x0E, 0xF7 }}

        [Ppis]
          <PpiName>gEfiPeiMpServices2PpiGuid</PpiName>      = { 0x5cb9cb3d, 0x31a4, 0x480c, { 0x94, 0x98, 0x29, 0xd2, 0x69, 0xba, 0xcf, 0xba}}

        [PcdsFixedAtBuild]
          gEfiMdePkgTokenSpaceGuid.PcdFSBClock|200000000|UINT32|0x0000000c
        """;
  }

  @Nullable
  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return Map.of(
        "GuidName", DecSyntaxHighlighter.GUID_NAME,
        "ProtocolName", DecSyntaxHighlighter.PROTOCOL_NAME,
        "PpiName", DecSyntaxHighlighter.PPI_NAME);
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
    return "Dec";
  }
}
