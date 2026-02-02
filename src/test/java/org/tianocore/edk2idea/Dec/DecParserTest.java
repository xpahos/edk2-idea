package org.tianocore.edk2idea.Dec;

import com.intellij.testFramework.*;
import org.jetbrains.annotations.NotNull;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class DecParserTest extends ParsingTestCase {
    public DecParserTest() {
        super("", "dec", new DecParserDefinition());
    }

    public void testDefines() {
        doTest();
    }

    public void testIncludes() {
        doTest();
    }

    public void testLibraryClasses() {
        doTest();
    }

    public void testGuids() {
        doTest();
    }

    public void testProtocols() {
        doTest();
    }

    public void testPpis() {
        doTest();
    }

    public void testPcds() {
        doTest();
    }

    public void testUserExtensions() {
        doTest();
    }

    private void doTest() {
        doTest(true);
    }

    @Override
    protected void doTest(boolean checkResult) {
        super.doTest(false);
        if (myFile != null) {
            String tree = com.intellij.psi.impl.DebugUtil.psiToString(myFile, false);

            if (checkResult) {
                try {
                    String testName = getTestName(false);
                    Path goldenPath = Paths.get("src/test/resources/DecParserGolden", testName + ".txt");

                    if (!Files.exists(goldenPath)) {
                        Files.write(goldenPath, tree.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Created golden file: " + goldenPath);
                        return;
                    }

                    String expected = new String(Files.readAllBytes(goldenPath), StandardCharsets.UTF_8);

                    String expectedNorm = expected.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
                    String actualNorm = tree.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");

                    assertEquals("AST mismatch for " + testName, expectedNorm, actualNorm);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/DecParserGolden";
    }
}
