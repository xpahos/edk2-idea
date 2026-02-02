package org.tianocore.edk2idea.Dsc;

import com.intellij.lexer.FlexAdapter;

public class DscLexerAdapter extends FlexAdapter {

    public DscLexerAdapter() {
        super(new DscLexer(null));
    }

}
