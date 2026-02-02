package org.tianocore.edk2idea.Fdf;

import com.intellij.testFramework.*;
import org.jetbrains.annotations.NotNull;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class FdfParserTest extends ParsingTestCase {
    public FdfParserTest() {
        super("", "fdf", new FdfParserDefinition());
    }

    public void testDefines() {
        doTest(true);
    }

    public void testFD() {
        doTest(true);
    }

    public void testFV() {
        doTest(true);
    }

    public void testRule() {
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
                    // Adjust path if necessary, but this should be relative to project root in
                    // tests usually,
                    // or we rely on getTestDataPath() but that returns absolute path constructed
                    // from super.
                    // Let's use getTestDataPath() combined with name.
                    Path goldenPath = Paths.get(getTestDataPath(), testName + ".txt");

                    if (!Files.exists(goldenPath)) {
                        // Auto-create for first run if missing
                        Files.createDirectories(goldenPath.getParent());
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
        return "src/test/resources/FdfParserGolden";
    }
}
