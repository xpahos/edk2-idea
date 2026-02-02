package org.tianocore.edk2idea.Dec;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Dec.psi.DecTypes;
import com.intellij.psi.TokenType;

%%

%class DecLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[ \t\f]
FIRST_VALUE_CHARACTER=[^ \n\r\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#")[^\r\n]*
KEY_CHARACTER=[^:= \n\r\t\f\\] | "\\ "

%state WAITING_VALUE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { return DecTypes.COMMENT; }

<YYINITIAL> {
  "["                                                       { return DecTypes.LBRACKET; }
  "]"                                                       { return DecTypes.RBRACKET; }
  "("                                                       { return DecTypes.LPAREN; }
  ")"                                                       { return DecTypes.RPAREN; }
  "{"                                                       { return DecTypes.LBRACE; }
  "}"                                                       { return DecTypes.RBRACE; }
  "."                                                       { return DecTypes.DOT; }
  ","                                                       { return DecTypes.COMMA; }
  "="                                                       { return DecTypes.EQ; }
  "|"                                                       { return DecTypes.PIPE; }

  "TRUE" | "FALSE" | "true" | "false"                       { return DecTypes.BOOL_VAL; }
  0[xX][0-9a-fA-F]+                                         { return DecTypes.HEX_VAL; }
  [0-9]+                                                    { return DecTypes.INT_VAL; }
  \"[^\"]*\"                                                { return DecTypes.STRING; }

  [a-zA-Z0-9_]+                                             { return DecTypes.WORD; }
  
  {CRLF}+                                                   { return TokenType.WHITE_SPACE; }
  {WHITE_SPACE}+                                            { return TokenType.WHITE_SPACE; }
}

[^]                                                         { return TokenType.BAD_CHARACTER; }
