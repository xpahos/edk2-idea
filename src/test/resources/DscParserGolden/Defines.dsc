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

  # こんにちは世界 (Hello World)
  DEFINE SECURE_BOOT_ENABLE      = FALSE # テスト
  DEFINE TEST_UNICODE            = L"ユニコード文字列"

  !include OvmfPkg/Include/Dsc/OvmfTpmDefines.dsc.inc

  #
  # Shell can be useful for debugging but should not be enabled for production
  #
  DEFINE BUILD_SHELL             = TRUE
