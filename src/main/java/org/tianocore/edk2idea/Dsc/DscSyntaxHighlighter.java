package org.tianocore.edk2idea.Dsc;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static org.tianocore.edk2idea.Dsc.psi.DscTokenSets.DEFINES_TAGS;
import static org.tianocore.edk2idea.Dsc.psi.DscTokenSets.SECTION_HEADERS;

public class DscSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey COMMENT = createTextAttributesKey("DSC_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey SECTION_HEADER = createTextAttributesKey("DSC_SECTION_HEADER",
            DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey DEFINES_KEYS = createTextAttributesKey("DSC_DEFINES_KEYS",
            DefaultLanguageHighlighterColors.LABEL);
    public static final TextAttributesKey DEFINES_VALUES = createTextAttributesKey("DSC_DEFINES_VALUE",
            DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey COMMAND = createTextAttributesKey("DSC_COMMAND",
            DefaultLanguageHighlighterColors.INLINE_SUGGESTION);
    public static final TextAttributesKey PATH_STRING = createTextAttributesKey("DSC_PATH_STRING",
            DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey PCD_NAME = createTextAttributesKey("DSC_PCD_NAME",
            DefaultLanguageHighlighterColors.LOCAL_VARIABLE);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[] { COMMENT };
    private static final TextAttributesKey[] SECTION_HEADER_KEYS = new TextAttributesKey[] { SECTION_HEADER };
    private static final TextAttributesKey[] DEFINES_TAGS_KEYS = new TextAttributesKey[] { DEFINES_KEYS };
    private static final TextAttributesKey[] DEFINES_VALUE_KEYS = new TextAttributesKey[] { DEFINES_VALUES };
    private static final TextAttributesKey[] COMMAND_KEYS = new TextAttributesKey[] { COMMAND };
    private static final TextAttributesKey[] PATH_STRING_KEYS = new TextAttributesKey[] { PATH_STRING };
    private static final TextAttributesKey[] PCD_NAME_KEYS = new TextAttributesKey[] { PCD_NAME };
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new DscLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(DscTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (SECTION_HEADERS.contains(tokenType)) {
            return SECTION_HEADER_KEYS;
        }
        if (DEFINES_TAGS.contains(tokenType)) {
            return DEFINES_TAGS_KEYS;
        }
        if (tokenType.equals(DscTypes.INCLUDE) ||
                tokenType.equals(DscTypes.IF) ||
                tokenType.equals(DscTypes.IFDEF) ||
                tokenType.equals(DscTypes.IFNDEF) ||
                tokenType.equals(DscTypes.ELSEIF) ||
                tokenType.equals(DscTypes.ELSE) ||
                tokenType.equals(DscTypes.ENDIF) ||
                tokenType.equals(DscTypes.ERROR)) {
            return COMMAND_KEYS;
        }
        if (tokenType.equals(DscTypes.PATH_STRING)) {
            return PATH_STRING_KEYS;
        }
        if (tokenType.equals(DscTypes.PCD_NAME_TOKEN)) {
            return PCD_NAME_KEYS;
        }
        return EMPTY_KEYS;
    }
}
