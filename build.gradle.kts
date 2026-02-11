import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.5.0"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "org.tianocore"
version = "1.0-SNAPSHOT"

// Include the generated files in the source set
sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
    }
    test {
        java {
            srcDirs("src/test/java")
        }
    }
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        create("CL", "2025.1")
        testFramework(TestFrameworkType.Platform)
    }
    testImplementation("junit:junit:4.13.2")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
        }

        changeNotes = """
      Initial version
    """.trimIndent()
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

tasks {
    buildPlugin {
        dependsOn(generateParser, generateLexer)
    }

    clean {
        dependsOn("cleanGenerated")
    }

    compileJava {
        dependsOn("generateParser")
        dependsOn("generateLexer")
    }

    processResources {
        dependsOn("generateParser")
        dependsOn("generateLexer")
    }

    generateParser {
        dependsOn("generateInfParser")
        dependsOn("generateDscParser")
        dependsOn("generateFdfParser")
        dependsOn("generateDecParser")
        dependsOn("generateUniParser")
        enabled = false
    }
    generateLexer {
        dependsOn("generateInfLexer")
        dependsOn("generateDscLexer")
        dependsOn("generateFdfLexer")
        dependsOn("generateDecLexer")
        dependsOn("generateUniLexer")
        enabled = false
    }

    processTestResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    test {
        testLogging {
            showStandardStreams = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        systemProperty("idea.tests.overwrite", System.getProperty("idea.tests.overwrite", "false"))
    }
}

tasks.register<Delete>("cleanGenerated") {
    group = "grammarkit"
    delete = setOf("src/main/gen/")
}

tasks.register<GenerateParserTask>("generateInfParser") {
    group = "parsers"
    targetRootOutputDir.set(File("src/main/gen"))
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Inf/Inf.bnf"))
    pathToParser.set("org/tianocore/edk2idea/Inf/InfParser.java")
    pathToPsiRoot.set("org/tianocore/edk2idea/Inf/psi")
}
tasks.register<GenerateLexerTask>("generateInfLexer") {
    group = "lexers"
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Inf/Inf.flex"))
    targetOutputDir.set(File("src/main/gen/org/tianocore/edk2idea/Inf"))
}

tasks.register<GenerateParserTask>("generateDscParser") {
    group = "parsers"
    targetRootOutputDir.set(File("src/main/gen"))
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Dsc/Dsc.bnf"))
    pathToParser.set("org/tianocore/edk2idea/Dsc/DscParser.java")
    pathToPsiRoot.set("org/tianocore/edk2idea/Dsc/psi")
}
tasks.register<GenerateLexerTask>("generateDscLexer") {
    group = "lexers"
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Dsc/Dsc.flex"))
    targetOutputDir.set(File("src/main/gen/org/tianocore/edk2idea/Dsc"))
}

tasks.register<GenerateParserTask>("generateFdfParser") {
    group = "parsers"
    targetRootOutputDir.set(File("src/main/gen"))
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Fdf/Fdf.bnf"))
    pathToParser.set("org/tianocore/edk2idea/Fdf/FdfParser.java")
    pathToPsiRoot.set("org/tianocore/edk2idea/Fdf/psi")
}
tasks.register<GenerateLexerTask>("generateFdfLexer") {
    group = "lexers"
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Fdf/Fdf.flex"))
    targetOutputDir.set(File("src/main/gen/org/tianocore/edk2idea/Fdf"))
}

tasks.register<GenerateParserTask>("generateDecParser") {
    group = "parsers"
    targetRootOutputDir.set(File("src/main/gen"))
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Dec/Dec.bnf"))
    pathToParser.set("org/tianocore/edk2idea/Dec/DecParser.java")
    pathToPsiRoot.set("org/tianocore/edk2idea/Dec/psi")
}
tasks.register<GenerateLexerTask>("generateDecLexer") {
    group = "lexers"
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Dec/Dec.flex"))
    targetOutputDir.set(File("src/main/gen/org/tianocore/edk2idea/Dec"))
}

tasks.register<GenerateParserTask>("generateUniParser") {
    group = "parsers"
    targetRootOutputDir.set(File("src/main/gen"))
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Uni/Uni.bnf"))
    pathToParser.set("org/tianocore/edk2idea/Uni/UniParser.java")
    pathToPsiRoot.set("org/tianocore/edk2idea/Uni/psi")
}
tasks.register<GenerateLexerTask>("generateUniLexer") {
    group = "lexers"
    sourceFile.set(File("src/main/java/org/tianocore/edk2idea/Uni/Uni.flex"))
    targetOutputDir.set(File("src/main/gen/org/tianocore/edk2idea/Uni"))
}

tasks.register<JavaExec>("validateParsers") {
    group = "verification"
    description = "Validates plugin parsers against an EDK2 repository."
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("org.tianocore.edk2idea.RepoParserValidator")

    if (project.hasProperty("edk2Path")) {
        args(project.property("edk2Path"))
    }
}