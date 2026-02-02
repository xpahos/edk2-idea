Path to Inf flex file: 
`./src/main/java/org/tianocore/edk2idea/Inf/Inf.flex`

Run build jflex task:
`./gradlew generateLexer`

Path to Inf bnf file:
`./src/main/java/org/tianocore/edk2idea/Inf/Inf.bnf`

Run build cup task:
`./gradlew generateParser`

Run InfParserTest:
`./gradlew test --tests "org.tianocore.edk2idea.Inf.InfParserTest"`

Inf format standard draft:
`drafts/edk2-InfSpecification-draft.pdf`