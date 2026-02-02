package org.tianocore.edk2idea.Dsc;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Dsc.DscTypes;
import com.intellij.psi.TokenType;

%%

%class DscLexer
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
%state EOF

%%

<YYINITIAL> {
    // Section Headers
    \[Defines(\.[a-zA-Z0-9_]+)?\]         { return DscTypes.DEFINES_SECTION_HEADER; }
    \[Packages(\.[a-zA-Z0-9_]+)?\]        { return DscTypes.PACKAGES_SECTION_HEADER; }
    \[LibraryClasses(\.[a-zA-Z0-9_\., ]+)?\]  { return DscTypes.LIBRARY_CLASSES_SECTION_HEADER; }
    \[Pcds[a-zA-Z0-9_]*(\.[a-zA-Z0-9_\., ]+)?\]  { return DscTypes.PCD_SECTION_HEADER; }
    \[BuildOptions(\.[a-zA-Z0-9_\., ]+)?\]    { return DscTypes.BUILD_OPTIONS_SECTION_HEADER; }
    \[SkuIds(\.[a-zA-Z0-9_]+)?\]          { return DscTypes.SKU_IDS_SECTION_HEADER; }
    \[Components(\.[a-zA-Z0-9_\., ]+)?\]      { return DscTypes.COMPONENTS_SECTION_HEADER; }
    \[DefaultStores(\.[a-zA-Z0-9_]+)?\]   { return DscTypes.DEFAULT_STORES_SECTION_HEADER; }
    \[Strings(\.[a-zA-Z0-9_]+)?\]         { return DscTypes.STRINGS_SECTION_HEADER; }
    
    // Directives
    "!include"                       { return DscTypes.INCLUDE; }
    "!ifdef"                         { return DscTypes.IFDEF; }
    "!ifndef"                        { return DscTypes.IFNDEF; }
    "!if"                            { return DscTypes.IF; }
    "!elseif"                        { return DscTypes.ELSEIF; }
    "!else"                          { return DscTypes.ELSE; }
    "!endif"                         { return DscTypes.ENDIF; }
    "!error"                         { return DscTypes.ERROR; }

    // Keywords (Generic)
    "TRUE"                           { return DscTypes.IDENTIFIER; }
    "FALSE"                          { return DscTypes.IDENTIFIER; }

    // Symbols
    "="                              { return DscTypes.EQ; }
    "|"                              { return DscTypes.PIPE; }
    "."                              { return DscTypes.DOT; }
    ","                              { return DscTypes.COMMA; }
    ":"                              { return DscTypes.COLON; }
    "{"                              { return DscTypes.LBRACE; }
    "}"                              { return DscTypes.RBRACE; }
    "["                              { return DscTypes.LBRACKET; }
    "]"                              { return DscTypes.RBRACKET; }
    "<"                              { return DscTypes.LT; }
    ">"                              { return DscTypes.GT; }
    "*"                              { return DscTypes.STAR; }
    "*"                              { return DscTypes.STAR; }
    "-"                              { return DscTypes.IDENTIFIER; } 
    "("                              { return DscTypes.LPAREN; }
    ")"                              { return DscTypes.RPAREN; }
    "=="                             { return DscTypes.EQ_EQ; }
    "!="                             { return DscTypes.NE; }
    "<="                             { return DscTypes.LE; }
    ">="                             { return DscTypes.GE; }
    "&&"                             { return DscTypes.AND; }
    "||"                             { return DscTypes.OR; }

    // Values
    {GUID}                           { return DscTypes.GUID; }
    {HEX_NUMBER}                     { return DscTypes.HEX_NUMBER; }
    {NUMBER}                         { return DscTypes.NUMBER; }
    {STRING}                         { return DscTypes.STRING; }
    // Identifiers (Specific) - Must be before PATH_STRING fallback if using generic match
    {MACRO_REF}                      { return DscTypes.MACRO_REF; }
    {IDENTIFIER}                     { return DscTypes.IDENTIFIER; }

    {PATH_STRING}                    { return DscTypes.PATH_STRING; }
    
    {CRLF}                           { return DscTypes.CRLF; }
    {WHITE_SPACE}                    { return TokenType.WHITE_SPACE; }
    {HASH_COMMENT}                   { return DscTypes.COMMENT; }
    
    <<EOF>>                          { yybegin(EOF); return DscTypes.CRLF; }
}

[^]                                  { return TokenType.BAD_CHARACTER; }
