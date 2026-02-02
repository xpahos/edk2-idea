package org.tianocore.edk2idea.Dec;

import com.intellij.lexer.FlexAdapter;

public class DecLexerAdapter extends FlexAdapter {
    public DecLexerAdapter() {
        super(new DecLexer(null));
    }
}
