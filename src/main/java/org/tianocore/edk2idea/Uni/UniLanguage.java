package org.tianocore.edk2idea.Uni;

import com.intellij.lang.Language;

public class UniLanguage extends Language {
    public static final UniLanguage INSTANCE = new UniLanguage();

    private UniLanguage() {
        super("EDK2 Uni");
    }
}
