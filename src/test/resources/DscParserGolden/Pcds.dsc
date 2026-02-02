[PcdsFeatureFlag]
  gEfiMdeModulePkgTokenSpaceGuid.PcdHiiOsRuntimeSupport|FALSE
  gEfiMdeModulePkgTokenSpaceGuid.PcdConOutGopSupport|TRUE

[PcdsFixedAtBuild]
  gEfiMdeModulePkgTokenSpaceGuid.PcdStatusCodeMemorySize|1
  gEfiMdePkgTokenSpaceGuid.PcdMaximumLinkedListLength|0
  gEfiMdeModulePkgTokenSpaceGuid.PcdBootManagerMenuFile|{ 0xdc, 0x5b, 0xc2, 0xee, 0xf2, 0x67, 0x95, 0x4d, 0xb1, 0xd5, 0xf8, 0x1b, 0x20, 0x39, 0xd1, 0x1d }

[PcdsDynamicDefault]
  gEfiMdeModulePkgTokenSpaceGuid.PcdEmuVariableNvStoreReserved|0
  gUefiOvmfPkgTokenSpaceGuid.PcdPciIoBase|0x0

  !if $(SMM_REQUIRE) == TRUE
    gUefiOvmfPkgTokenSpaceGuid.PcdSmmSmramRequire|TRUE
  !endif
