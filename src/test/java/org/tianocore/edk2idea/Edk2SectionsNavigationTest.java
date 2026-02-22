package org.tianocore.edk2idea;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.tianocore.edk2idea.Dec.psi.DecGuidName;

public class Edk2SectionsNavigationTest extends BasePlatformTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData/PcdNavigation";
    }

    // --- PCD Navigation Tests ---

    public void testPcdFindUsages() {
        myFixture.configureByText("Test.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                "  TokenSpaceGuid.PcdTest<caret>FixedAtBuild|0x00|UINT32|0x00000001\n");

        myFixture.configureByText("Test.dsc", "[Defines]\n[PcdsDynamic]\n" +
                "  TokenSpaceGuid.PcdTestFixedAtBuild|0x01\n");

        myFixture.configureByText("Test.inf", "[Defines]\n  INF_VERSION = 1.25\n\n[Pcd]\n" +
                "  TokenSpaceGuid.PcdTestFixedAtBuild\n");

        // The caret is in Test.dec. We should find usages in Test.dsc and Test.inf
        myFixture.configureFromExistingVirtualFile(myFixture.findFileInTempDir("Test.dec"));

        PsiElement leaf = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        org.tianocore.edk2idea.Dec.psi.DecPcdName target = com.intellij.psi.util.PsiTreeUtil.getParentOfType(
                leaf,
                org.tianocore.edk2idea.Dec.psi.DecPcdName.class);
        assertNotNull("Target DecPcdName not found at caret!", target);

        java.util.Collection<com.intellij.usageView.UsageInfo> usages = myFixture.findUsages(target);

        assertEquals("Should find exactly 3 usages (DSC, INF, and Self-Reference)", 3, usages.size());

        boolean foundInDsc = false;
        boolean foundInInf = false;
        boolean foundInDec = false;

        for (com.intellij.usageView.UsageInfo usage : usages) {
            String fileName = usage.getFile().getName();
            if (fileName.equals("Test.dsc"))
                foundInDsc = true;
            if (fileName.equals("Test.inf"))
                foundInInf = true;
            if (fileName.equals("Test.dec"))
                foundInDec = true;
        }

        assertTrue("Did not find usage in DSC", foundInDsc);
        assertTrue("Did not find usage in INF", foundInInf);
        assertTrue("Did not find usage in DEC (self)", foundInDec);
    }

    public void testDecPcdNameUnifiedClickability() {
        myFixture.configureByText("Test.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                "  Token<caret>SpaceGuid.PcdTestFixedAtBuild|0x00|UINT32|0x00000001\n");

        PsiReference ref1 = myFixture.getReferenceAtCaretPositionWithAssertion();
        assertEquals("TokenSpaceGuid.PcdTestFixedAtBuild", ref1.getCanonicalText());

        myFixture.configureByText("Test2.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                "  TokenSpaceGuid.PcdTest<caret>FixedAtBuild|0x00|UINT32|0x00000001\n");

        PsiReference ref2 = myFixture.getReferenceAtCaretPositionWithAssertion();
        assertEquals("TokenSpaceGuid.PcdTestFixedAtBuild", ref2.getCanonicalText());

        myFixture.configureByText("Test3.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                "  TokenSpaceGuid<caret>.PcdTestFixedAtBuild|0x00|UINT32|0x00000001\n");

        PsiReference ref3 = myFixture.getReferenceAtCaretPositionWithAssertion();
        assertEquals("TokenSpaceGuid.PcdTestFixedAtBuild", ref3.getCanonicalText());
    }

    public void testDscPcdFindUsages() {
        myFixture.configureByText("Test.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                "  TokenSpaceGuid.PcdTestFixedAtBuild|0x00|UINT32|0x00000001\n");

        myFixture.configureByText("Test.dsc", "[Defines]\n[PcdsDynamic]\n" +
                "  TokenSpaceGuid.PcdTest<caret>FixedAtBuild|0x01\n");

        myFixture.configureByText("Test.inf", "[Defines]\n  INF_VERSION = 1.25\n\n[Pcd]\n" +
                "  TokenSpaceGuid.PcdTestFixedAtBuild\n");

        myFixture.configureFromExistingVirtualFile(myFixture.findFileInTempDir("Test.dsc"));

        PsiElement leaf = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        org.tianocore.edk2idea.Dsc.psi.DscPcdName target = com.intellij.psi.util.PsiTreeUtil
                .getParentOfType(leaf, org.tianocore.edk2idea.Dsc.psi.DscPcdName.class);
        assertNotNull("Target DscPcdName not found at caret!", target);

        java.util.Collection<com.intellij.usageView.UsageInfo> usages = myFixture.findUsages(target);

        assertEquals("Should find exactly 3 usages (DEC, INF, and Self-Reference)", 3, usages.size());
    }

    public void testInfPcdFindUsages() {
        myFixture.configureByText("Test.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                "  TokenSpaceGuid.PcdTestFixedAtBuild|0x00|UINT32|0x00000001\n");

        myFixture.configureByText("Test.dsc", "[Defines]\n[PcdsDynamic]\n" +
                "  TokenSpaceGuid.PcdTestFixedAtBuild|0x01\n");

        myFixture.configureByText("Test.inf", "[Defines]\n  INF_VERSION = 1.25\n\n[Pcd]\n" +
                "  TokenSpaceGuid.PcdTe<caret>stFixedAtBuild\n");

        myFixture.configureFromExistingVirtualFile(myFixture.findFileInTempDir("Test.inf"));

        PsiElement leaf = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        org.tianocore.edk2idea.Inf.psi.InfPcdName target = com.intellij.psi.util.PsiTreeUtil
                .getParentOfType(leaf, org.tianocore.edk2idea.Inf.psi.InfPcdName.class);
        assertNotNull("Target InfPcdName not found at caret!", target);

        java.util.Collection<com.intellij.usageView.UsageInfo> usages = myFixture.findUsages(target);

        assertEquals("Should find exactly 3 usages (DEC, DSC, and Self-Reference)", 3, usages.size());
    }

    public void testPsiTreeDump() {
        myFixture.configureByText("Test.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                "  TokenSpaceGuid.PcdTestFixedAtBuild|0x00|UINT32|0x00000001\n");
        System.err.println("=== DEC PSI TREE ===");
        System.err.println(com.intellij.psi.impl.DebugUtil.psiToString(myFixture.getFile(), false));

        myFixture.configureByText("Test.dsc", "[Defines]\n[PcdsDynamic]\n" +
                "  TokenSpaceGuid.PcdTestDynamic|0x01\n");
        System.err.println("=== DSC PSI TREE ===");
        System.err.println(com.intellij.psi.impl.DebugUtil.psiToString(myFixture.getFile(), false));

        myFixture.configureByText("Test.inf", "[Defines]\n  INF_VERSION = 1.25\n\n[Pcd]\n" +
                "  TokenSpaceGuid.PcdTestDynamic\n");
        System.err.println("=== INF PSI TREE ===");
        System.err.println(com.intellij.psi.impl.DebugUtil.psiToString(myFixture.getFile(), false));
        System.err.println("================");
    }

    // --- Protocol, Guid, Ppi Navigation Tests ---

    public void testProtocolNavigation() {
        myFixture.configureByText("Test.dec",
                "[Defines]\n[Protocols]\n" +
                        "  gEfiCcMeasurementProtocolGuid  = { 0x96751a3d, 0x72f4, 0x41a6, { 0xa7, 0x94, 0xed, 0x5d, 0x0e, 0x67, 0xae, 0x6b }}\n");

        myFixture.configureByText("Test.inf",
                "[Defines]\n  INF_VERSION = 1.25\n\n[Protocols]\n" +
                        "  gEfiCcMeasurementProtocol<caret>Guid\n");

        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion();
        assertEquals("gEfiCcMeasurementProtocolGuid", ref.getCanonicalText());

        PsiElement target = ref.resolve();
        assertNotNull("Reference didn't resolve", target);
        assertTrue("Target is not DecGuidName", target instanceof DecGuidName);
        assertEquals("gEfiCcMeasurementProtocolGuid", target.getText());
    }

    public void testPpiNavigation() {
        myFixture.configureByText("Test.dec",
                "[Defines]\n[Ppis]\n" +
                        "  gEfiPeiMpServices2PpiGuid      = { 0x5cb9cb3d, 0x31a4, 0x480c, { 0x94, 0x98, 0x29, 0xd2, 0x69, 0xba, 0xcf, 0xba}}\n");

        myFixture.configureByText("Test.inf",
                "[Defines]\n  INF_VERSION = 1.25\n\n[Ppis]\n" +
                        "  gEfiPeiMpServic<caret>es2PpiGuid\n");

        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion();
        assertEquals("gEfiPeiMpServices2PpiGuid", ref.getCanonicalText());

        PsiElement target = ref.resolve();
        assertNotNull("Reference didn't resolve", target);
        assertTrue("Target is not DecGuidName", target instanceof DecGuidName);
        assertEquals("gEfiPeiMpServices2PpiGuid", target.getText());
    }

    public void testGuidNavigation() {
        myFixture.configureByText("Test.dec",
                "[Defines]\n[Guids]\n" +
                        "  gEfiFileInfoGuid               = { 0x09576E92, 0x6D3F, 0x11D2, { 0x8E, 0x39, 0x00, 0xA0, 0xC9, 0x69, 0x72, 0x3B }}\n");

        myFixture.configureByText("Test.inf",
                "[Defines]\n  INF_VERSION = 1.25\n\n[Guids]\n" +
                        "  gEfiFileInf<caret>oGuid\n");

        PsiReference ref = myFixture.getReferenceAtCaretPositionWithAssertion();
        assertEquals("gEfiFileInfoGuid", ref.getCanonicalText());

        PsiElement target = ref.resolve();
        assertNotNull("Reference didn't resolve", target);
        assertTrue("Target is not DecGuidName", target instanceof DecGuidName);
        assertEquals("gEfiFileInfoGuid", target.getText());
    }

    public void testGuidFindUsages() {
        myFixture.configureByText("Test.dec",
                "[Defines]\n[Protocols]\n" +
                        "  gEfiCcMeasurementPro<caret>tocolGuid  = { 0x96751a3d, 0x72f4, 0x41a6, { 0xa7, 0x94, 0xed, 0x5d, 0x0e, 0x67, 0xae, 0x6b }}\n");

        myFixture.configureByText("Test1.inf",
                "[Defines]\n  INF_VERSION = 1.25\n\n[Protocols]\n" +
                        "  gEfiCcMeasurementProtocolGuid\n");

        myFixture.configureByText("Test2.inf",
                "[Defines]\n  INF_VERSION = 1.25\n\n[Protocols]\n" +
                        "  gEfiCcMeasurementProtocolGuid\n");

        // Set caret context back to the DEC file to trigger usages from definition
        myFixture.configureFromExistingVirtualFile(myFixture.findFileInTempDir("Test.dec"));

        PsiElement leaf = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        DecGuidName target = com.intellij.psi.util.PsiTreeUtil.getParentOfType(leaf, DecGuidName.class);
        assertNotNull("Target DecGuidName not found at caret!", target);

        java.util.Collection<com.intellij.usageView.UsageInfo> usages = myFixture.findUsages(target);

        assertTrue("Should find at least 2 usages", usages.size() >= 2);
    }

    public void testDecGuidHighlighting() {
        myFixture.configureByText("Test.dec",
                "[Defines]\n" +
                        "  DEC_SPECIFICATION = 0x00010005\n\n" +
                        "[Guids]\n" +
                        "  <info textAttributesKey=\"DEC_GUID_NAME\">gEfiGlobalVariableGuid</info> = { 0x8BE4DF61, 0x93CA, 0x11D2, { 0xAA, 0x0D, 0x00, 0xE0, 0x98, 0x03, 0x2B, 0x8C }}\n\n"
                        +
                        "[Protocols]\n" +
                        "  <info textAttributesKey=\"DEC_PROTOCOL_NAME\">gEfiCcMeasurementProtocolGuid</info> = { 0x96751a3d, 0x72f4, 0x41a6, { 0xa7, 0x94, 0xed, 0x5d, 0x0e, 0x67, 0xae, 0x6b }}\n\n"
                        +
                        "[Ppis]\n" +
                        "  <info textAttributesKey=\"DEC_PPI_NAME\">gEfiPeiMpServices2PpiGuid</info> = { 0x5cb9cb3d, 0x31a4, 0x480c, { 0x94, 0x98, 0x29, 0xd2, 0x69, 0xba, 0xcf, 0xba}}\n");

        myFixture.checkHighlighting(false, true, false);
    }

    public void testInfGuidHighlighting() {
        myFixture.configureByText("Test.inf",
                "[Defines]\n" +
                        "  INF_VERSION = 1.25\n\n" +
                        "[Guids]\n" +
                        "  ## SOMETIMES_CONSUMES\n" +
                        "  <info textAttributesKey=\"INF_GUID_NAME\">gEfiImageSecurityDatabaseGuid</info>\n\n" +
                        "[Protocols]\n" +
                        "  <info textAttributesKey=\"INF_PROTOCOL_NAME\">gEfiFirmwareVolume2ProtocolGuid</info> ## SOMETIMES_CONSUMES\n\n"
                        +
                        "[Ppis]\n" +
                        "  <info textAttributesKey=\"INF_PPI_NAME\">gEfiPeiMpServices2PpiGuid</info>\n");

        myFixture.checkHighlighting(false, true, false);
    }
}
