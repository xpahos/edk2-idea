package org.tianocore.edk2idea;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiElement;

public class Edk2PcdNavigationTest extends BasePlatformTestCase {

        @Override
        protected String getTestDataPath() {
                return "src/test/testData/PcdNavigation";
        }

        /**
         * Error SHOULD appear: PCD declared as FixedAtBuild in DEC,
         * and redefined in a [PcdsFixedAtBuild] section in the DSC.
         */
        public void testFixedAtBuildAnnotator() {
                myFixture.configureByText("Test.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                                "  TokenSpaceGuid.PcdTestFixedAtBuild|0x00|UINT32|0x00000001\n");

                myFixture.configureByText("Test.dsc", "[Defines]\n[PcdsFixedAtBuild]\n" +
                                "  <error descr=\"FixedAtBuild variables cannot be redefined\">TokenSpaceGuid.PcdTestFixedAtBuild</error>|0x01\n");

                myFixture.checkHighlighting();
        }

        /**
         * Error should NOT appear: PCD declared as FixedAtBuild in DEC,
         * but used in a [PcdsDynamic] section in the DSC — that's allowed.
         */
        public void testNoAnnotationInPcdsDynamic() {
                myFixture.configureByText("TestDyn.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                                "  TokenSpaceGuid.PcdTestDyn|0x00|UINT32|0x00000001\n");

                myFixture.configureByText("TestDyn.dsc", "[Defines]\n[PcdsDynamic]\n" +
                                "  TokenSpaceGuid.PcdTestDyn|0x01\n");

                myFixture.checkHighlighting();
        }

        /**
         * Error should NOT appear: PCD declared as FixedAtBuild in DEC,
         * but used in a [PcdsFeatureFlag] section in the DSC — that's allowed.
         */
        public void testNoAnnotationInPcdsFeatureFlag() {
                myFixture.configureByText("TestFF.dec", "[Defines]\n[PcdsFixedAtBuild]\n" +
                                "  TokenSpaceGuid.PcdTestFF|0x00|UINT32|0x00000001\n");

                myFixture.configureByText("TestFF.dsc", "[Defines]\n[PcdsFeatureFlag]\n" +
                                "  TokenSpaceGuid.PcdTestFF|FALSE\n");

                myFixture.checkHighlighting();
        }

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

                // When searching from DSC, because Edk2PcdReference doesn't currently
                // cross-reference the exact DEC structure explicitly backward in find usages
                // unless the PSI search index connects them, we expect usages.
                // Let's assert we find usage in DEC, INF, and Self.
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
}
