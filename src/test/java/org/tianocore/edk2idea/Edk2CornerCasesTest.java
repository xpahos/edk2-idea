package org.tianocore.edk2idea;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.psi.PsiReference;
import org.junit.Assert;

public class Edk2CornerCasesTest extends BasePlatformTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testDscNestedDefines() {
    myFixture.addFileToProject("Component.inf", "");
    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLATFORM_NAME = Test

        [Components]
          Component.inf {
            <Defines>
              FILE_GUID = 12345678-1234-1234-1234-123456789012
              VERSION_STRING = 1.0
          }
        """);
    myFixture.checkHighlighting(); // specific verify parser no error
  }

  public void testFdfSectionReproduction() {
    myFixture.addFileToProject("NetworkPkg/Network.fdf.inc", "");
    myFixture.addFileToProject("OvmfPkg/VirtioNetDxe/VirtioNet.inf", "");

    myFixture.configureByText("Test.fdf", """
        !if $(E1000_ENABLE)
          FILE DRIVER = 5D695E11-9B3F-4b83-B25F-4A8D5D69BE07 {
            SECTION PE32 = Intel3.5/EFIX64/E3522X2.EFI
          }
        !endif
        !include NetworkPkg/Network.fdf.inc
        !if $(NETWORK_ENABLE) == TRUE
          INF  OvmfPkg/VirtioNetDxe/VirtioNet.inf
        !endif
        """);
    myFixture.checkHighlighting();
  }

  public void testFdfDataBlockReproduction() {
    myFixture.configureByText("Test.fdf", """
        [FV.Internal]
        DATA = {
          0x00, 0x00, 0x00, 0x00,
          !if $(FD_SIZE_IN_KB) == 1024
            0x20, 0x00,
          !endif
          { 0x01, 0x02 }
        }
        """);
    myFixture.checkHighlighting();
  }

  public void testDscHeadlessReproduction() {
    myFixture.addFileToProject("NetworkPkg/Library/DxeDpcLib/DxeDpcLib.inf", "");
    myFixture.configureByText("Test.dsc.inc", """
        DpcLib|NetworkPkg/Library/DxeDpcLib/DxeDpcLib.inf
        OtherLib|NetworkPkg/Library/OtherLib/OtherLib.inf
        """);
    myFixture.checkHighlighting();
  }

  public void testDscNestedPcdReproduction() {
    myFixture.configureByText("Test.dsc", """
        [Components]
        FmpDevicePkg/FmpDxe/FmpDxe.inf {
          <Defines>
            FILE_GUID = $(FMP_SYSTEM_DEVICE)
          <PcdsFixedAtBuild>
            gFmpDevicePkgTokenSpaceGuid.PcdFmpDeviceImageIdName|L"RISC-V VIRT System Firmware Device"
        }
        """);
    myFixture.checkHighlighting();
  }

  public void testDscBuildOptionsReproduction() {
    myFixture.configureByText("Test.dsc", """
        [BuildOptions]
        MSFT:*_*_*_CXX_FLAGS         = /std:c++20 /Zc:strictStrings- /wd4244
        GCC:*_*_*_CXX_FLAGS          = -std=c++20
        """);
    myFixture.checkHighlighting();
  }

  public void testDscBuildOptionsDoubleEqReproduction() {
    myFixture.configureByText("Test.dsc", """
        [BuildOptions]
        MSFT:*_*_*_DLINK_FLAGS   == /out:"$(BIN_DIR)\\(MODULE_NAME_GUID).exe"
        """);
    myFixture.checkHighlighting();
  }

  public void testDscComparisonOperators() {
    myFixture.configureByText("Test.dsc", """
        [LibraryClasses]
        !if $(FD_SIZE_IN_KB) < 4096
          OpensslLib|CryptoPkg/Library/OpensslLib/OpensslLib.inf
        !endif
        !if $(FD_SIZE_IN_KB) > 4096
          OpensslLib|CryptoPkg/Library/OpensslLib/OpensslLib.inf
        !endif
        !if $(FD_SIZE_IN_KB) == 4096
          OpensslLib|CryptoPkg/Library/OpensslLib/OpensslLib.inf
        !endif
        !if $(FD_SIZE_IN_KB) != 4096
          OpensslLib|CryptoPkg/Library/OpensslLib/OpensslLib.inf
        !endif
        !if $(FD_SIZE_IN_KB) <= 4096
          OpensslLib|CryptoPkg/Library/OpensslLib/OpensslLib.inf
        !endif
        !if $(FD_SIZE_IN_KB) >= 4096
          OpensslLib|CryptoPkg/Library/OpensslLib/OpensslLib.inf
        !endif
        """);
    myFixture.checkHighlighting();
  }

  public void testInfSourceOverrideReproduction() {
    myFixture.configureByText("Test.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = TestModule
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE = UEFI_DRIVER
          VERSION_STRING = 1.0
          ENTRY_POINT = UefiMain

        [Sources.IA32]
          Ia32/InternalGetSpinLockProperties.c | MSFT
        """);
    myFixture.checkHighlighting();
  }

  public void testInfSourcesMultiArch() {
    myFixture.configureByText("TestMultiArch.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = TestModule
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE = UEFI_DRIVER
          VERSION_STRING = 1.0
          ENTRY_POINT = UefiMain

        [Sources.IA32, Sources.X64]
          CpuTimerLib.c
          BaseCpuTimerLib.c
        """);
    myFixture.checkHighlighting();
  }

  public void testInfHostApplication() {
    myFixture.configureByText("TestHostApp.inf", """
        [Defines]
          INF_VERSION                    = 0x00010006
          BASE_NAME                      = BaseLibUnitTestsHost
          FILE_GUID                      = 1d005f4c-4dfa-41b5-ab0c-be91fe121459
          MODULE_TYPE                    = HOST_APPLICATION
          VERSION_STRING                 = 1.0
        """);
    myFixture.checkHighlighting();
  }

  public void testInfRelativePathSources() {
    myFixture.configureByText("TestRelativePath.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = TestModule
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE = UEFI_DRIVER
          VERSION_STRING = 1.0
          ENTRY_POINT = UefiMain

        [Sources]
          DxeTpm2MeasureBootLibSanitizationTest.c
          ../DxeTpm2MeasureBootLibSanitization.c
        """);
    myFixture.checkHighlighting();
  }

  public void testInfProtocolFeatureFlagPcd() {
    myFixture.configureByText("TestProtocolPcd.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = TestModule
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE = UEFI_DRIVER
          VERSION_STRING = 1.0
          ENTRY_POINT = UefiMain

        [Protocols]
          gEfiSmmSwapAddressRangeProtocolGuid | gEfiMdeModulePkgTokenSpaceGuid.PcdFullFtwServiceEnable
        """);
    myFixture.checkHighlighting();
  }

  public void testInfEfiSpecVersion() {
    myFixture.configureByText("TestEfiSpec.inf", """
        [Defines]
          INF_VERSION                    = 0x00010005
          BASE_NAME                      = EmuSnpDxe
          FILE_GUID                      = 22597239-6107-DF44-AD3F-5F053E92222E
          MODULE_TYPE                    = UEFI_DRIVER
          VERSION_STRING                 = 1.0
          EDK_RELEASE_VERSION            = 0x00020000
          EFI_SPECIFICATION_VERSION      = 0x00020000
        """);
    myFixture.checkHighlighting();
  }

  public void testInfValidArchitectures() {
    myFixture.configureByText("TestValidArch.inf", """
        [Defines]
          INF_VERSION                    = 0x0001001A
          BASE_NAME                      = VirtMmCommunication
          FILE_GUID                      = 0B807404-2D1C-4066-95F4-F28A58800185
          MODULE_TYPE                    = DXE_RUNTIME_DRIVER
          VERSION_STRING                 = 1.0
          ENTRY_POINT                    = VirtMmCommunication2Initialize
          VALID_ARCHITECTURES            = X64 AARCH64 RISCV64
        """);
    myFixture.checkHighlighting();
  }

  public void testInfDepexTrue() {
    myFixture.configureByText("TestDepex.inf", """
        [Defines]
          INF_VERSION                    = 0x00010005
          BASE_NAME                      = TestModule
          FILE_GUID                      = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE                    = UEFI_DRIVER
          VERSION_STRING                 = 1.0
          ENTRY_POINT                    = UefiMain

        [Depex]
          TRUE
        """);
    myFixture.checkHighlighting();
  }

  public void testInfDepexUppercase() {
    myFixture.configureByText("TestDepexUpper.inf", """
        [Defines]
          INF_VERSION                    = 0x00010005
          BASE_NAME                      = TestModule
          FILE_GUID                      = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE                    = UEFI_DRIVER
          VERSION_STRING                 = 1.0
          ENTRY_POINT                    = UefiMain

        [DEPEX]
          TRUE
        """);
    myFixture.checkHighlighting();
  }

  public void testInfLibraryClassHostApp() {
    myFixture.configureByText("TestLibHostApp.inf", """
        [Defines]
          INF_VERSION                    = 0x00010005
          BASE_NAME                      = BaseCryptLib
          FILE_GUID                      = 5C14CE62-000E-4CC6-803C-683018EA5F97
          MODULE_TYPE                    = DXE_DRIVER
          VERSION_STRING                 = 1.0
          LIBRARY_CLASS                  = BaseCryptLib|HOST_APPLICATION
        """);
    myFixture.checkHighlighting();
  }

  public void testInfPiSpecVersion() {
    myFixture.configureByText("TestPiSpec.inf", """
        [Defines]
          INF_VERSION               = 1.25
          BASE_NAME                 = SpiNorFlashJedecSfdpDxe
          FILE_GUID                 = 0DC9C2C7-D450-41BA-9CF7-D2090C35A797
          MODULE_TYPE               = DXE_DRIVER
          VERSION_STRING            = 0.1
          PI_SPECIFICATION_VERSION  = 1.10
        """);
    myFixture.checkHighlighting();
  }

  public void testInfDefineInDefines() {
    myFixture.configureByText("TestDefine.inf", """
        [Defines]
          INF_VERSION                    = 0x00010005
          BASE_NAME                      = OpensslLibCrypto
          MODULE_UNI_FILE                = OpensslLibCrypto.uni
          FILE_GUID                      = E29FC209-8B64-4500-BD20-AF4EAE47EA0E
          MODULE_TYPE                    = BASE
          VERSION_STRING                 = 1.0
          LIBRARY_CLASS                  = OpensslLib
          CONSTRUCTOR                    = OpensslLibConstructor

          DEFINE OPENSSL_PATH            = openssl
          DEFINE OPENSSL_FLAGS           = -DOPENSSL_FLAGS_NOASM
        """);
    myFixture.checkHighlighting();
  }

  public void testInfDefineComplexFlags() {
    myFixture.configureByText("TestDefineFlags.inf",
        """
            [Defines]
              INF_VERSION                    = 0x00010005
              BASE_NAME                      = OpensslLibCrypto
              MODULE_UNI_FILE                = OpensslLibCrypto.uni
              FILE_GUID                      = E29FC209-8B64-4500-BD20-AF4EAE47EA0E
              MODULE_TYPE                    = BASE
              VERSION_STRING                 = 1.0
              LIBRARY_CLASS                  = OpensslLib
              CONSTRUCTOR                    = OpensslLibConstructor

              DEFINE OPENSSL_FLAGS           = -DL_ENDIAN -DOPENSSL_SMALL_FOOTPRINT -D_CRT_SECURE_NO_DEPRECATE -D_CRT_NONSTDC_NO_DEPRECATE -DEDK2_OPENSSL_NOEC=1 -DOPENSSL_NO_ASM
              DEFINE OPENSSL_FLAGS_NOASM     =
              DEFINE OPENSSL_FLAGS_IA32      = -DAES_ASM -DGHASH_ASM -DMD5_ASM -DOPENSSL_CPUID_OBJ -DSHA1_ASM -DSHA256_ASM -DSHA512_ASM -DVPAES_ASM
              DEFINE OPENSSL_FLAGS_X64       = -DAES_ASM -DBSAES_ASM -DGHASH_ASM -DKECCAK1600_ASM -DMD5_ASM -DOPENSSL_CPUID_OBJ -DSHA1_ASM -DSHA256_ASM -DSHA512_ASM -DVPAES_ASM
            """);
    myFixture.checkHighlighting();
  }

  public void testFdfSetStatementOperators() {
    myFixture.configureByText("TestSet.fdf",
        """
            [Defines]
              # Arithmetic
              SET A = 1 + 2
              SET B = 1 - 2
              SET C = 1 * 2
              SET D = 1 / 2
              SET E = 1 % 2

              # Bitwise
              SET F = 1 | 2
              SET G = 1 & 2
              SET H = 1 ^ 2
              SET I = ~1

              # Complex
              SET J = (1 + 2) * 3
              SET K = (1 | 2) & 3
            """);
    myFixture.checkHighlighting();
  }

  public void testFdfRuleFileStatement() {
    myFixture.configureByText("TestRule.fdf", """
        [Rule.Common.SEC]
          FILE SEC = $(NAMED_GUID) {
            PE32     PE32           $(INF_OUTPUT)/$(MODULE_NAME).efi
            UI       STRING ="$(MODULE_NAME)" Optional
            VERSION  STRING ="$(INF_VERSION)" Optional BUILD_NUM=$(BUILD_NUMBER)
          }
        """);
    myFixture.checkHighlighting();
  }

  public void testFdfDataBlockRule() {
    myFixture.configureByText("TestData.fdf", """
        [Rule.Common.SEC]
          FILE SEC = $(NAMED_GUID) {
            DATA = {
              0x7f, 0x45, 0x4c, 0x46
            }
          }
        """);
    myFixture.checkHighlighting();
  }

  public void testFdfFdRegionReproduction() {
    myFixture.configureByText("TestFd.fdf", """
        [FD.Test]
        0x00008000|0x001f8000
        gArmTokenSpaceGuid.PcdFvBaseAddress|gArmTokenSpaceGuid.PcdFvSize
        FV = FVMAIN_COMPACT
        """);
    myFixture.checkHighlighting();
  }

  public void testFdfDataBlock() {
    myFixture.configureByText("TestData.fdf",
        """
                  DATA = {
            # ELF file header
            0x7f, 0x45, 0x4c, 0x46, 0x02, 0x01, 0x01, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00,
            0xd0, 0xff, 0x4f, 0x00, 0x00, 0x00, 0x00, 0x00, # hdr.e_entry
            0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x40, 0x00, 0x38, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                  }
                  """);
    myFixture.checkHighlighting();
  }

  public void testDecIncludesArchReproduction() {
    myFixture.configureByText("Test.dec", """
        [Includes]
          Include
          Test/UnitTest/Include
          Test/Mock/Include
          Library/MipiSysTLib/mipisyst/library/include

        [Includes.IA32]
          Include/Ia32

        [Includes.X64]
          Include/X64
        """);
    myFixture.checkHighlighting();
  }

  public void testDecLibraryClassesReproduction() {
    myFixture.configureByText("TestLibs.dec", """
        [LibraryClasses.IA32, LibraryClasses.X64]
          ##  @libraryclass  ...
          MtrrLib|Include/Library/MtrrLib.h

        [LibraryClasses.AARCH64]
          DynamicTablesScmiInfoLib|Include/Library/DynamicTablesScmiInfoLib.h

        [LibraryClasses.X64.PEIM]
          MmPlatformHobProducerLib|Include/Library/MmPlatformHobProducerLib.h
        """);
    myFixture.checkHighlighting();
  }

  public void testDecIncludesCommonReproduction() {
    myFixture.configureByText("TestCommon.dec", """
        [Includes.common]
          Include
        [Includes.Common.Private]
          PrivateInclude/Crt
          Library/JsonLib
          Library/JsonLib/jansson/src
        """);
    myFixture.checkHighlighting();
  }

  public void testDecPcdReproduction() {
    myFixture.configureByText("TestPcd.dec", """
        [PcdsFeatureFlag]
          gEfiMdePkgTokenSpaceGuid.PcdComponentNameDisable|FALSE|BOOLEAN|0x0000000d
        [PcdsFixedAtBuild, PcdsPatchableInModule]
          gStandaloneMmPkgTokenSpaceGuid.PcdFwVolMmMaxEncapsulationDepth|0x10|UINT32|0x00000001
        [PcdsFixedAtBuild]
          gEdkiiDynamicTablesPkgTokenSpaceGuid.PcdMaxCustomACPIGenerators|1|UINT16|0xC0000001
        """);
    myFixture.checkHighlighting();
  }

  public void testDecPcdByteArrayReproduction() {
    myFixture.configureByText("TestPcdByteArray.dec",
        """
            [PcdsFixedAtBuild.AARCH64, PcdsPatchableInModule.AARCH64]
              gEfiMdePkgTokenSpaceGuid.PcdCpuRngSupportedAlgorithm|{0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}|VOID*|0x00000037
            """);
    myFixture.checkHighlighting();
  }

  public void testDecUserExtensionsReproduction() {
    myFixture.configureByText("TestUserExtensions.dec", """
        [UserExtensions.TianoCore."ExtraFiles"]
          MdePkgExtra.uni
        """);
    myFixture.checkHighlighting();
  }

  public void testDecPcdValuesReproduction() {
    myFixture.configureByText("TestPcdValues.dec",
        """
            [PcdsFixedAtBuild, PcdsDynamic, PcdsDynamicEx, PcdsPatchableInModule]
              gPcAtChipsetPkgTokenSpaceGuid.PcdRtcDefaultYear|gPcAtChipsetPkgTokenSpaceGuid.PcdMinimalValidYear|UINT16|0x0000000F
              gEfiSecurityPkgTokenSpaceGuid.PcdFixedUsbCredentialProviderTokenFileName|L"Token.bin"|VOID*|0x00000005
              gUefiOvmfPkgTokenSpaceGuid.PcdOvmfPeiMemFvBase|0x0|UINT32|0
            """);
    myFixture.checkHighlighting();
  }

  public void testDecPcdCustomTypeReproduction() {
    myFixture.configureByText("TestPcdCustomType.dec",
        """
            [PcdsFixedAtBuild]
              gEfiRedfishPkgTokenSpaceGuid.PcdRedfishRestExServiceDevicePath|{0x0}|REST_EX_SERVICE_DEVICE_PATH_DATA|0x00001000 {
                <HeaderFiles>
                  Pcd/RestExServiceDevicePath.h
                <Packages>
                  MdePkg/MdePkg.dec
                  MdeModulePkg/MdeModulePkg.dec
                  RedfishPkg/RedfishPkg.dec
              }
            """);
    myFixture.checkHighlighting();
  }

  public void testDecPcdByteArrayDecimalReproduction() {
    myFixture.configureByText("TestPcdByteArrayDecimal.dec", """
        [PcdsFixedAtBuild, PcdsPatchableInModule, PcdsDynamic, PcdsDynamicEx]
          gFmpDevicePkgTokenSpaceGuid.PcdFmpDeviceImageTypeIdGuid|{0}|VOID*|0x40000010
        """);
    myFixture.checkHighlighting();
  }

  public void testInfBuildOptionsReproduction() {
    myFixture.configureByText("TestBuildOptions.inf", """
        [Defines]
          INF_VERSION = 0x00010017
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = BASE
        [BuildOptions]
          GCC:*_*_AARCH64_CC_XIPFLAGS ==
        """);
    myFixture.checkHighlighting();
  }

  public void testUniSlashReproduction() {
    myFixture.configureByText("TestSlash.uni", """
        /=#

        #langdef en-US "English"
        """);
    myFixture.checkHighlighting();
  }

  public void testInfMmStandaloneReproduction() {
    myFixture.configureByText("TestMmStandalone.inf", """
        [Defines]
          INF_VERSION = 1.25
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE = BASE
          VERSION_STRING = 1.0
          LIBRARY_CLASS = DebugLib|DXE_CORE MM_STANDALONE
        """);
    myFixture.checkHighlighting();
  }

  public void testInfLibraryClassMmStandalone() {
    myFixture.configureByText("TestLibMmStandalone.inf",
        """
            [Defines]
              INF_VERSION                    = 1.25
              BASE_NAME                      = DebugLibFdtPL011UartRam
              FILE_GUID                      = 0584DE55-9C4C-49C1-ADA0-F62C9C1F3600
              MODULE_TYPE                    = BASE
              VERSION_STRING                 = 1.0
              LIBRARY_CLASS  = DebugLib|DXE_CORE SMM_CORE MM_CORE_STANDALONE DXE_DRIVER DXE_SMM_DRIVER SMM_DRIVER MM_STANDALONE UEFI_DRIVER UEFI_APPLICATION
            """);
    myFixture.checkHighlighting();
  }

  public void testInfPcdDefaultValueCrash() {
    myFixture.configureByText("TestPcdDefault.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = BASE
        [Pcd]
          gEfiMdePkgTokenSpaceGuid.PcdUartDefaultBaudRate|115200    ## CONSUMES
        """);
    myFixture.checkHighlighting();
  }

  public void testInfMmStandaloneModuleTypeCrash() {
    myFixture.configureByText("TestMmStandalone.inf", """
        [Defines]
          INF_VERSION                    = 0x00010005
          BASE_NAME                      = VarCheckPolicyLibStandaloneMm
          FILE_GUID                      = 44B09E3D-5EDA-4673-ABCF-C8AE4560C8EC
          MODULE_TYPE                    = MM_STANDALONE
        """);
    myFixture.checkHighlighting();
  }

  public void testDscBuildOptionsEmptyValueCrash() {
    myFixture.configureByText("TestBuildOptionsEmpty.dsc", """
        [BuildOptions]
          GCC:*_CLANGDWARF_X64_DLINK2_FLAGS  ==
          GCC:*_CLANGDWARF_IA32_DLINK2_FLAGS ==
        """);
    myFixture.checkHighlighting();
  }

  public void testInfPcdStringValueCrash() {
    myFixture.configureByText("TestPcdString.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = BASE
        [Pcd]
          gEmulatorPkgTokenSpaceGuid.PcdEmuGop|L"GOP Window"
        """);
    myFixture.checkHighlighting();
  }

  public void testUniParserCrash() {
    myFixture.configureByText("TestParser.uni",
        """
            #string STR_DUMP_DYN_PCD_HELP_INFORMATION       #language en-US ""
                                                                            ".TH DumpDynPcd 0 "Dump dynamic[ex] PCD info."\\r\\n"
                                                                            ".SH NAME\\r\\n"
            """);
    myFixture.checkHighlighting();
  }

  public void testUniParserTypoCrash() {
    myFixture.configureByText("TestParserTypo.uni", """
        #string STR_NULL
        #lauguage fr-FR  ""
        """);
    myFixture.checkHighlighting();
  }

  public void testInfProtocolFeatureFlagCrash() {
    myFixture.configureByText("TestProtocol.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = BASE
        [Protocols]
          gEfiComponentNameProtocolGuid  | NOT gEfiMdePkgTokenSpaceGuid.PcdComponentNameDisable
        """);
    myFixture.checkHighlighting();
  }

  public void testInfBuildOptionsCommaCrash() {
    myFixture.configureByText("TestBuildOptionsComma.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = BASE
        [BuildOptions]
          GCC:*_GCC_AARCH64_CC_FLAGS = -fpie
          GCC:*_GCC_AARCH64_DLINK_FLAGS = -Wl,-z,text,-Bsymbolic,-pie
          GCC:*_CLANGDWARF_AARCH64_CC_FLAGS = -fpie
          GCC:*_CLANGDWARF_AARCH64_DLINK_FLAGS = -Wl,-z,text,-Bsymbolic,-pie
        """);
    myFixture.checkHighlighting();
  }

  public void testFdfRuleBlockCrash() {
    myFixture.configureByText("TestRule.fdf", """
        [Rule.Common.DXE_CORE]
          FILE DXE_CORE = $(NAMED_GUID) {
            COMPRESS PI_STD {
              PE32     PE32           $(INF_OUTPUT)/$(MODULE_NAME).efi
              UI       STRING="$(MODULE_NAME)" Optional
              VERSION  STRING="$(INF_VERSION)" Optional BUILD_NUM=$(BUILD_NUMBER)
            }
          }
        """);
    myFixture.checkHighlighting();
  }

  public void testInfDoublePipeCrash() {
    myFixture.configureByText("TestDoublePipe.inf",
        """
            [Defines]
              INF_VERSION = 0x00010005
              BASE_NAME = Test
              FILE_GUID = 00000000-0000-0000-0000-000000000000
              VERSION_STRING = 1.0
              MODULE_TYPE = BASE
            [Sources]
              $(OPENSSL_GEN_PATH)/IA32-MSFT/crypto/aes/aes-586.nasm ||||gEfiCryptoPkgTokenSpaceGuid.PcdOpensslLibAssemblySourceStyleNasm
            """);
    myFixture.checkHighlighting();
  }

  public void testInfFeatureFlagKeywords() {
    myFixture.configureByText("TestKeywords.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = BASE
        [Sources]
          File.c | | | | Token.Pcd1 AND Token.Pcd2
          File2.c | | | | Token.Pcd1 OR Token.Pcd2
          File3.c | | | | NOT Token.Pcd3
        """);
    myFixture.checkHighlighting();
  }

  public void testDscSectionWithMacro() {
    myFixture.configureByText("TestMacroSection.dsc", """
        [Defines]
          PLATFORM_NAME = Test
          PLATFORM_GUID = 00000000-0000-0000-0000-000000000000
          PLATFORM_VERSION = 1.0
          DSC_SPECIFICATION = 0x00010005
          OUTPUT_DIRECTORY = Build/Test
          SUPPORTED_ARCHITECTURES = IA32|X64
          BUILD_TARGETS = DEBUG|RELEASE
          SKUID_IDENTIFIER = DEFAULT

        [Components.$(FSP_ARCH)]
          }
        """);
    myFixture.checkHighlighting();
  }

  public void testDecSectionWithMacro() {
    myFixture.configureByText("TestMacroSection.dec", """
        [Defines]
          DEC_SPECIFICATION              = 0x00010005
          PACKAGE_NAME                   = TestPkg
          PACKAGE_GUID                   = 00000000-0000-0000-0000-000000000000
          PACKAGE_VERSION                = 1.0

        [Includes.$(ARCH)]
          Include
        """);
    myFixture.checkHighlighting();
  }

  public void testInfSectionWithMacro() {
    myFixture.configureByText("TestMacroSection.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = BASE

        [LibraryClasses.$(ARCH)]
          DebugLib
        """);
    myFixture.checkHighlighting();
  }

  public void testDepexWithComment() {
    myFixture.configureByText("TestDepexComment.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = DXE_DRIVER

        [Depex]
          TRUE # comment
          AND FALSE
        """);
    myFixture.checkHighlighting();
  }

  public void testDepexMacro() {
    myFixture.configureByText("TestDepexMacro.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = DXE_DRIVER

        [Depex]
          $(TEST_MACRO)
        """);
    myFixture.checkHighlighting();
  }

  public void testDepexPcd() {
    myFixture.configureByText("TestDepexPcd.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          VERSION_STRING = 1.0
          MODULE_TYPE = DXE_DRIVER

        [Depex]
          gEfiTokenSpaceGuid.PcdName
        """);
    myFixture.checkHighlighting();
  }

  public void testDepexIdentifier() {
    myFixture.configureByText("DxeMpInitLibUpDepLib.inf", """
        [Defines]
          INF_VERSION                    = 0x00010005
          BASE_NAME                      = DxeMpInitLibUpDepLib
          FILE_GUID                      = 95FA4B7B-930E-4755-A9B7-10F0716DA374
          MODULE_TYPE                    = BASE
          VERSION_STRING                 = 1.0
          LIBRARY_CLASS                  = NULL

        [Depex]
          gEfiMpInitLibUpDepProtocolGuid
        """);
    myFixture.checkHighlighting();
  }

  public void testDepexNoNewline() {
    // Manually construct string without trailing newline
    String content = "[Defines]\n" +
        "  INF_VERSION = 0x00010005\n" +
        "  BASE_NAME = Test\n" +
        "  FILE_GUID = 00000000-0000-0000-0000-000000000000\n" +
        "  VERSION_STRING = 1.0\n" +
        "  MODULE_TYPE = DXE_DRIVER\n" +
        "\n" +
        "[Depex]\n" +
        "  gEfiMpInitLibUpDepProtocolGuid";
    myFixture.configureByText("TestDepexNoNewline.inf", content);
    myFixture.checkHighlighting();
  }

  public void testDepexCarriageReturn() {
    // Manually construct string with CR only
    String content = "[Defines]\n" +
        "  INF_VERSION = 0x00010005\n" +
        "  BASE_NAME = Test\n" +
        "  FILE_GUID = 00000000-0000-0000-0000-000000000000\n" +
        "  VERSION_STRING = 1.0\n" +
        "  MODULE_TYPE = DXE_DRIVER\n" +
        "\n" +
        "[Depex]\r" +
        "  gEfiMpInitLibUpDepProtocolGuid\r";
    myFixture.configureByText("TestDepexCR.inf", content);
    myFixture.checkHighlighting();
  }
}
