package org.tianocore.edk2idea;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.tianocore.edk2idea.Dsc.DscFileType;
import org.tianocore.edk2idea.Fdf.FdfFileType;

public class HeadlessParsingTest extends BasePlatformTestCase {

    public void testHeadlessDsc() {
        String content = """
                DpcLib|NetworkPkg/Library/DxeDpcLib/DxeDpcLib.inf
                NetLib|NetworkPkg/Library/DxeNetLib/DxeNetLib.inf
                IpIoLib|NetworkPkg/Library/DxeIpIoLib/DxeIpIoLib.inf
                """;
        PsiFile file = myFixture.configureByText("NetworkLibs.dsc.inc", content);
        assertEquals(DscFileType.INSTANCE, file.getFileType());
        ensureNoErrors(file);
    }

    public void testHeadlessFdf() {
        String content = """
                INF  NetworkPkg/DpcDxe/DpcDxe.inf
                INF  NetworkPkg/SnpDxe/SnpDxe.inf
                !if $(NETWORK_VLAN_ENABLE) == TRUE
                  INF  NetworkPkg/VlanConfigDxe/VlanConfigDxe.inf
                !endif
                """;
        PsiFile file = myFixture.configureByText("Network.fdf.inc", content);
        assertEquals(FdfFileType.INSTANCE, file.getFileType());
        ensureNoErrors(file);
    }

    private void ensureNoErrors(PsiFile file) {
        PsiErrorElement error = PsiTreeUtil.findChildOfType(file, PsiErrorElement.class);
        if (error != null) {
            fail("Found parser error: " + error.getErrorDescription() + " at text: '" + error.getText() + "'");
        }
    }
}
