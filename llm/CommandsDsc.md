Path to Dsc flex file:
`./src/main/java/org/tianocore/edk2idea/Dsc/Dsc.flex`

Run build jflex task:
`./gradlew generateLexer`

Path to Dsc bnf file:
`./src/main/java/org/tianocore/edk2idea/Dsc/Dsc.bnf`

Run build cup task:
`./gradlew generateParser`

Run DscParserTest:
`./gradlew test --tests "org.tianocore.edk2idea.Dsc.DscParserTest"`

DSC format standard draft:
`./drafts/edk2-DscSpecification-draft.pdf`