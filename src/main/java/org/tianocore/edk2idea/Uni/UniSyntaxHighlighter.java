package org.tianocore.edk2idea.Uni;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Uni.psi.UniTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class UniSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey COMMENT = createTextAttributesKey("UNI_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey KEYWORD = createTextAttributesKey("UNI_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING = createTextAttributesKey("UNI_STRING",
            DefaultLanguageHighlighterColors.STRING);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[] { COMMENT };
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[] { KEYWORD };
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[] { STRING };
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new UniLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(UniTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (tokenType.equals(UniTypes.STRING_KEYWORD) || tokenType.equals(UniTypes.LANGUAGE_KEYWORD)) {
            return KEYWORD_KEYS;
        }
        if (tokenType.equals(UniTypes.STRING_LITERAL)) {
            return STRING_KEYS;
        }
        return EMPTY_KEYS;
    }
}
