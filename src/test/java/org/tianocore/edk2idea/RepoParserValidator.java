package org.tianocore.edk2idea;

import com.intellij.core.CoreApplicationEnvironment;
import com.intellij.core.CoreProjectEnvironment;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.local.CoreLocalFileSystem;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.LightVirtualFile;
import org.tianocore.edk2idea.Dec.DecFileType;
import org.tianocore.edk2idea.Dec.DecParserDefinition;
import org.tianocore.edk2idea.Dsc.DscFileType;
import org.tianocore.edk2idea.Dsc.DscParserDefinition;
import org.tianocore.edk2idea.Fdf.FdfFileType;
import org.tianocore.edk2idea.Fdf.FdfParserDefinition;
import org.tianocore.edk2idea.Inf.InfFileType;
import org.tianocore.edk2idea.Inf.InfParserDefinition;
import org.tianocore.edk2idea.Uni.UniFileType;
import org.tianocore.edk2idea.Uni.UniParserDefinition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class RepoParserValidator {

    private static int processedFiles = 0;
    private static int passedFiles = 0;
    private static int failedFiles = 0;
    private static final Map<String, String> errors = new LinkedHashMap<>();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: Path parameter is not specified.");
            System.exit(1);
        }

        String edk2Path = args[0];
        File rootDir = new File(edk2Path);

        if (!rootDir.exists() || !rootDir.isDirectory()) {
            System.err.println("Error: Path does not exist or is not a directory: " + edk2Path);
            System.exit(1);
        }

        System.out.println("Validating parsers on: " + edk2Path);

        Disposable disposable = Disposer.newDisposable();
        try {
            CoreApplicationEnvironment appEnv = new CoreApplicationEnvironment(disposable);
            CoreProjectEnvironment projectEnv = new CoreProjectEnvironment(disposable, appEnv);

            // Register FileTypes
            appEnv.registerFileType(InfFileType.INSTANCE, "inf");
            appEnv.registerFileType(DscFileType.INSTANCE, "dsc");
            appEnv.registerFileType(FdfFileType.INSTANCE, "fdf");
            appEnv.registerFileType(DecFileType.INSTANCE, "dec");
            appEnv.registerFileType(UniFileType.INSTANCE, "uni");

            // Register ParserDefinitions
            appEnv.registerParserDefinition(new InfParserDefinition());
            appEnv.registerParserDefinition(new DscParserDefinition());
            appEnv.registerParserDefinition(new FdfParserDefinition());
            appEnv.registerParserDefinition(new DecParserDefinition());
            appEnv.registerParserDefinition(new UniParserDefinition());

            PsiManager psiManager = PsiManager.getInstance(projectEnv.getProject());
            PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(projectEnv.getProject());

            validateDirectory(rootDir, psiFileFactory);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Disposer.dispose(disposable);
        }

        System.out.println("\nValidation Results:");
        if (!errors.isEmpty()) {
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                System.out.println(entry.getKey() + "\n\n" + entry.getValue() + "\n\n");
            }
            System.out.println("Total recognized files: " + passedFiles);
            System.out.println("Total files with errors: " + failedFiles);

            // Workaround for IntelliJ Platform 2025.1:
            // The "PeriodicMetricReader" thread is not a daemon and prevents the JVM from
            // exiting.
            // We explicitly find it and interrupt it so the process can terminate.
            Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
            for (Thread t : stackTraces.keySet()) {
                if ("PeriodicMetricReader".equals(t.getName()) && !t.isDaemon()) {
                    try {
                        System.out.println("Interrupting " + t.getName() + " to allow exit.");
                        t.interrupt();
                    } catch (Exception e) {
                        System.err.println("Failed to interrupt " + t.getName() + ": " + e.getMessage());
                    }
                }
            }
        }

        // Fallback: forcefully exit if we are still running.
        System.exit(failedFiles > 0 ? 1 : 0);
    }

    private static void validateDirectory(File rootDir, PsiFileFactory psiFileFactory) {
        try {
            Files.walkFileTree(rootDir.toPath(), new java.nio.file.SimpleFileVisitor<Path>() {
                @Override
                public java.nio.file.FileVisitResult visitFile(Path file,
                        java.nio.file.attribute.BasicFileAttributes attrs) {
                    validateFile(file.toFile(), psiFileFactory);
                    return java.nio.file.FileVisitResult.CONTINUE;
                }

                @Override
                public java.nio.file.FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.println("Failed to visit file: " + file + " (" + exc.getMessage() + ")");
                    return java.nio.file.FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void validateFile(File file, PsiFileFactory psiFileFactory) {
        String fileName = file.getName().toLowerCase();

        System.out.println("Processing: " + file.getAbsolutePath());

        com.intellij.openapi.fileTypes.FileType fileType = null;
        if (fileName.endsWith(".inf"))
            fileType = InfFileType.INSTANCE;
        else if (fileName.endsWith(".dsc") || fileName.endsWith(".dsc.inc"))
            fileType = DscFileType.INSTANCE;
        else if (fileName.endsWith(".fdf") || fileName.endsWith(".fdf.inc"))
            fileType = FdfFileType.INSTANCE;
        else if (fileName.endsWith(".dec"))
            fileType = DecFileType.INSTANCE;
        else if (fileName.endsWith(".uni"))
            fileType = UniFileType.INSTANCE;

        if (fileType != null) {
            try {
                String content = Files.readString(file.toPath());
                // Use LightVirtualFile to verify parsing without full VFS
                PsiFile psiFile = psiFileFactory.createFileFromText(file.getName(), fileType, content);

                if (psiFile == null) {
                    return;
                }

                // Check if parser definition exists for the language
                if (LanguageParserDefinitions.INSTANCE.forLanguage(psiFile.getLanguage()) == null) {
                    return;
                }

                boolean hasError = false;
                StringBuilder errorMsg = new StringBuilder();

                Collection<PsiErrorElement> errorElements = PsiTreeUtil.collectElementsOfType(psiFile,
                        PsiErrorElement.class);
                if (!errorElements.isEmpty()) {
                    hasError = true;
                    for (PsiErrorElement error : errorElements) {
                        errorMsg.append(error.getErrorDescription())
                                .append(" at offset ")
                                .append(error.getTextOffset())
                                .append(": '")
                                .append(error.getText())
                                .append("'\n");
                    }
                }

                if (hasError) {
                    failedFiles++;
                    errors.put(file.getAbsolutePath(), errorMsg.toString());
                } else {
                    passedFiles++;
                }

            } catch (IOException e) {
                System.err.println("Failed to read file: " + file.getAbsolutePath());
            } catch (Throwable t) {
                failedFiles++;
                errors.put(file.getAbsolutePath(), "Exception: " + t.getMessage());
            }
        }
    }
}
