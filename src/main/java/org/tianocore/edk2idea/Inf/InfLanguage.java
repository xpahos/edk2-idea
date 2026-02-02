package org.tianocore.edk2idea.Inf;

public class InfLanguage extends com.intellij.lang.Language {

    public static final InfLanguage INSTANCE = new InfLanguage();

    private InfLanguage() {
        super("EDK2 Information Module");
    }

}