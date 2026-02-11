package org.tianocore.edk2idea.Uni;

import com.intellij.testFramework.ParsingTestCase;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UniParserTest extends ParsingTestCase {
    public UniParserTest() {
        super("", "uni", new UniParserDefinition());
    }

    public void testStrings() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/UniParserGolden";
    }

    @Override
    protected void doTest(boolean checkResult) {
        super.doTest(false);
        if (myFile != null) {
            String tree = com.intellij.psi.impl.DebugUtil.psiToString(myFile, false);

            if (checkResult) {
                try {
                    String testName = getTestName(false);
                    Path goldenPath = Paths.get(getTestDataPath(), testName + ".txt").toAbsolutePath();
                    String expectedFilePath = goldenPath.toString();

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

                    assertSameLinesWithFile(expectedFilePath, tree);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
