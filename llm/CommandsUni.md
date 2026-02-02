Path to Uni flex file: 
`./src/main/java/org/tianocore/edk2idea/Uni/Uni.flex`

Run build jflex task:
`./gradlew generateLexer`

Path to Uni bnf file:
`./src/main/java/org/tianocore/edk2idea/Uni/Uni.bnf`

Run build cup task:
`./gradlew generateParser`

Run UniParserTest:
`./gradlew test --tests "org.tianocore.edk2idea.Uni.UniParserTest"`

Uni format standard draft:
`drafts/edk2-UniSpecification-draft.pdf`