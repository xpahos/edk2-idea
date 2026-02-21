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
// COMMAND_FLAG: / followed by 1-4 uppercase letters (e.g., /D, /I, /O, /Od, /W3)
// This ensures it doesn't match longer file paths like /BaseLib.inf
COMMAND_FLAG = \/[A-Z][A-Z]{0,3}
MACRO_REF = \$\([a-zA-Z0-9_\.]+\)

// Numbers
HEX_NUMBER = 0[xX][0-9a-fA-F]+
NUMBER = [0-9]+
GUID = [0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}

%state WAITING_VALUE
%state WAITING_INCLUDE_PATH
%state PCD_SECTION
%state EOF

%%

<YYINITIAL> {
    // Section Headers
    \[Defines(\.[a-zA-Z0-9_]+)?\]         { return DscTypes.DEFINES_SECTION_HEADER; }
    \[Packages(\.[a-zA-Z0-9_\$\(\)]+)?\]        { return DscTypes.PACKAGES_SECTION_HEADER; }
    \[LibraryClasses(\.[a-zA-Z0-9_\., \$\(\)]+)?\]  { return DscTypes.LIBRARY_CLASSES_SECTION_HEADER; }
    \[Pcds[a-zA-Z0-9_]*(\.[a-zA-Z0-9_\., \$\(\)]+)?\]  { yybegin(PCD_SECTION); return DscTypes.PCD_SECTION_HEADER; }
    \[BuildOptions(\.[a-zA-Z0-9_\., \$\(\)]+)?\]    { return DscTypes.BUILD_OPTIONS_SECTION_HEADER; }
    \[SkuIds(\.[a-zA-Z0-9_\$\(\)]+)?\]          { return DscTypes.SKU_IDS_SECTION_HEADER; }
    \[Components(\.[a-zA-Z0-9_\., \$\(\)]+)?\]      { return DscTypes.COMPONENTS_SECTION_HEADER; }
    \[DefaultStores(\.[a-zA-Z0-9_\$\(\)]+)?\]   { return DscTypes.DEFAULT_STORES_SECTION_HEADER; }
    \[Strings(\.[a-zA-Z0-9_\$\(\)]+)?\]         { return DscTypes.STRINGS_SECTION_HEADER; }
    
    // Directives
    "!include"                       { yybegin(WAITING_INCLUDE_PATH); return DscTypes.INCLUDE; }
    "!ifdef"                         { return DscTypes.IFDEF; }
    "!ifndef"                        { return DscTypes.IFNDEF; }
    "!if"                            { return DscTypes.IF; }
    "!elseif"                        { return DscTypes.ELSEIF; }
    "!else"                          { return DscTypes.ELSE; }
    "!endif"                         { return DscTypes.ENDIF; }
    "!error"                         { return DscTypes.ERROR; }
    
    "FLASH_DEFINITION"               { return DscTypes.FLASH_DEFINITION; }
    "EXEC"                           { return DscTypes.EXEC; }
    "Defines"                        { return DscTypes.DEFINES; }

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
     "+"                              { return DscTypes.PLUS; }

     // Values - IMPORTANT: Order matters for conflicting patterns
     {GUID}                           { return DscTypes.GUID; }
     {HEX_NUMBER}                     { return DscTypes.HEX_NUMBER; }
     {NUMBER}                         { return DscTypes.NUMBER; }
     {STRING}                         { return DscTypes.STRING; }
     
     // COMMAND_FLAG must come before PATH_STRING to match /D, /I, etc.
     // Pattern: / followed by uppercase letter (e.g., /D, /I, /O, /Fd, /Od)
     {COMMAND_FLAG}                   { return DscTypes.COMMAND_FLAG; }
     
     // Macro references must come before IDENTIFIER
     {MACRO_REF}                      { return DscTypes.MACRO_REF; }
     {IDENTIFIER}                     { return DscTypes.IDENTIFIER; }

     {PATH_STRING}                    { return DscTypes.PATH_STRING; }
    
    {CRLF}                           { return DscTypes.CRLF; }
    {WHITE_SPACE}                    { return TokenType.WHITE_SPACE; }
    {HASH_COMMENT}                   { return DscTypes.COMMENT; }
    
    <<EOF>>                          { yybegin(EOF); return DscTypes.CRLF; }
}

<PCD_SECTION> {
    "[" { yypushback(1); yybegin(YYINITIAL); }

    {HASH_COMMENT}                   { return DscTypes.COMMENT; }

    // Directives inside PCD sections
    "!include"                       { yybegin(WAITING_INCLUDE_PATH); return DscTypes.INCLUDE; }
    "!ifdef"                         { return DscTypes.IFDEF; }
    "!ifndef"                        { return DscTypes.IFNDEF; }
    "!if"                            { return DscTypes.IF; }
    "!elseif"                        { return DscTypes.ELSEIF; }
    "!else"                          { return DscTypes.ELSE; }
    "!endif"                         { return DscTypes.ENDIF; }
    "!error"                         { return DscTypes.ERROR; }

    // Symbols
    "|"                              { return DscTypes.PIPE; }
    "."                              { return DscTypes.DOT; }
    ","                              { return DscTypes.COMMA; }
    ":"                              { return DscTypes.COLON; }
    "{"                              { return DscTypes.LBRACE; }
    "}"                              { return DscTypes.RBRACE; }
    "<"                              { return DscTypes.LT; }
    ">"                              { return DscTypes.GT; }
    "="                              { return DscTypes.EQ; }
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
    "+"                              { return DscTypes.PLUS; }

    // Values
    "TRUE"                           { return DscTypes.IDENTIFIER; }
    "FALSE"                          { return DscTypes.IDENTIFIER; }
    {GUID}                           { return DscTypes.GUID; }
    {HEX_NUMBER}                     { return DscTypes.HEX_NUMBER; }
    {NUMBER}                         { return DscTypes.NUMBER; }
    {STRING}                         { return DscTypes.STRING; }
    {MACRO_REF}                      { return DscTypes.MACRO_REF; }

    // PCD name: identifier with dots (e.g., gEfiMdePkgTokenSpaceGuid.PcdFSBClock)
    [a-zA-Z_][a-zA-Z0-9_]*(\.[a-zA-Z_][a-zA-Z0-9_]*)+  { return DscTypes.PCD_NAME_TOKEN; }
    {IDENTIFIER}                     { return DscTypes.IDENTIFIER; }

    {PATH_STRING}                    { return DscTypes.PATH_STRING; }

    {CRLF}                           { return DscTypes.CRLF; }
    {WHITE_SPACE}                    { return TokenType.WHITE_SPACE; }

    <<EOF>>                          { yybegin(EOF); return DscTypes.CRLF; }
}

<WAITING_INCLUDE_PATH> {
    {WHITE_SPACE}                    { return TokenType.WHITE_SPACE; }
    {PATH_STRING}                    { yybegin(YYINITIAL); return DscTypes.PATH_STRING; }
    {CRLF}                           { yybegin(YYINITIAL); return DscTypes.CRLF; }
    <<EOF>>                          { yybegin(EOF); return DscTypes.CRLF; }
}

[^]                                  { return TokenType.BAD_CHARACTER; }
