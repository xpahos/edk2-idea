package org.tianocore.edk2idea.Inf;

import com.intellij.testFramework.ParsingTestCase;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.DebugUtil;
import org.jetbrains.annotations.NotNull;

public class DebugTreePrinter extends ParsingTestCase {
    public DebugTreePrinter() {
        super("", "inf", new InfParserDefinition());
    }

    public void testGenerateGolden() throws Exception {
        String[] files = {
                "Packages",
                "LibraryClasses",
                "Protocols",
                "Guids",
                "Ppis",
                "Pcds",
                "Depex",
                "Binaries",
                "BuildOptions",
                "UserExtensions",
                "Defines"
        };

        for (String name : files) {
            System.out.println("___FILE_" + name + "___");
            java.nio.file.Path path = java.nio.file.Paths.get("src/test/resources/InfParserGolden/" + name + ".inf");
            String text = java.nio.file.Files.readString(path);
            PsiFile file = createFile(name + ".inf", text);
            // using false, false to match standard ParsingTestCase output format
            System.out.println(DebugUtil.psiToString(file, false, false));
            System.out.println("___END_" + name + "___");
        }
    }

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "src/test/resources/InfParserGolden";
    }
}
