!include MdePkg/MdeLibs.dsc.inc

[LibraryClasses]
  SmmRelocationLib|OvmfPkg/Library/SmmRelocationLib/SmmRelocationLib.inf
  PcdLib|MdePkg/Library/BasePcdLibNull/BasePcdLibNull.inf
  TimerLib|OvmfPkg/Library/AcpiTimerLib/BaseAcpiTimerLib.inf

[LibraryClasses.common.SEC]
  TimerLib|OvmfPkg/Library/AcpiTimerLib/BaseRomAcpiTimerLib.inf
  QemuFwCfgLib|OvmfPkg/Library/QemuFwCfgLib/QemuFwCfgSecLib.inf
  !if $(DEBUG_ON_SERIAL_PORT)
    DebugLib|MdePkg/Library/BaseDebugLibSerialPort/BaseDebugLibSerialPort.inf
  !else
    DebugLib|OvmfPkg/Library/PlatformDebugLibIoPort/PlatformRomDebugLibIoPort.inf
  !endif
