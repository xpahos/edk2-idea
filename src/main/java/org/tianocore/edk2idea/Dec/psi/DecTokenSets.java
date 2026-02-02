package org.tianocore.edk2idea.Dec.psi;

import com.intellij.psi.tree.TokenSet;

public interface DecTokenSets {
    TokenSet SECTION_HEADERS = TokenSet.create(DecTypes.LBRACKET, DecTypes.RBRACKET, DecTypes.SECTION_NAME);
    TokenSet VALUES = TokenSet.create(DecTypes.HEX_VAL, DecTypes.INT_VAL, DecTypes.BOOL_VAL, DecTypes.STRING);
    TokenSet KEYWORDS = TokenSet.create(DecTypes.BOOL_VAL);
}
