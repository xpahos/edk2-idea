[Components]
  OvmfPkg/ResetVector/ResetVector.inf

  OvmfPkg/Sec/SecMain.inf {
    <LibraryClasses>
      NULL|MdeModulePkg/Library/LzmaCustomDecompressLib/LzmaCustomDecompressLib.inf
      BaseCryptLib|CryptoPkg/Library/BaseCryptLib/SecCryptLib.inf
  }

  MdeModulePkg/Universal/PCD/Pei/Pcd.inf {
    <LibraryClasses>
      PcdLib|MdePkg/Library/BasePcdLibNull/BasePcdLibNull.inf
  }

  !if $(DEBUG_TO_MEM)
    OvmfPkg/MemDebugLogPei/MemDebugLogPei.inf
  !endif
