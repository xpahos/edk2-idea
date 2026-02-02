package org.tianocore.edk2idea.Uni;

import com.intellij.lexer.FlexAdapter;

public class UniLexerAdapter extends FlexAdapter {
    public UniLexerAdapter() {
        super(new UniLexer(null));
    }
}
