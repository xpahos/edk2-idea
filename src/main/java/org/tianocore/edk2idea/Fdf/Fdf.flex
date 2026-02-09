package org.tianocore.edk2idea.Fdf;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Fdf.FdfTypes;
import com.intellij.psi.TokenType;

%%

%class FdfLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

CRLF = \r?\n
WHITE_SPACE = [ \t\f]+
HASH_COMMENT = #[^\r\n]*

// Identifiers and Strings
IDENTIFIER = [a-zA-Z0-9_\-\.]+
STRING = \"([^\"\r\n]*)\"
PATH_STRING = [a-zA-Z0-9_\-\.\/\\\$]+
MACRO_REF = \$\([a-zA-Z0-9_\.]+\)

// Numbers
HEX_NUMBER = 0[xX][0-9a-fA-F]+
NUMBER = [0-9]+
GUID = [0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}

%state WAITING_VALUE
%state WAITING_INCLUDE_PATH
%state EOF

%%

<YYINITIAL> {
    // Section Headers
    \[Defines(\.[a-zA-Z0-9_]+)?\]                { return FdfTypes.DEFINES_SECTION_HEADER; }
    \[FD\.[a-zA-Z0-9_\.]+\]                       { return FdfTypes.FD_SECTION_HEADER; }
    \[FV\.[a-zA-Z0-9_\.]+\]                       { return FdfTypes.FV_SECTION_HEADER; }
    \[Rule\.[a-zA-Z0-9_\.]+\]                     { return FdfTypes.RULE_SECTION_HEADER; }
    \[Capsule\.[a-zA-Z0-9_\.]+\]                  { return FdfTypes.CAPSULE_SECTION_HEADER; }
    \[OptionRom\.[a-zA-Z0-9_\.]+\]                { return FdfTypes.OPTION_ROM_SECTION_HEADER; }
    
    // Directives
    "!include"                       { yybegin(WAITING_INCLUDE_PATH); return FdfTypes.INCLUDE; }
    "!ifdef"                         { return FdfTypes.IFDEF; }
    "!ifndef"                        { return FdfTypes.IFNDEF; }
    "!if"                            { return FdfTypes.IF; }
    "!elseif"                        { return FdfTypes.ELSEIF; }
    "!else"                          { return FdfTypes.ELSE; }
    "!endif"                         { return FdfTypes.ENDIF; }
    "!error"                         { return FdfTypes.ERROR; }

    // Keywords (FDF Specific)
    "APRIORI"                        { return FdfTypes.APRIORI; }
    "INF"                            { return FdfTypes.INF; }
    "FILE"                           { return FdfTypes.FILE; }
    "SECTION"                        { return FdfTypes.SECTION; }
    "SET"                            { return FdfTypes.SET; }

    // Symbols
    "="                              { return FdfTypes.EQ; }
    "|"                              { return FdfTypes.PIPE; }
    "."                              { return FdfTypes.DOT; }
    ","                              { return FdfTypes.COMMA; }
    ":"                              { return FdfTypes.COLON; }
    "{"                              { return FdfTypes.LBRACE; }
    "}"                              { return FdfTypes.RBRACE; }
    "["                              { return FdfTypes.LBRACKET; }
    "]"                              { return FdfTypes.RBRACKET; }
    "<"                              { return FdfTypes.LT; }
    ">"                              { return FdfTypes.GT; }
    "*"                              { return FdfTypes.STAR; }
    "("                              { return FdfTypes.LPAREN; }
    ")"                              { return FdfTypes.RPAREN; }
    "=="                             { return FdfTypes.EQ_EQ; }
    "!="                             { return FdfTypes.NE; }
    "<="                             { return FdfTypes.LE; }
    ">="                             { return FdfTypes.GE; }
    "+"                              { return FdfTypes.PLUS; }
    "-"                              { return FdfTypes.MINUS; }
    "/"                              { return FdfTypes.DIV; }
    "%"                              { return FdfTypes.MOD; }
    "&"                              { return FdfTypes.BIT_AND; }
    "^"                              { return FdfTypes.XOR; }
    "~"                              { return FdfTypes.BIT_NOT; }
    "&&"                             { return FdfTypes.AND_OP; }
    "||"                             { return FdfTypes.OR_OP; }

    // Values
    {GUID}                           { return FdfTypes.GUID; }
    {HEX_NUMBER}                     { return FdfTypes.HEX_NUMBER; }
    {NUMBER}                         { return FdfTypes.NUMBER; }
    {STRING}                         { return FdfTypes.STRING; }
    
    {MACRO_REF}                      { return FdfTypes.MACRO_REF; }
    {IDENTIFIER}                     { return FdfTypes.IDENTIFIER; }

    {PATH_STRING}                    { return FdfTypes.PATH_STRING; }
    
    {CRLF}                           { return FdfTypes.CRLF; }
    {WHITE_SPACE}                    { return TokenType.WHITE_SPACE; }
    {HASH_COMMENT}                   { return FdfTypes.COMMENT; }
    
    <<EOF>>                          { yybegin(EOF); return FdfTypes.CRLF; }
}

<WAITING_INCLUDE_PATH> {
    {WHITE_SPACE}                    { return TokenType.WHITE_SPACE; }
    {PATH_STRING}                    { yybegin(YYINITIAL); return FdfTypes.PATH_STRING; }
    {CRLF}                           { yybegin(YYINITIAL); return FdfTypes.CRLF; }
    <<EOF>>                          { yybegin(EOF); return FdfTypes.CRLF; }
}

[^]                                  { return TokenType.BAD_CHARACTER; }
