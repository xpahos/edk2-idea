package org.tianocore.edk2idea.Fdf;

import com.intellij.lang.Language;

public class FdfLanguage extends Language {

    public static final FdfLanguage INSTANCE = new FdfLanguage();

    private FdfLanguage() {
        super("EDK2 Flash Description File");
    }
}
