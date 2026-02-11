package org.tianocore.edk2idea.Inf;

import com.intellij.testFramework.*;
import org.jetbrains.annotations.NotNull;

public class InfParserTest extends ParsingTestCase {
    public InfParserTest() {
        super("", "inf", new InfParserDefinition());
    }

    public void testDefines() {
        doTest(true);
    }

    public void testDefinesAllValues() {
        doTest(true);
    }

    public void testSpecialChars() {
        doTest(true);
    }

    public void testDefinesDxeDriver() {
        doTest(true);
    }

    public void testDefinesLibrary() {
        doTest(true);
    }

    public void testDefinesUefiDriver() {
        doTest(true);
    }

    public void testDefinesUefiHandleParsingLib() {
        doTest(true);
    }

    public void testSources() {
        doTest(true);
    }

    public void testPackages() {
        doTest(true);
    }

    public void testLibraryClasses() {
        doTest(true);
    }

    public void testProtocols() {
        doTest(true);
    }

    public void testGuids() {
        doTest(true);
    }

    public void testPpis() {
        doTest(true);
    }

    public void testPcds() {
        doTest(true);
    }

    public void testDepex() {
        doTest(true);
    }

    public void testBinaries() {
        doTest(true);
    }

    public void testBuildOptions() {
        doTest(true);
    }

    public void testUserExtensions() {
        doTest(true);
    }

    public void testSourcesWithMacros() {
        doTest(true);
    }

    public void testSourcesWithFullOptions() {
        doTest(true);
    }

    public void testBinariesWithFullOptions() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/InfParserGolden";
    }

    @Override
    protected void doTest(boolean checkResult) {
        super.doTest(false);
        if (myFile != null) {
            String tree = com.intellij.psi.impl.DebugUtil.psiToString(myFile, false);
            System.out.println("###### ACTUAL OUTPUT START " + getName() + " ######");
            System.out.println(tree);
            System.out.println("###### ACTUAL OUTPUT END " + getName() + " ######");

            // Sanitize the tree output to remove unstable object IDs
            tree = tree.replaceAll(" \\(java\\.lang\\.String@[a-f0-9]+\\)", "")
                    .replaceAll(
                            " \\(com\\.intellij\\.platform\\.testFramework\\.core\\.PresentableFileInfo@[a-f0-9]+\\)",
                            "");

            if (checkResult) {
                try {
                    String testName = getTestName(false);
                    // Use absolute path and robust file writing
                    java.nio.file.Path goldenPath = java.nio.file.Paths.get(getTestDataPath(), testName + ".txt")
                            .toAbsolutePath();
                    String expectedFilePath = goldenPath.toString();

                    if (Boolean.getBoolean("idea.tests.overwrite")) {
                        java.nio.file.Files.write(goldenPath, tree.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                        System.out.println("Overwrote golden file: " + goldenPath);
                        return;
                    }

                    if (!java.nio.file.Files.exists(goldenPath)) {
                        java.nio.file.Files.write(goldenPath, tree.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                        System.out.println("Created golden file: " + goldenPath);
                        return;
                    }

                    assertSameLinesWithFile(expectedFilePath, tree);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}