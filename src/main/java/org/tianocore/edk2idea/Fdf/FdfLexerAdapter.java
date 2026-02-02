package org.tianocore.edk2idea.Fdf;

import com.intellij.lexer.FlexAdapter;

public class FdfLexerAdapter extends FlexAdapter {

    public FdfLexerAdapter() {
        super(new FdfLexer(null));
    }

}
