Path to Fdf flex file:
`./src/main/java/org/tianocore/edk2idea/Fdf/Fdf.flex`

Run build jflex task:
`./gradlew generateLexer`

Path to Fdf bnf file:
`./src/main/java/org/tianocore/edk2idea/Fdf/Fdf.bnf`

Run build cup task:
`./gradlew generateParser`

Run FdfParserTest:
`./gradlew test --tests "org.tianocore.edk2idea.Fdf.FdfParserTest"`

Fdf format standard draft:
`drafts/edk2-FdfSpecification-draft.pdf`