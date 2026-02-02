package org.tianocore.edk2idea.Inf;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.tianocore.edk2idea.Inf.InfTypes;
import com.intellij.psi.TokenType;

%%

%class InfLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

%{

private java.util.Stack<Integer> stateStack = new java.util.Stack<>();

private void pushState(int newState) {
    stateStack.push(yystate());
    yybegin(newState);
}

private void popState() {
    if (!stateStack.isEmpty()) {
        yybegin(stateStack.pop());
    } else {
        yybegin(YYINITIAL);
    }
}

private void clearStack() {
    while (!stateStack.isEmpty()) stateStack.pop();
}

%}

// Character classes
CRLF = \r?\n
WHITE_SPACE = [ \t\f]+
ALPHA = [a-zA-Z_]
DIGIT = [0-9]
ALPHANUM = [a-zA-Z0-9_]
HEX_DIGIT = [0-9a-fA-F]

// Identifiers
IDENTIFIER = {ALPHA}{ALPHANUM}*
BASE_NAME_STRING = [a-zA-Z0-9][a-zA-Z0-9_\-\.]*
PATH_STRING = ({BASE_NAME_STRING}|[\/\\])+

// Numbers
HEX_NUMBER = 0[xX]{HEX_DIGIT}+
VERSION_NUMBER = {DIGIT}+(\.{DIGIT}+)?
DECIMAL_NUMBER = {DIGIT}+

// GUID format: 8-4-4-4-12 hex digits
GUID = {HEX_DIGIT}{8}-{HEX_DIGIT}{4}-{HEX_DIGIT}{4}-{HEX_DIGIT}{4}-{HEX_DIGIT}{12}

// Strings
STRING = \"([^\"\r\n]*)\"
SINGLE_QUOTED_STRING = '([^'\r\n]*)'

// Comments
HASH_COMMENT = #[^\r\n]*

%state EOF

%state DEFINES_SECTION
%state SOURCES_SECTION
%state DEPEX_SECTION

%state DEFINES_SECTION_WAITING_HEX_N_VERSION_NUMBER
%state DEFINES_SECTION_WAITING_HEX_NUMBER
%state DEFINES_SECTION_WAITING_BASE_NAME_STRING
%state DEFINES_SECTION_WAITING_GUID
%state DEFINES_SECTION_WAITING_MODULE_TYPE
%state DEFINES_SECTION_WAITING_HEX_NUMBER
%state DEFINES_SECTION_WAITING_DECIMAL_VERSION
%state DEFINES_SECTION_WAITING_DECIMAL_NUMBER
%state DEFINES_SECTION_WAITING_LIBRARY_CLASS
%state DEFINES_SECTION_WAITING_BOOLEAN
%state DEFINES_SECTION_WAITING_IDENTIFIER
%state DEFINES_SECTION_WAITING_PCD_DRIVER
%state DEFINES_SECTION_WAITING_PATH_STRING
%state DEFINES_SECTION_WAITING_MAKEFILE
%state DEFINES_SECTION_WAITING_EQ_IDENTIFIER
%state DEFINES_SECTION_WAITING_EQ_DECIMAL_VERSION
%state BUILD_OPTIONS_SECTION

%%

<YYINITIAL> {
    // Numbers
    {GUID}                           { return InfTypes.GUID; }
    {HEX_NUMBER}                     { return InfTypes.HEX_NUMBER; }
    {VERSION_NUMBER}                 { return InfTypes.VERSION_NUMBER; }
    {DECIMAL_NUMBER}                 { return InfTypes.NUMBER; }

    // Section Headers
    \[Defines(\.[a-zA-Z0-9]+)?\]         { pushState(DEFINES_SECTION); return InfTypes.DEFINES_SECTION_HEADER; }
    \[Sources(\.[a-zA-Z0-9]+)?\]         { pushState(SOURCES_SECTION); return InfTypes.SOURCES_SECTION_HEADER; }
    \[Packages(\.[a-zA-Z0-9]+)?\]        { return InfTypes.PACKAGES_SECTION_HEADER; }
    \[LibraryClasses(\.[a-zA-Z0-9]+)?\]  { return InfTypes.LIBRARY_CLASSES_SECTION_HEADER; }
    \[Protocols(\.[a-zA-Z0-9]+)?\]       { return InfTypes.PROTOCOLS_SECTION_HEADER; }
    \[Guids(\.[a-zA-Z0-9]+)?\]           { return InfTypes.GUIDS_SECTION_HEADER; }
    \[Ppis(\.[a-zA-Z0-9]+)?\]            { return InfTypes.PPIS_SECTION_HEADER; }
    \[Pcd(Ex)?(\.[a-zA-Z0-9]+)?\]        { return InfTypes.PCD_SECTION_HEADER; }
    \[FixedPcd\]                         { return InfTypes.PCD_SECTION_HEADER; }
    \[FeaturePcd\]                       { return InfTypes.PCD_SECTION_HEADER; }
    \[PatchPcd\]                         { return InfTypes.PCD_SECTION_HEADER; }
    \[Depex(\.[a-zA-Z0-9]+)?\]           { pushState(DEPEX_SECTION); return InfTypes.DEPEX_SECTION_HEADER; }
    \[Binaries(\.[a-zA-Z0-9]+)?\]        { return InfTypes.BINARIES_SECTION_HEADER; }
    \[UserExtensions[^\]]*\]             { return InfTypes.USER_EXTENSIONS_SECTION_HEADER; }
    \[BuildOptions(\.[a-zA-Z0-9]+)?\]    { pushState(BUILD_OPTIONS_SECTION); return InfTypes.BUILD_OPTIONS_SECTION_HEADER; }
    \[[^\]]+\]                           { return InfTypes.UNKNOWN_SECTION_HEADER; }


    // Architecture
    //"IA32"                           { return InfTypes.ARCH; }
    //"X64"                            { return InfTypes.ARCH; }
    //"EBC"                            { return InfTypes.ARCH; }
    //"ARM"                            { return InfTypes.ARCH; }
    //"AARCH64"                        { return InfTypes.ARCH; }
    //"RISCV64"                        { return InfTypes.ARCH; }

    // Usage keywords
    "PRODUCES"                       { return InfTypes.USAGE; }
    "CONSUMES"                       { return InfTypes.USAGE; }
    "NOTIFY"                         { return InfTypes.USAGE; }
    "SOMETIMES_PRODUCES"             { return InfTypes.USAGE; }
    "SOMETIMES_CONSUMES"             { return InfTypes.USAGE; }
    "PRIVATE"                        { return InfTypes.USAGE; }
    "UNDEFINED"                      { return InfTypes.USAGE; }

    // Binary types
    "PE32"                           { return InfTypes.BINARY_TYPE; }
    "DXE_DEPEX"                      { return InfTypes.BINARY_TYPE; }
    "UI"                             { return InfTypes.BINARY_TYPE; }
    "VERSION"                        { return InfTypes.BINARY_TYPE; }
    "PEI_DEPEX"                      { return InfTypes.BINARY_TYPE; }
    "TE"                             { return InfTypes.BINARY_TYPE; }
    "FV"                             { return InfTypes.BINARY_TYPE; }
    "RAW"                            { return InfTypes.BINARY_TYPE; }
    "ACPI"                           { return InfTypes.BINARY_TYPE; }
    "ASL"                            { return InfTypes.BINARY_TYPE; }

    // Strings
    {STRING}                         { return InfTypes.STRING; }
    {SINGLE_QUOTED_STRING}           { return InfTypes.STRING; }
    {IDENTIFIER}                     { return InfTypes.IDENTIFIER; }
    {BASE_NAME_STRING}               { return InfTypes.BASE_NAME_STRING; }
    {PATH_STRING}                    { return InfTypes.PATH_STRING; }

    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION> {
    "[" { yypushback(1); popState(); }
    // Special INF keywords (most specific first)
    "INF_VERSION"                    { pushState(DEFINES_SECTION_WAITING_HEX_N_VERSION_NUMBER); return InfTypes.INF_VERSION; }
    "BASE_NAME"                      { pushState(DEFINES_SECTION_WAITING_BASE_NAME_STRING); return InfTypes.BASE_NAME; }
    "FILE_GUID"                      { pushState(DEFINES_SECTION_WAITING_GUID); return InfTypes.FILE_GUID; }
    "EDK_RELEASE_VERSION"            { pushState(DEFINES_SECTION_WAITING_HEX_NUMBER); return InfTypes.EDK_RELEASE_VERSION; }
    "PI_SPECIFICATION_VERSION"       { pushState(DEFINES_SECTION_WAITING_HEX_N_VERSION_NUMBER); return InfTypes.PI_SPECIFICATION_VERSION; }
    "UEFI_SPECIFICATION_VERSION"     { pushState(DEFINES_SECTION_WAITING_HEX_N_VERSION_NUMBER); return InfTypes.UEFI_SPECIFICATION_VERSION; }
    "MODULE_TYPE"                    { pushState(DEFINES_SECTION_WAITING_MODULE_TYPE); return InfTypes.MODULE_TYPE; }
    "BUILD_NUMBER"                   { pushState(DEFINES_SECTION_WAITING_HEX_NUMBER); return InfTypes.BUILD_NUMBER; }
    "VERSION_STRING"                 { pushState(DEFINES_SECTION_WAITING_DECIMAL_VERSION); return InfTypes.VERSION_STRING; }
    "MODULE_UNI_FILE"                { pushState(DEFINES_SECTION_WAITING_PATH_STRING); return InfTypes.MODULE_UNI_FILE; }
    "LIBRARY_CLASS"                  { pushState(DEFINES_SECTION_WAITING_LIBRARY_CLASS); return InfTypes.LIBRARY_CLASS; }
    "PCD_IS_DRIVER"                  { pushState(DEFINES_SECTION_WAITING_PCD_DRIVER); return InfTypes.PCD_IS_DRIVER; }
    "ENTRY_POINT"                    { pushState(DEFINES_SECTION_WAITING_IDENTIFIER); return InfTypes.ENTRY_POINT; }
    "UNLOAD_IMAGE"                   { pushState(DEFINES_SECTION_WAITING_IDENTIFIER); return InfTypes.UNLOAD_IMAGE; }
    "CONSTRUCTOR"                    { pushState(DEFINES_SECTION_WAITING_IDENTIFIER); return InfTypes.CONSTRUCTOR; }
    "DESTRUCTOR"                     { pushState(DEFINES_SECTION_WAITING_IDENTIFIER); return InfTypes.DESTRUCTOR; }
    "SHADOW"                         { pushState(DEFINES_SECTION_WAITING_BOOLEAN); return InfTypes.SHADOW; }
    "PCI_DEVICE_ID"                  { pushState(DEFINES_SECTION_WAITING_DECIMAL_NUMBER); return InfTypes.PCI_DEVICE_ID; }
    "PCI_VENDOR_ID"                  { pushState(DEFINES_SECTION_WAITING_DECIMAL_NUMBER); return InfTypes.PCI_VENDOR_ID; }
    "PCI_CLASS_CODE"                 { pushState(DEFINES_SECTION_WAITING_DECIMAL_NUMBER); return InfTypes.PCI_CLASS_CODE; }
    "PCI_COMPRESS"                   { pushState(DEFINES_SECTION_WAITING_BOOLEAN); return InfTypes.PCI_COMPRESS; }
    "UEFI_HII_RESOURCE_SECTION"      { pushState(DEFINES_SECTION_WAITING_BOOLEAN); return InfTypes.UEFI_HII_RESOURCE_SECTION; }
    "DEFINE"                         { pushState(DEFINES_SECTION_WAITING_EQ_IDENTIFIER); return InfTypes.DEFINE; }
    "SPEC"                           { pushState(DEFINES_SECTION_WAITING_EQ_DECIMAL_VERSION);return InfTypes.SPEC; }
    "CUSTOM_MAKEFILE"                { pushState(DEFINES_SECTION_WAITING_MAKEFILE); return InfTypes.CUSTOM_MAKEFILE; }
    "DPX_SOURCE"                     { pushState(DEFINES_SECTION_WAITING_PATH_STRING); return InfTypes.DPX_SOURCE; }

    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_HEX_N_VERSION_NUMBER> {
    {HEX_NUMBER}                     { popState(); return InfTypes.HEX_NUMBER; }
    {VERSION_NUMBER}                 { popState(); return InfTypes.VERSION_NUMBER; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_BASE_NAME_STRING> {
    {BASE_NAME_STRING}               { popState(); return InfTypes.BASE_NAME_STRING; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_GUID> {
    {GUID}                           { popState(); return InfTypes.GUID; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_MODULE_TYPE> {
    // Module types
    "BASE"                           { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "SEC"                            { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "PEI_CORE"                       { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "PEIM"                           { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_CORE"                       { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_DRIVER"                     { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_RUNTIME_DRIVER"             { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_SAL_DRIVER"                 { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_SMM_DRIVER"                 { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "UEFI_DRIVER"                    { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "UEFI_APPLICATION"               { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "SMM_CORE"                       { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "MM_STANDALONE"                  { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "MM_CORE_STANDALONE"             { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    "USER_DEFINED"                   { popState(); return InfTypes.MODULE_TYPE_VALUE; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_HEX_NUMBER> {
    {HEX_NUMBER}                     { popState(); return InfTypes.HEX_NUMBER; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_DECIMAL_VERSION> {
    {VERSION_NUMBER}                 { popState(); return InfTypes.VERSION_NUMBER; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_DECIMAL_NUMBER> {
    {DECIMAL_NUMBER}                 { popState(); return InfTypes.NUMBER; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_LIBRARY_CLASS> {
    "BASE"                           { return InfTypes.MODULE_TYPE_VALUE; }
    "SEC"                            { return InfTypes.MODULE_TYPE_VALUE; }
    "PEI_CORE"                       { return InfTypes.MODULE_TYPE_VALUE; }
    "PEIM"                           { return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_CORE"                       { return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_DRIVER"                     { return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_RUNTIME_DRIVER"             { return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_SAL_DRIVER"                 { return InfTypes.MODULE_TYPE_VALUE; }
    "DXE_SMM_DRIVER"                 { return InfTypes.MODULE_TYPE_VALUE; }
    "UEFI_DRIVER"                    { return InfTypes.MODULE_TYPE_VALUE; }
    "UEFI_APPLICATION"               { return InfTypes.MODULE_TYPE_VALUE; }
    "SMM_CORE"                       { return InfTypes.MODULE_TYPE_VALUE; }
    "MM_STANDALONE"                  { return InfTypes.MODULE_TYPE_VALUE; }
    "MM_CORE_STANDALONE"             { return InfTypes.MODULE_TYPE_VALUE; }
    "USER_DEFINED"                   { return InfTypes.MODULE_TYPE_VALUE; }

    {IDENTIFIER}                     { return InfTypes.IDENTIFIER; }
    {CRLF}                           { popState(); return InfTypes.CRLF; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_HEX_NUMBER> {
    {HEX_NUMBER}                     { popState(); InfTypes.HEX_NUMBER; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_BOOLEAN> {
    "TRUE"                           { popState(); return InfTypes.BOOLEAN_VALUE; }
    "FALSE"                          { popState(); return InfTypes.BOOLEAN_VALUE; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_PCD_DRIVER> {
    "PEI_PCD_DRIVER"                 { popState(); return InfTypes.PCD_DRIVER_TYPE; }
    "DXE_PCD_DRIVER"                 { popState(); return InfTypes.PCD_DRIVER_TYPE; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_IDENTIFIER> {
    {IDENTIFIER}                     { popState(); return InfTypes.IDENTIFIER; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_PATH_STRING> {
    {PATH_STRING}                    { popState(); return InfTypes.PATH_STRING; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_EQ_IDENTIFIER> {
    {IDENTIFIER}                     { return InfTypes.IDENTIFIER; }
    {CRLF}                           { popState(); return InfTypes.CRLF; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<DEFINES_SECTION_WAITING_EQ_DECIMAL_VERSION> {
    {IDENTIFIER}                     { return InfTypes.IDENTIFIER; }
    {VERSION_NUMBER}                 { popState(); return InfTypes.VERSION_NUMBER; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}


<DEFINES_SECTION_WAITING_MAKEFILE> {
    "GCC"                            { return InfTypes.COMPILER_TYPE; }
    "MSFT"                           { return InfTypes.COMPILER_TYPE; }
    {PATH_STRING}                    { popState(); return InfTypes.PATH_STRING; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}

<BUILD_OPTIONS_SECTION> {
    "[" { yypushback(1); popState(); }
    
    // Operators
    "="                                  { return InfTypes.EQUALS; }
    "=="                                 { return InfTypes.DOUBLE_EQUALS; }

    // Complex Keys (LHS) containing *, :, _
    [a-zA-Z0-9_*]+(:[a-zA-Z0-9_*_]+)*    { return InfTypes.IDENTIFIER; }

    // Flags (RHS) starting with - or /
    [-/][a-zA-Z0-9_\-+=.:]+              { return InfTypes.IDENTIFIER; }

    // Make macros $(VAR)
    \$\([a-zA-Z0-9_]+\)                  { return InfTypes.IDENTIFIER; }
    
    // Standard elements
    {HEX_NUMBER}                     { return InfTypes.HEX_NUMBER; }
    {VERSION_NUMBER}                 { return InfTypes.VERSION_NUMBER; }
    {DECIMAL_NUMBER}                 { return InfTypes.NUMBER; }
    {STRING}                         { return InfTypes.STRING; }
    {SINGLE_QUOTED_STRING}           { return InfTypes.STRING; }
    {IDENTIFIER}                     { return InfTypes.IDENTIFIER; }
    {PATH_STRING}                    { return InfTypes.PATH_STRING; }
    
    {CRLF}                           { return InfTypes.CRLF; }
    {WHITE_SPACE}                    { return TokenType.WHITE_SPACE; }
    {HASH_COMMENT}                   { return InfTypes.COMMENT; }
    
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
    [^]                              { return TokenType.BAD_CHARACTER; }
}

<SOURCES_SECTION> {
    "[" { yypushback(1); popState(); }
    {PATH_STRING}                    { return InfTypes.PATH_STRING; }
    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
}



<DEPEX_SECTION> {
    "[" { yypushback(1); popState(); }

    "AND"                            { return InfTypes.DEPEX_AND; }
    "OR"                             { return InfTypes.DEPEX_OR; }
    "NOT"                            { return InfTypes.DEPEX_NOT; }
    "TRUE"                           { return InfTypes.BOOLEAN_VALUE; }
    "FALSE"                          { return InfTypes.BOOLEAN_VALUE; }
    "BEFORE"                         { return InfTypes.DEPEX_BEFORE; }
    "AFTER"                          { return InfTypes.DEPEX_AFTER; }
    "SOR"                            { return InfTypes.DEPEX_SOR; }
    "PUSH"                           { return InfTypes.DEPEX_PUSH; }
    "END"                            { return InfTypes.DEPEX_END; }

    {IDENTIFIER}                     { return InfTypes.IDENTIFIER; }
    {GUID}                           { return InfTypes.GUID; }
    "("                              { return InfTypes.LPAREN; }
    ")"                              { return InfTypes.RPAREN; }

    {CRLF}                           { return TokenType.WHITE_SPACE; }
    {WHITE_SPACE}                    { return TokenType.WHITE_SPACE; }
    {HASH_COMMENT}                   { return InfTypes.COMMENT; }

    <<EOF>>                          { clearStack(); yybegin(EOF); return InfTypes.CRLF; }
    [^]                              { return TokenType.BAD_CHARACTER; }
}


{CRLF}                               { return InfTypes.CRLF; }
{WHITE_SPACE}                        { return TokenType.WHITE_SPACE; }
{HASH_COMMENT}                       { return InfTypes.COMMENT; }

// Operators and punctuation
"="                                  { return InfTypes.EQUALS; }
"|"                                  { return InfTypes.PIPE; }
"."                                  { return InfTypes.DOT; }
"("                                  { return InfTypes.LPAREN; }
")"                                  { return InfTypes.RPAREN; }
"["                                  { return InfTypes.LBRACKET; }
"]"                                  { return InfTypes.RBRACKET; }
"*"                                  { return InfTypes.STAR; }
"-"                                  { return InfTypes.MINUS; }

// Default - any other character
[^]                                  { return TokenType.BAD_CHARACTER; }