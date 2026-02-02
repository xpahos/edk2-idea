package org.tianocore.edk2idea.Fdf;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class FdfSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey COMMENT = createTextAttributesKey("FDF_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey SECTION_HEADER = createTextAttributesKey("FDF_SECTION_HEADER",
            DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey KEYWORD = createTextAttributesKey("FDF_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey PROPERTY_KEY = createTextAttributesKey("FDF_PROPERTY_KEY",
            DefaultLanguageHighlighterColors.LABEL);
    public static final TextAttributesKey PROPERTY_VALUE = createTextAttributesKey("FDF_PROPERTY_VALUE",
            DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey COMMAND = createTextAttributesKey("FDF_COMMAND",
            DefaultLanguageHighlighterColors.INLINE_SUGGESTION);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[] { COMMENT };
    private static final TextAttributesKey[] SECTION_HEADER_KEYS = new TextAttributesKey[] { SECTION_HEADER };
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[] { KEYWORD };
    private static final TextAttributesKey[] COMMAND_KEYS = new TextAttributesKey[] { COMMAND };
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FdfLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(FdfTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (tokenType.equals(FdfTypes.DEFINES_SECTION_HEADER) ||
                tokenType.equals(FdfTypes.FD_SECTION_HEADER) ||
                tokenType.equals(FdfTypes.FV_SECTION_HEADER) ||
                tokenType.equals(FdfTypes.RULE_SECTION_HEADER) ||
                tokenType.equals(FdfTypes.CAPSULE_SECTION_HEADER) ||
                tokenType.equals(FdfTypes.OPTION_ROM_SECTION_HEADER)) {
            return SECTION_HEADER_KEYS;
        }
        if (tokenType.equals(FdfTypes.APRIORI) ||
                tokenType.equals(FdfTypes.INF) ||
                tokenType.equals(FdfTypes.FILE) ||
                tokenType.equals(FdfTypes.SECTION)) {
            return KEYWORD_KEYS;
        }
        if (tokenType.equals(FdfTypes.INCLUDE) ||
                tokenType.equals(FdfTypes.IF) ||
                tokenType.equals(FdfTypes.IFDEF) ||
                tokenType.equals(FdfTypes.IFNDEF) ||
                tokenType.equals(FdfTypes.ELSEIF) ||
                tokenType.equals(FdfTypes.ELSE) ||
                tokenType.equals(FdfTypes.ENDIF) ||
                tokenType.equals(FdfTypes.ERROR)) {
            return COMMAND_KEYS;
        }
        return EMPTY_KEYS;
    }
}
