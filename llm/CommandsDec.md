Path to Dec flex file:
`./src/main/java/org/tianocore/edk2idea/Dec/Dec.flex`

Run build jflex task:
`./gradlew generateLexer`

Path to Dec bnf file:
`./src/main/java/org/tianocore/edk2idea/Dec/Dec.bnf`

Run build cup task:
`./gradlew generateParser`

Run DecParserTest:
`./gradlew test --tests "org.tianocore.edk2idea.Dec.DecParserTest"`

DEC format standard draft:
`./drafts/edk2-DecSpecification-draft.pdf`