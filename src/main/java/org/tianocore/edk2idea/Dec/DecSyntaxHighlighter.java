package org.tianocore.edk2idea.Dec;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Dec.psi.DecTokenSets;
import org.tianocore.edk2idea.Dec.psi.DecTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class DecSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey COMMENT = createTextAttributesKey("DEC_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey SECTION_HEADER = createTextAttributesKey("DEC_SECTION_HEADER",
            DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey VALUE = createTextAttributesKey("DEC_VALUE",
            DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey KEY = createTextAttributesKey("DEC_KEY",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("DEC_BAD_CHARACTER",
            DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
    public static final TextAttributesKey PATH_STRING = createTextAttributesKey("DEC_PATH_STRING",
            DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey PCD_NAME = createTextAttributesKey("DEC_PCD_NAME",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[] { COMMENT };
    private static final TextAttributesKey[] SECTION_HEADER_KEYS = new TextAttributesKey[] { SECTION_HEADER };
    private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[] { VALUE };
    private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[] { KEY };
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[] { BAD_CHARACTER };
    private static final TextAttributesKey[] PATH_STRING_KEYS = new TextAttributesKey[] { PATH_STRING };
    private static final TextAttributesKey[] PCD_NAME_KEYS = new TextAttributesKey[] { PCD_NAME };
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new DecLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(DecTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (DecTokenSets.SECTION_HEADERS.contains(tokenType)) {
            return SECTION_HEADER_KEYS;
        }
        if (DecTokenSets.VALUES.contains(tokenType)) {
            return VALUE_KEYS;
        }
        if (tokenType.equals(DecTypes.WORD)) {
            return KEY_KEYS;
        }
        if (tokenType.equals(DecTypes.PATH_STRING)) {
            return PATH_STRING_KEYS;
        }
        if (tokenType.equals(DecTypes.PCD_NAME_TOKEN)) {
            return PCD_NAME_KEYS;
        }
        if (tokenType.equals(DecTypes.PCD_SECTION_HEADER)) {
            return SECTION_HEADER_KEYS;
        }
        return EMPTY_KEYS;
    }
}
