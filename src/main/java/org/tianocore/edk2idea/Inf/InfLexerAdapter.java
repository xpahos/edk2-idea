package org.tianocore.edk2idea.Inf;

import com.intellij.lexer.FlexAdapter;

public class InfLexerAdapter extends FlexAdapter {
    public InfLexerAdapter() {
        super(new InfLexer(null));
    }
}
