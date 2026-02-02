package org.tianocore.edk2idea.Inf;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

final class InfColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Comment", InfSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Section header", InfSyntaxHighlighter.SECTION_HEADER),
            new AttributesDescriptor("Defines keys", InfSyntaxHighlighter.DEFINES_KEYS),
            new AttributesDescriptor("Defines values", InfSyntaxHighlighter.DEFINES_VALUES),
    };

    @Override
    public Icon getIcon() {
        return InfIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new InfSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return """
[Defines]
  INF_VERSION                    = 0x00010005
  BASE_NAME                      = DxeImageVerificationLib
  MODULE_UNI_FILE                = DxeImageVerificationLib.uni
  FILE_GUID                      = 0CA970E1-43FA-4402-BC0A-81AF336BFFD6
  MODULE_TYPE                    = DXE_DRIVER
  VERSION_STRING                 = 1.0
  LIBRARY_CLASS                  = NULL|DXE_DRIVER DXE_RUNTIME_DRIVER DXE_SMM_DRIVER UEFI_APPLICATION UEFI_DRIVER
  CONSTRUCTOR                    = DxeImageVerificationLibConstructor

#
# The following information is for reference only and not required by the build tools.
#
#  VALID_ARCHITECTURES           = IA32 X64 EBC
#

[Sources]
  DxeImageVerificationLib.c
  DxeImageVerificationLib.h
  Measurement.c

[Packages]
  MdePkg/MdePkg.dec
  MdeModulePkg/MdeModulePkg.dec
  CryptoPkg/CryptoPkg.dec
  SecurityPkg/SecurityPkg.dec

[LibraryClasses]
  MemoryAllocationLib
  BaseLib
  UefiLib
  UefiBootServicesTableLib
  UefiRuntimeServicesTableLib
  BaseMemoryLib
  DebugLib
  DevicePathLib
  BaseCryptLib
  SecurityManagementLib
  PeCoffLib
  TpmMeasurementLib

[Protocols]
  gEfiFirmwareVolume2ProtocolGuid       ## SOMETIMES_CONSUMES
  gEfiBlockIoProtocolGuid               ## SOMETIMES_CONSUMES
  gEfiSimpleFileSystemProtocolGuid      ## SOMETIMES_CONSUMES

[Guids]
  ## SOMETIMES_CONSUMES   ## Variable:L"DB"
  ## SOMETIMES_CONSUMES   ## Variable:L"DBX"
  ## SOMETIMES_CONSUMES   ## Variable:L"DBT"
  ## PRODUCES             ## SystemTable
  ## CONSUMES             ## SystemTable
  gEfiImageSecurityDatabaseGuid

  ## SOMETIMES_CONSUMES   ## GUID       # Unique ID for the type of the signature.
  ## SOMETIMES_PRODUCES   ## GUID       # Unique ID for the type of the signature.
  gEfiCertSha1Guid

  ## SOMETIMES_CONSUMES   ## GUID       # Unique ID for the type of the signature.
  ## SOMETIMES_PRODUCES   ## GUID       # Unique ID for the type of the signature.
  gEfiCertSha256Guid

  ## SOMETIMES_CONSUMES   ## GUID       # Unique ID for the type of the signature.
  ## SOMETIMES_PRODUCES   ## GUID       # Unique ID for the type of the signature.
  gEfiCertSha384Guid

  ## SOMETIMES_CONSUMES   ## GUID       # Unique ID for the type of the signature.
  ## SOMETIMES_PRODUCES   ## GUID       # Unique ID for the type of the signature.
  gEfiCertSha512Guid

  gEfiCertX509Guid                      ## SOMETIMES_CONSUMES    ## GUID     # Unique ID for the type of the signature.
  gEfiCertX509Sha256Guid                ## SOMETIMES_CONSUMES    ## GUID     # Unique ID for the type of the signature.
  gEfiCertX509Sha384Guid                ## SOMETIMES_CONSUMES    ## GUID     # Unique ID for the type of the signature.
  gEfiCertX509Sha512Guid                ## SOMETIMES_CONSUMES    ## GUID     # Unique ID for the type of the signature.
  gEfiCertPkcs7Guid                     ## SOMETIMES_CONSUMES    ## GUID     # Unique ID for the type of the certificate.

[Pcd]
  gEfiSecurityPkgTokenSpaceGuid.PcdOptionRomImageVerificationPolicy          ## SOMETIMES_CONSUMES
  gEfiSecurityPkgTokenSpaceGuid.PcdRemovableMediaImageVerificationPolicy     ## SOMETIMES_CONSUMES
  gEfiSecurityPkgTokenSpaceGuid.PcdFixedMediaImageVerificationPolicy         ## SOMETIMES_CONSUMES
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
        return "Inf";
    }

}
