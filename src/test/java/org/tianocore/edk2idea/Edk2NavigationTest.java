package org.tianocore.edk2idea;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.tianocore.edk2idea.Inf.InfTypes;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginDescriptor;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiElement;

public class Edk2NavigationTest extends BasePlatformTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testInfNavigation() {
    myFixture.addFileToProject("MdePkg/Include/Library/UefiLib.h", "// Header file");
    myFixture.configureByText("Test.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 12345678-1234-1234-1234-123456789012
          MODULE_TYPE = BASE
          VERSION_STRING = 1.0
          LIBRARY_CLASS = TestLib

        [Sources]
          MdePkg/Include/Library/UefiLib<caret>.h
        """);

    // Try automatic resolution
    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull(reference);
    PsiElement target = reference.resolve();
    assertNotNull(target);
    assertEquals("UefiLib.h", ((PsiFile) target).getName());
  }

  public void testRelativeNavigation() {
    myFixture.addFileToProject("SubDir/Local.h", "// Local header");
    PsiFile file = myFixture.addFileToProject("SubDir/Test.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 12345678-1234-1234-1234-123456789012
          MODULE_TYPE = BASE
          VERSION_STRING = 1.0
          LIBRARY_CLASS = TestLib

        [Sources]
          Local.h
        """);
    myFixture.configureFromExistingVirtualFile(file.getVirtualFile());
    int offset = file.getText().indexOf("Local.h");
    myFixture.getEditor().getCaretModel().moveToOffset(offset + 1);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull(reference);
    PsiElement target = reference.resolve();
    assertNotNull(target);
    assertEquals("Local.h", ((PsiFile) target).getName());
  }

  public void testDscIncludeNavigation() {
    myFixture.addFileToProject("Included.dsc.inc", "# Included file");
    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLAFTORM_NAME = Test
          PLATFORM_GUID = 12345678-1234-1234-1234-123456789012
          PLATFORM_VERSION = 1.0
          DSC_SPECIFICATION = 0x00010005
          OUTPUT_DIRECTORY = Build/Test
          SUPPORTED_ARCHITECTURES = X64
          BUILD_TARGETS = DEBUG|RELEASE
          SKUID_IDENTIFIER = DEFAULT

        !include Included<caret>.dsc.inc
        """);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Reference did not resolve", target);
    assertEquals("Included.dsc.inc", ((PsiFile) target).getName());
  }

  public void testDscLibraryClassNavigation() {
    myFixture.addFileToProject("MdePkg/Library/BaseLib/BaseLib.inf", "// INF File");
    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLATFORM_NAME = Test

        [LibraryClasses]
          BaseLib|MdePkg/Library/BaseLib/BaseLib<caret>.inf
        """);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Reference did not resolve", target);
    assertEquals("BaseLib.inf", ((PsiFile) target).getName());
  }

  public void testDscFlashDefinitionNavigation() {
    myFixture.addFileToProject("Test.fdf", "// FDF File");
    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLATFORM_NAME = Test
          FLASH_DEFINITION = Test<caret>.fdf
        """);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Reference did not resolve", target);
    assertEquals("Test.fdf", ((PsiFile) target).getName());
  }

  public void testDscComponentNavigation() {
    myFixture.addFileToProject("Component.inf", "");
    myFixture.addFileToProject("Component2.inf", "");
    myFixture.addFileToProject("Exec.exe", "");
    myFixture.addFileToProject("Lib.inf", "");

    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLATFORM_NAME = Test

        [Components]
          Component<caret>.inf
          Component2.inf EXEC = Exec.exe
          Component3.inf {
            <LibraryClasses>
              Lib|Lib.inf
          }
        """);

    // 1. Standard Component
    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Target not found", target);
    assertEquals("Component.inf", ((PsiFile) target).getName());

    // 2. EXEC Component (INF)
    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLATFORM_NAME = Test

        [Components]
          Component.inf
          Component2<caret>.inf EXEC = Exec.exe
        """);
    reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    target = reference.resolve();
    assertEquals("Component2.inf", ((PsiFile) target).getName());

    // 3. EXEC Component (EXEC)
    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLATFORM_NAME = Test

        [Components]
          Component.inf
          Component2.inf EXEC = Exec<caret>.exe
        """);
    reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    target = reference.resolve();
    assertEquals("Exec.exe", ((PsiFile) target).getName());

    // 4. Nested Library Class
    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLATFORM_NAME = Test

        [Components]
          Component3.inf {
            <LibraryClasses>
              Lib|Lib<caret>.inf
          }
        """);
    reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    target = reference.resolve();
    assertEquals("Lib.inf", ((PsiFile) target).getName());
  }

  public void testDscComponentConditionals() {
    myFixture.addFileToProject("Lib.inf", "");
    myFixture.addFileToProject("Component.inf", "");

    myFixture.configureByText("Test.dsc", """
        [Defines]
          PLATFORM_NAME = Test

        [Components]
          Component.inf {
            <LibraryClasses>
              !if $(SMM_REQUIRE) == TRUE
                Lib|Lib<caret>.inf
              !endif
          }

          !if $(SMM_REQUIRE) == TRUE
             Component.inf
          !endif
        """);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Target not found", target);
    assertEquals("Lib.inf", ((PsiFile) target).getName());
  }

  public void testFdfIncludeNavigation() {
    myFixture.addFileToProject("Included.fdf.inc", "# Included file");
    myFixture.configureByText("Test.fdf", """
        [Defines]
          FD_NAME = Test

        !include Included<caret>.fdf.inc
        """);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Reference did not resolve", target);
    assertEquals("Included.fdf.inc", ((PsiFile) target).getName());
  }

  public void testDecIncludeNavigation() {
    myFixture.addFileToProject("Include/Library/EthMacLib.h", "// Header");
    myFixture.configureByText("Test.dec", """
        [Defines]
          DEC_SPECIFICATION              = 0x00010005
          PACKAGE_NAME                   = TestPkg
          PACKAGE_GUID                   = 12345678-1234-1234-1234-123456789012
          PACKAGE_VERSION                = 0.1

        [Includes]
          Include/Library/EthMac<caret>Lib.h
        """);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Reference did not resolve", target);
    assertEquals("EthMacLib.h", ((PsiFile) target).getName());
  }

  public void testFdfInfNavigation() {
    myFixture.addFileToProject("MyModule.inf", "");
    myFixture.configureByText("Test.fdf", """
        [FV.Internal]
        INF MyModule<caret>.inf
        """);
    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Reference did not resolve", target);
    assertEquals("MyModule.inf", ((PsiFile) target).getName());

    // Test with options
    myFixture.configureByText("Test2.fdf", """
        [FV.Internal]
        INF RuleOverride=Uncompressed MyModule<caret>.inf
        """);
    reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found (with options)", reference);
    target = reference.resolve();
    assertNotNull("Reference did not resolve (with options)", target);
    assertEquals("MyModule.inf", ((PsiFile) target).getName());
  }

  public void testInfPackagesNavigation() {
    myFixture.addFileToProject("MdePkg/MdePkg.dec", "");
    myFixture.configureByText("Test.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = TestModule
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE = UEFI_DRIVER
          VERSION_STRING = 1.0
          ENTRY_POINT = UefiMain

        [Packages]
          MdePkg/MdePkg.dec
        """);
    myFixture.checkHighlighting();

    // Verify reference resolves
    int offset = myFixture.getFile().getText().indexOf("MdePkg/MdePkg.dec");
    var element = myFixture.getFile().findElementAt(offset);
    assertNotNull("Element not found at offset", element);

    // Check if valid reference exists on element or parent
    com.intellij.psi.PsiReference reference = element.getReference();
    if (reference == null && element.getParent() != null) {
      reference = element.getParent().getReference();
      if (reference == null) {
        com.intellij.psi.PsiReference[] refs = element.getParent().getReferences();
        if (refs.length > 0)
          reference = refs[0];
      }
    }
    assertNotNull("Reference should be found", reference);
    assertNotNull("Reference should resolve", reference.resolve());
  }

  public void testInfBinariesNavigation() {
    myFixture.configureByText("TestBinaries.inf", """
        [Defines]
          INF_VERSION = 1.25
          BASE_NAME = Test
          FILE_GUID = 00000000-0000-0000-0000-000000000000
          MODULE_TYPE = BASE
          VERSION_STRING = 1.0
        [Binaries]
          BIN|Logo.bmp|*
        """);

    // Check that we can resolve the reference at "Logo.bmp"
    int offset = myFixture.getEditor().getDocument().getText().indexOf("Logo.bmp");
    com.intellij.psi.PsiReference ref = myFixture.getFile().findReferenceAt(offset);
    assertNotNull("Reference should exist for Logo.bmp", ref);
  }

  public void testMacroNavigation() {
    myFixture.addFileToProject("Platform/BaseLib.inf", "// BaseLib");
    myFixture.configureByText("Test.dsc", """
        [Defines]
          DEFINE PLATFORM_DIR = Platform
          SET OTHER_DIR = Platform

        [LibraryClasses]
          BaseLib|$(PLATFORM_DIR)/BaseLib<caret>.inf
        """);

    // Initial verification with DEFINE
    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Target not found using DEFINE", target);
    assertEquals("BaseLib.inf", ((PsiFile) target).getName());

    // Verification with SET (Simulated by modifying text to use SET variable)
    myFixture.configureByText("Test2.dsc", """
        [Defines]
          DEFINE PLATFORM_DIR = Platform
          SET OTHER_DIR = Platform

        [LibraryClasses]
          BaseLib|$(OTHER_DIR)/BaseLib<caret>.inf
        """);
    reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    target = reference.resolve();
    assertNotNull("Target not found using SET", target);
    assertEquals("BaseLib.inf", ((PsiFile) target).getName());
  }

  public void testInfMacroNavigation() {
    myFixture.addFileToProject("Platform/Include/api.h", "// Header");
    myFixture.configureByText("Test.dsc", """
        [Defines]
          DEFINE PLATFORM_DIR = Platform
        """);
    myFixture.configureByText("Test.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 12345678-1234-1234-1234-123456789012
          MODULE_TYPE = BASE
          VERSION_STRING = 1.0
          LIBRARY_CLASS = TestLib

        [Sources]
          $(PLATFORM_DIR)/Include/api<caret>.h
        """);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Target not found", target);
    assertEquals("api.h", ((PsiFile) target).getName());
  }

  public void testInfUndefinedMacroNavigation() {
    myFixture.addFileToProject("Pkg/Include/Library/MissingMacroLib.h", "// Header");
    // No DSC definition for UNKNOWN_PKG_DIR
    myFixture.configureByText("Test.inf", """
        [Defines]
          INF_VERSION = 0x00010005
          BASE_NAME = Test
          FILE_GUID = 12345678-1234-1234-1234-123456789012
          MODULE_TYPE = BASE
          VERSION_STRING = 1.0
          LIBRARY_CLASS = TestLib

        [Sources]
          $(UNKNOWN_PKG_DIR)/Include/Library/MissingMacroLib<caret>.h
        """);

    PsiReference reference = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Reference not found", reference);
    PsiElement target = reference.resolve();
    assertNotNull("Target not found (fuzzy matching failed)", target);
    assertEquals("MissingMacroLib.h", ((PsiFile) target).getName());
  }
}
