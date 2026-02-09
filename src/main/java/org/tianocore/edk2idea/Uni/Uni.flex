package org.tianocore.edk2idea.Uni;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Uni.psi.UniTypes;
import com.intellij.psi.TokenType;

%%

%class UniLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
END_OF_LINE_COMMENT=("//")[^\r\n]*
STRING_KEYWORD="#string"
LANGDEF_KEYWORD="#langdef"
LANGUAGE_KEYWORD="#language"
STRING_LITERAL=\"([^\r\n]*)\"
WORD=[a-zA-Z0-9_.\-]+

%state WAITING_VALUE

%%

<YYINITIAL> {
  {END_OF_LINE_COMMENT}                           { return UniTypes.COMMENT; }
  "/"                                             { return UniTypes.SLASH; }
  "="                                             { return UniTypes.EQUALS; }
  "#"                                             { return UniTypes.SHARP; }
  {STRING_KEYWORD}                                { return UniTypes.STRING_KEYWORD; }
  {LANGDEF_KEYWORD}                               { return UniTypes.LANGDEF_KEYWORD; }
  {LANGUAGE_KEYWORD}                              { return UniTypes.LANGUAGE_KEYWORD; }
  {STRING_LITERAL}                                { return UniTypes.STRING_LITERAL; }
  {WORD}                                          { return UniTypes.WORD; }
  {CRLF}+                                         { return TokenType.WHITE_SPACE; }
  {WHITE_SPACE}+                                  { return TokenType.WHITE_SPACE; }
  [^]                                             { return TokenType.BAD_CHARACTER; }
}
