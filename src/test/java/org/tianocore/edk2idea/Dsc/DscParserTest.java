package org.tianocore.edk2idea.Dsc;

import com.intellij.testFramework.*;
import org.jetbrains.annotations.NotNull;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class DscParserTest extends ParsingTestCase {
    public DscParserTest() {
        super("", "dsc", new DscParserDefinition());
    }

    public void testDefines() {
        doTest(true);
    }

    public void testComponents() {
        doTest(true);
    }

    public void testLibraryClasses() {
        doTest(true);
    }

    public void testPcds() {
        doTest(true);
    }

    public void testBuildOptions() {
        doTest(true);
    }

    public void testSkuIds() {
        doTest(true);
    }

    public void testPackages() {
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
                    Path goldenPath = Paths.get("src/test/resources/DscParserGolden", testName + ".txt");

                    if (Boolean.getBoolean("idea.tests.overwrite")) {
                        Files.write(goldenPath, tree.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Overwrote golden file: " + goldenPath);
                        return;
                    }

                    if (!Files.exists(goldenPath)) {
                        Files.write(goldenPath, tree.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Created golden file: " + goldenPath);
                        return;
                    }

                    String expected = new String(Files.readAllBytes(goldenPath), StandardCharsets.UTF_8);

                    String expectedNorm = expected.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
                    String actualNorm = tree.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");

                    assertEquals("AST mismatch for " + testName, expectedNorm, actualNorm);

                } catch (Exception e) {
                    System.out.println("###### EXCEPTION " + getName() + " ######");
                    e.printStackTrace(System.out);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/DscParserGolden";
    }
}
