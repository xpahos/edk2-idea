package org.tianocore.edk2idea.Dec;

import com.intellij.lang.Language;

public class DecLanguage extends Language {
    public static final DecLanguage INSTANCE = new DecLanguage();

    private DecLanguage() {
        super("EDK2 Package Declaration File");
    }
}
