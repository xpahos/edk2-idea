package org.tianocore.edk2idea.Inf.psi;

import com.intellij.psi.tree.TokenSet;
import org.tianocore.edk2idea.Inf.InfTypes;

public interface InfTokenSets {

    TokenSet COMMENTS = TokenSet.create(InfTypes.COMMENT);

    TokenSet SECTION_HEADERS = TokenSet.create(
            InfTypes.DEFINES_SECTION_HEADER,
            InfTypes.SOURCES_SECTION_HEADER,
            InfTypes.PACKAGES_SECTION_HEADER,
            InfTypes.LIBRARY_CLASSES_SECTION_HEADER,
            InfTypes.PROTOCOLS_SECTION_HEADER,
            InfTypes.GUIDS_SECTION_HEADER,
            InfTypes.PPIS_SECTION_HEADER,
            InfTypes.PCD_SECTION_HEADER,
            InfTypes.DEPEX_SECTION_HEADER,
            InfTypes.BINARIES_SECTION_HEADER,
            InfTypes.BUILD_OPTIONS_SECTION_HEADER
    );

    TokenSet DEFINES_TAGS = TokenSet.create(
            InfTypes.INF_VERSION,
            InfTypes.BASE_NAME,
            InfTypes.FILE_GUID,
            InfTypes.EDK_RELEASE_VERSION,
            InfTypes.PI_SPECIFICATION_VERSION,
            InfTypes.UEFI_SPECIFICATION_VERSION,
            InfTypes.MODULE_TYPE,
            InfTypes.BUILD_NUMBER,
            InfTypes.VERSION_STRING,
            InfTypes.MODULE_UNI_FILE,
            InfTypes.LIBRARY_CLASS,
            InfTypes.PCD_IS_DRIVER,
            InfTypes.ENTRY_POINT,
            InfTypes.UNLOAD_IMAGE,
            InfTypes.CONSTRUCTOR,
            InfTypes.DESTRUCTOR,
            InfTypes.SHADOW,
            InfTypes.PCI_DEVICE_ID,
            InfTypes.PCI_VENDOR_ID,
            InfTypes.PCI_CLASS_CODE,
            InfTypes.PCI_COMPRESS,
            InfTypes.UEFI_HII_RESOURCE_SECTION,
            InfTypes.DEFINE,
            InfTypes.SPEC,
            InfTypes.CUSTOM_MAKEFILE,
            InfTypes.DPX_SOURCE
    );
}
