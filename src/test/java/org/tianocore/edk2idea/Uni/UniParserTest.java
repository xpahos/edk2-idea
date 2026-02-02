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

                    if (!Files.exists(goldenPath)) {
                        Files.write(goldenPath, tree.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Created golden file: " + goldenPath);
                        return;
                    }

                    // For now, if file exists, we rely on super.doTest() (via ParsingTestCase) to
                    // check,
                    // but we can also manually check if needed or just handle the missing case
                    // logic.
                    // The ParsingTestCase usually throws if checkResult is true and no file.
                    // We'll leave standard behavior for now, but keep auto-gen logic for new tests.
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (checkResult) {
            try {
                checkResult(getTestName(false), myFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
