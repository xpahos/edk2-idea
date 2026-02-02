package org.tianocore.edk2idea.Uni;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.tianocore.edk2idea.Uni.psi.UniFile;
import org.tianocore.edk2idea.Uni.psi.UniTokenSets;
import org.tianocore.edk2idea.Uni.psi.UniTypes;
import org.jetbrains.annotations.NotNull;

public class UniParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(UniLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new UniLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(TokenType.WHITE_SPACE);
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return UniTokenSets.COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return new UniParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new UniFile(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return UniTypes.Factory.createElement(node);
    }
}
