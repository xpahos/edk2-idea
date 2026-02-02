package org.tianocore.edk2idea.Uni.psi;

import com.intellij.psi.tree.TokenSet;
import org.tianocore.edk2idea.Uni.psi.UniTypes;

public interface UniTokenSets {
    TokenSet COMMENTS = TokenSet.create(UniTypes.COMMENT);
    TokenSet KEYWORDS = TokenSet.create(UniTypes.STRING_KEYWORD, UniTypes.LANGUAGE_KEYWORD);
    TokenSet STRINGS = TokenSet.create(UniTypes.STRING_LITERAL);
}
