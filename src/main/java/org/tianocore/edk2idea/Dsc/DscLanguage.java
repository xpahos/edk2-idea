package org.tianocore.edk2idea.Dsc;

import com.intellij.lang.Language;

public class DscLanguage extends Language {

    public static final DscLanguage INSTANCE = new DscLanguage();

    private DscLanguage() {
        super("EDK2 Description Module");
    }
}