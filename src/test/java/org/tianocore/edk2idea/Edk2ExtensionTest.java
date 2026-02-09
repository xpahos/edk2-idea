package org.tianocore.edk2idea;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.tianocore.edk2idea.Dsc.DscFileType;
import org.tianocore.edk2idea.Fdf.FdfFileType;

public class Edk2ExtensionTest extends BasePlatformTestCase {
    public void testDscIncFileType() {
        myFixture.configureByText("test.dsc.inc", "");
        System.out.println("File type for test.dsc.inc: " + myFixture.getFile().getFileType().getName());
        assertEquals(DscFileType.INSTANCE, myFixture.getFile().getFileType());
    }

    public void testFdfIncFileType() {
        myFixture.configureByText("test.fdf.inc", "");
        System.out.println("File type for test.fdf.inc: " + myFixture.getFile().getFileType().getName());
        assertEquals(FdfFileType.INSTANCE, myFixture.getFile().getFileType());
    }

    public void testStandardDscFileType() {
        myFixture.configureByText("test.dsc", "");
        System.out.println("File type for test.dsc: " + myFixture.getFile().getFileType().getName());
        assertEquals(DscFileType.INSTANCE, myFixture.getFile().getFileType());
    }

    public void testStandardFdfFileType() {
        myFixture.configureByText("test.fdf", "");
        System.out.println("File type for test.fdf: " + myFixture.getFile().getFileType().getName());
        assertEquals(FdfFileType.INSTANCE, myFixture.getFile().getFileType());
    }
}
