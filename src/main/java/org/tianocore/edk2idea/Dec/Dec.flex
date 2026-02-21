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
FIRST_VALUE_CHARACTER=[^ \n\r\f\\] | "\\"{CRLF} | "\\"
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\"
END_OF_LINE_COMMENT=("#")[^\r\n]*
KEY_CHARACTER=[^:= \n\r\t\f\\] | "\\ "

%state WAITING_VALUE
%state PCD_SECTION

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { return DecTypes.COMMENT; }

<YYINITIAL> {
  \[Pcds[^\]\r\n]*\]                                         { yybegin(PCD_SECTION); return DecTypes.PCD_SECTION_HEADER; }

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

  "/"                                                       { return DecTypes.SLASH; }
  "\\"                                                      { return DecTypes.BACKSLASH; }
  "<"                                                       { return DecTypes.LT; }
  ">"                                                       { return DecTypes.GT; }
  "-"                                                       { return DecTypes.MINUS; }

  "TRUE" | "FALSE" | "true" | "false"                       { return DecTypes.BOOL_VAL; }
  0[xX][0-9a-fA-F]+                                         { return DecTypes.HEX_VAL; }
  [0-9]+                                                    { return DecTypes.INT_VAL; }
  \"[^\"]*\"                                                { return DecTypes.STRING; }

  [a-zA-Z0-9_]+                                             { return DecTypes.WORD; }
  
  {CRLF}+                                                   { return DecTypes.CRLF; }
  {WHITE_SPACE}+                                            { return TokenType.WHITE_SPACE; }

  \$\([a-zA-Z0-9_\.]+\)                                     { return DecTypes.MACRO_REF; }
}

<PCD_SECTION> {
  "["                                                       { yypushback(1); yybegin(YYINITIAL); }

  {END_OF_LINE_COMMENT}                                     { return DecTypes.COMMENT; }

  "|"                                                       { return DecTypes.PIPE; }
  "{"                                                       { return DecTypes.LBRACE; }
  "}"                                                       { return DecTypes.RBRACE; }
  "("                                                       { return DecTypes.LPAREN; }
  ")"                                                       { return DecTypes.RPAREN; }
  "."                                                       { return DecTypes.DOT; }
  ","                                                       { return DecTypes.COMMA; }
  "="                                                       { return DecTypes.EQ; }
  "/"                                                       { return DecTypes.SLASH; }
  "\\"                                                      { return DecTypes.BACKSLASH; }
  "<"                                                       { return DecTypes.LT; }
  ">"                                                       { return DecTypes.GT; }
  "-"                                                       { return DecTypes.MINUS; }

  "TRUE" | "FALSE" | "true" | "false"                       { return DecTypes.BOOL_VAL; }
  0[xX][0-9a-fA-F]+                                         { return DecTypes.HEX_VAL; }
  [0-9]+                                                    { return DecTypes.INT_VAL; }
  \"[^\"]*\"                                                { return DecTypes.STRING; }

  [a-zA-Z0-9_]+(\.[a-zA-Z0-9_]+)+                           { return DecTypes.PCD_NAME_TOKEN; }
  [a-zA-Z0-9_]+                                             { return DecTypes.WORD; }

  {CRLF}+                                                   { return DecTypes.CRLF; }
  {WHITE_SPACE}+                                            { return TokenType.WHITE_SPACE; }

  \$\([a-zA-Z0-9_\.]+\)                                     { return DecTypes.MACRO_REF; }

  <<EOF>>                                                   { yybegin(YYINITIAL); return DecTypes.CRLF; }
}

[^]                                                         { return TokenType.BAD_CHARACTER; }
