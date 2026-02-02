package org.tianocore.edk2idea.Dsc.psi;

import com.intellij.psi.tree.TokenSet;
import org.tianocore.edk2idea.Dsc.DscTypes;

public interface DscTokenSets {

        TokenSet COMMENTS = TokenSet.create(DscTypes.COMMENT);

        TokenSet SECTION_HEADERS = TokenSet.create(
                        DscTypes.BUILD_OPTIONS_SECTION_HEADER,
                        DscTypes.COMPONENTS_SECTION_HEADER,
                        DscTypes.DEFAULT_STORES_SECTION_HEADER,
                        DscTypes.DEFINES_SECTION_HEADER,
                        DscTypes.LIBRARY_CLASSES_SECTION_HEADER,
                        DscTypes.PACKAGES_SECTION_HEADER,
                        DscTypes.PCD_SECTION_HEADER,
                        DscTypes.SKU_IDS_SECTION_HEADER,
                        DscTypes.STRINGS_SECTION_HEADER,
                        DscTypes.UNKNOWN_SECTION_HEADER);

        TokenSet DEFINES_TAGS = TokenSet.EMPTY;
}
