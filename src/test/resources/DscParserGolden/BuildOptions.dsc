[BuildOptions]
  GCC:RELEASE_*_*_CC_FLAGS = -DMDEPKG_NDEBUG
  INTEL:RELEASE_*_*_CC_FLAGS = /D MDEPKG_NDEBUG
  MSFT:RELEASE_*_*_CC_FLAGS = /D MDEPKG_NDEBUG

  !if $(TOOL_CHAIN_TAG) == "GCC"
    GCC:*_*_*_CC_FLAGS = -Os
  !endif

[BuildOptions.common.EDKII.DXE_RUNTIME_DRIVER]
  GCC:*_*_*_DLINK_FLAGS = -z common-page-size=0x1000
  XCODE:*_*_*_DLINK_FLAGS = -seg1addr 0x1000 -segalign 0x1000
