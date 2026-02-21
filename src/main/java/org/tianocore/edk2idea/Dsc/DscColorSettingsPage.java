package org.tianocore.edk2idea.Dsc;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

final class DscColorSettingsPage implements ColorSettingsPage {

  private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[] {
      new AttributesDescriptor("Comment", DscSyntaxHighlighter.COMMENT),
      new AttributesDescriptor("Section header", DscSyntaxHighlighter.SECTION_HEADER),
      new AttributesDescriptor("Defines keys", DscSyntaxHighlighter.DEFINES_KEYS),
      new AttributesDescriptor("Defines values", DscSyntaxHighlighter.DEFINES_VALUES),
      new AttributesDescriptor("Command", DscSyntaxHighlighter.COMMAND),
      new AttributesDescriptor("Path string", DscSyntaxHighlighter.PATH_STRING),
      new AttributesDescriptor("PCD variable", DscSyntaxHighlighter.PCD_NAME),
  };

  @Override
  public Icon getIcon() {
    return DscIcons.FILE;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new DscSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return """
        [Defines]
            PLATFORM_NAME                  = Ovmf
            PLATFORM_GUID                  = 5a9e7754-d81b-49ea-85ad-69eaa7b1539b
            PLATFORM_VERSION               = 0.1
            DSC_SPECIFICATION              = 0x00010005
            OUTPUT_DIRECTORY               = Build/OvmfX64
            SUPPORTED_ARCHITECTURES        = X64
            BUILD_TARGETS                  = NOOPT|DEBUG|RELEASE
            SKUID_IDENTIFIER               = DEFAULT
            FLASH_DEFINITION               = OvmfPkg/OvmfPkgX64.fdf

            #
            # Defines for default states.  These can be changed on the command line.
            # -D FLAG=VALUE
            #
            DEFINE SECURE_BOOT_ENABLE      = FALSE
            DEFINE SMM_REQUIRE             = FALSE
            DEFINE QEMU_PV_VARS            = FALSE
            DEFINE STANDALONE_MM_ENABLE    = FALSE
            DEFINE SOURCE_DEBUG_ENABLE     = FALSE
            DEFINE CC_MEASUREMENT_ENABLE   = TRUE

          !include OvmfPkg/Include/Dsc/OvmfTpmDefines.dsc.inc

            #
            # Shell can be useful for debugging but should not be enabled for production
            #
            DEFINE BUILD_SHELL             = TRUE

            #
            # Network definition
            #
            DEFINE NETWORK_TLS_ENABLE             = FALSE
            DEFINE NETWORK_IP6_ENABLE             = FALSE
            DEFINE NETWORK_HTTP_BOOT_ENABLE       = FALSE
            DEFINE NETWORK_ALLOW_HTTP_CONNECTIONS = TRUE
            DEFINE NETWORK_ISCSI_ENABLE           = TRUE
            DEFINE NETWORK_ISCSI_DEFAULT_ENABLE   = FALSE
            DEFINE NETWORK_PXE_BOOT_ENABLE        = TRUE

          !include NetworkPkg/NetworkDefines.dsc.inc

            #
            # Device drivers
            #
          !include OvmfPkg/Include/Dsc/OvmfOptHwDefines.dsc.inc

            #
            # Flash size selection. Setting FD_SIZE_IN_KB on the command line directly to
            # one of the supported values, in place of any of the convenience macros, is
            # permitted.
            #
          !ifdef $(FD_SIZE_1MB)
            DEFINE FD_SIZE_IN_KB           = 1024
          !else
          !ifdef $(FD_SIZE_2MB)
            DEFINE FD_SIZE_IN_KB           = 2048
          !else
          !ifdef $(FD_SIZE_4MB)
            DEFINE FD_SIZE_IN_KB           = 4096
          !else
            DEFINE FD_SIZE_IN_KB           = 4096
          !endif
          !endif
          !endif

            #
            # Define the FILE_GUID of CpuMpPei/CpuDxe for unique-processor version.
            #
            DEFINE UP_CPU_PEI_GUID  = 280251c4-1d09-4035-9062-839acb5f18c1
            DEFINE UP_CPU_DXE_GUID  = 6490f1c5-ebcc-4665-8892-0075b9bb49b7

          !include OvmfPkg/Include/Dsc/OvmfPkg.dsc.inc

          [BuildOptions]
            GCC:RELEASE_*_*_CC_FLAGS             = -DMDEPKG_NDEBUG
            INTEL:RELEASE_*_*_CC_FLAGS           = /D MDEPKG_NDEBUG
            MSFT:RELEASE_*_*_CC_FLAGS            = /D MDEPKG_NDEBUG
          !if $(TOOL_CHAIN_TAG) != "XCODE5" && $(TOOL_CHAIN_TAG) != "CLANGPDB"
            GCC:*_*_*_CC_FLAGS                   = -mno-mmx -mno-sse
          !endif
          !if $(SOURCE_DEBUG_ENABLE) == TRUE
            MSFT:*_*_X64_GENFW_FLAGS  = --keepexceptiontable
            GCC:*_*_X64_GENFW_FLAGS   = --keepexceptiontable
            INTEL:*_*_X64_GENFW_FLAGS = --keepexceptiontable
          !endif
            RELEASE_*_*_GENFW_FLAGS = --zero

            #
            # Disable deprecated APIs.
            #
            MSFT:*_*_*_CC_FLAGS = /D DISABLE_NEW_DEPRECATED_INTERFACES
            INTEL:*_*_*_CC_FLAGS = /D DISABLE_NEW_DEPRECATED_INTERFACES
            GCC:*_*_*_CC_FLAGS = -D DISABLE_NEW_DEPRECATED_INTERFACES

            #
            # Add TDX_GUEST_SUPPORTED
            #
            MSFT:*_*_*_CC_FLAGS = /D TDX_GUEST_SUPPORTED
            INTEL:*_*_*_CC_FLAGS = /D TDX_GUEST_SUPPORTED
            GCC:*_*_*_CC_FLAGS = -D TDX_GUEST_SUPPORTED

          !include NetworkPkg/NetworkBuildOptions.dsc.inc

          [BuildOptions.common.EDKII.DXE_RUNTIME_DRIVER]
            GCC:*_*_*_DLINK_FLAGS = -z common-page-size=0x1000
            XCODE:*_*_*_DLINK_FLAGS = -seg1addr 0x1000 -segalign 0x1000
            XCODE:*_*_*_MTOC_FLAGS = -align 0x1000
            CLANGPDB:*_*_*_DLINK_FLAGS = /ALIGN:4096

          # Force PE/COFF sections to be aligned at 4KB boundaries to support page level
          # protection of MM/MM_CORE modules
          [BuildOptions.common.EDKII.DXE_SMM_DRIVER, BuildOptions.common.EDKII.SMM_CORE, BuildOptions.common.EDKII.MM_CORE_STANDALONE, BuildOptions.common.EDKII.MM_STANDALONE]
            GCC:*_*_*_DLINK_FLAGS = -z common-page-size=0x1000
            XCODE:*_*_*_DLINK_FLAGS = -seg1addr 0x1000 -segalign 0x1000
            XCODE:*_*_*_MTOC_FLAGS = -align 0x1000
            CLANGPDB:*_*_*_DLINK_FLAGS = /ALIGN:4096

          ################################################################################
          #
          # SKU Identification section - list of all SKU IDs supported by this Platform.
          #
          ################################################################################
          [SkuIds]
            0|DEFAULT

          ################################################################################
          #
          # Library Class section - list of all Library Classes needed by this Platform.
          #
          ################################################################################

          !include MdePkg/MdeLibs.dsc.inc

          [LibraryClasses]
            SmmRelocationLib|OvmfPkg/Library/SmmRelocationLib/SmmRelocationLib.inf
            PcdLib|MdePkg/Library/BasePcdLibNull/BasePcdLibNull.inf
          !if $(STANDALONE_MM_ENABLE) != TRUE
            MemEncryptSevLib|OvmfPkg/Library/BaseMemEncryptSevLib/DxeMemEncryptSevLib.inf
          !else
            # CC is not supported with standalone MM enabled
            MemEncryptSevLib|OvmfPkg/Library/MemEncryptSevLibNull/MemEncryptSevLibNull.inf
          !endif
            MemEncryptTdxLib|OvmfPkg/Library/BaseMemEncryptTdxLib/BaseMemEncryptTdxLib.inf
            PeiHardwareInfoLib|OvmfPkg/Library/HardwareInfoLib/PeiHardwareInfoLib.inf
            DxeHardwareInfoLib|OvmfPkg/Library/HardwareInfoLib/DxeHardwareInfoLib.inf
            ImagePropertiesRecordLib|MdeModulePkg/Library/ImagePropertiesRecordLib/ImagePropertiesRecordLib.inf
            HstiLib|MdePkg/Library/DxeHstiLib/DxeHstiLib.inf

          [LibraryClasses.common]
            AmdSvsmLib|OvmfPkg/Library/AmdSvsmLib/AmdSvsmLib.inf
            BaseCryptLib|CryptoPkg/Library/BaseCryptLib/BaseCryptLib.inf
            CcExitLib|OvmfPkg/Library/CcExitLib/CcExitLib.inf
            TdxLib|MdePkg/Library/TdxLib/TdxLib.inf
            TdxMailboxLib|OvmfPkg/Library/TdxMailboxLib/TdxMailboxLib.inf
          !if $(DEBUG_TO_MEM)
            MemDebugLogLib|OvmfPkg/Library/MemDebugLogLib/MemDebugLogDxeLib.inf
          !else
            MemDebugLogLib|OvmfPkg/Library/MemDebugLogLib/MemDebugLogLibNull.inf
          !endif

          ################################################################################
          #
          # Pcd Section - list of all EDK II PCD Entries defined by this Platform.
          #
          ################################################################################
          [PcdsFeatureFlag]
            gEfiMdeModulePkgTokenSpaceGuid.PcdHiiOsRuntimeSupport|FALSE
          !if $(SMM_REQUIRE) == TRUE
            gUefiOvmfPkgTokenSpaceGuid.PcdSmmSmramRequire|TRUE
          !endif


          [PcdsFixedAtBuild]
            gEfiMdeModulePkgTokenSpaceGuid.PcdStatusCodeMemorySize|1
          !if $(SMM_REQUIRE) == FALSE
            gEfiMdeModulePkgTokenSpaceGuid.PcdResetOnMemoryTypeInformationChange|FALSE
          !endif

          ################################################################################
          #
          # Components Section - list of all EDK II Modules needed by this Platform.
          #
          ################################################################################
          [Components]
            OvmfPkg/ResetVector/ResetVector.inf

            #
            # SEC Phase modules
            #
            OvmfPkg/Sec/SecMain.inf {
              <LibraryClasses>
                NULL|MdeModulePkg/Library/LzmaCustomDecompressLib/LzmaCustomDecompressLib.inf
                NULL|OvmfPkg/IntelTdx/TdxHelperLib/SecTdxHelperLib.inf
                BaseCryptLib|CryptoPkg/Library/BaseCryptLib/SecCryptLib.inf
            }

            #
            # PEI Phase modules
            #
            MdeModulePkg/Core/Pei/PeiMain.inf
          !if $(DEBUG_TO_MEM)
            OvmfPkg/MemDebugLogPei/MemDebugLogPei.inf
          !endif

            #
            # Smbios Measurement support
            #
          !if $(TPM2_ENABLE) == TRUE || $(CC_MEASUREMENT_ENABLE) == TRUE
            MdeModulePkg/Universal/SmbiosMeasurementDxe/SmbiosMeasurementDxe.inf
          !endif

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
    return "Dsc";
  }

}
