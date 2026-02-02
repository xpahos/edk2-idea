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
        }
        if (checkResult) {
            try {
                checkResult(getTestName(false), myFile);
            } catch (Throwable e) {
                System.out.println("###### EXCEPTION " + getName() + " ######");
                e.printStackTrace(System.out);
                System.out.println("Message: " + e.getMessage());
                try {
                    java.lang.reflect.Method getExpected = e.getClass().getMethod("getExpected");
                    java.lang.reflect.Method getActual = e.getClass().getMethod("getActual");
                    System.out.println("Reflection Expected: " + getExpected.invoke(e));
                    System.out.println("Reflection Actual: " + getActual.invoke(e));
                } catch (Exception ex) {
                    System.out.println("Reflection failed: " + ex);
                    // Try fields
                    try {
                        java.lang.reflect.Field expectedF = e.getClass().getDeclaredField("expected");
                        expectedF.setAccessible(true);
                        System.out.println("Field Expected: " + expectedF.get(e));
                        java.lang.reflect.Field actualF = e.getClass().getDeclaredField("actual");
                        actualF.setAccessible(true);
                        System.out.println("Field Actual: " + actualF.get(e));
                    } catch (Exception ex2) {
                        System.out.println("Field Reflection failed: " + ex2);
                    }
                }
                System.out.println("###### EXCEPTION END ######");
                throw new RuntimeException(e);
            }
        }
    }
}