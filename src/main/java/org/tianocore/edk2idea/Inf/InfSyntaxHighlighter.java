package org.tianocore.edk2idea.Inf;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static org.tianocore.edk2idea.Inf.psi.InfTokenSets.DEFINES_TAGS;
import static org.tianocore.edk2idea.Inf.psi.InfTokenSets.SECTION_HEADERS;

public class InfSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("INF_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey SECTION_HEADER =
            createTextAttributesKey("INF_SECTION_HEADER", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey DEFINES_KEYS =
            createTextAttributesKey("INF_DEFINES_KEYS", DefaultLanguageHighlighterColors.LABEL);
    public static final TextAttributesKey DEFINES_VALUES =
            createTextAttributesKey("INF_DEFINES_VALUE", DefaultLanguageHighlighterColors.IDENTIFIER);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] SECTION_HEADER_KEYS = new TextAttributesKey[]{SECTION_HEADER};
    private static final TextAttributesKey[] DEFINES_TAGS_KEYS = new TextAttributesKey[]{DEFINES_KEYS};
    private static final TextAttributesKey[] DEFINES_VALUE_KEYS = new TextAttributesKey[]{DEFINES_VALUES};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new InfLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(InfTypes.COMMENT)) {
            return COMMENT_KEYS;
        }
        if (SECTION_HEADERS.contains(tokenType)) {
            return SECTION_HEADER_KEYS;
        }
        if (DEFINES_TAGS.contains(tokenType)) {
            return DEFINES_TAGS_KEYS;
        }
        return EMPTY_KEYS;
    }
}
