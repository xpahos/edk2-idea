package org.tianocore.edk2idea.index;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Dec.DecFileType;
import org.tianocore.edk2idea.Dec.psi.DecFile;
import org.tianocore.edk2idea.Dec.psi.DecPcdEntry;
import org.tianocore.edk2idea.Dec.psi.DecPcdsSection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DecPcdIndex extends FileBasedIndexExtension<String, DecPcdData> {
    public static final ID<String, DecPcdData> NAME = ID.create("edk2.dec.pcd.index");

    @NotNull
    @Override
    public ID<String, DecPcdData> getName() {
        return NAME;
    }

    @NotNull
    @Override
    public DataIndexer<String, DecPcdData, FileContent> getIndexer() {
        return new DataIndexer<String, DecPcdData, FileContent>() {
            @NotNull
            @Override
            public Map<String, DecPcdData> map(@NotNull FileContent inputData) {
                Map<String, DecPcdData> map = new HashMap<>();
                PsiFile psiFile = inputData.getPsiFile();
                if (psiFile instanceof DecFile) {
                    Collection<DecPcdsSection> sections = PsiTreeUtil.findChildrenOfType(psiFile, DecPcdsSection.class);
                    for (DecPcdsSection section : sections) {
                        String sectionType = "Unknown";
                        // Extract section type from PCD_SECTION_HEADER text, e.g.
                        // "[PcdsFixedAtBuild.common]"
                        String headerText = section.getFirstChild().getText();
                        if (headerText.startsWith("[") && headerText.endsWith("]")) {
                            String inner = headerText.substring(1, headerText.length() - 1);
                            // Strip arch modifiers after first dot
                            int dotIdx = inner.indexOf('.');
                            sectionType = (dotIdx > 0) ? inner.substring(0, dotIdx) : inner;
                        }

                        for (DecPcdEntry entry : section.getPcdEntryList()) {
                            String name = entry.getPcdName().getText();
                            String type = entry.getPcdType().getText();
                            String value = entry.getPcdValue().getText();
                            String ui = entry.getPcdToken().getText();
                            map.put(name, new DecPcdData(type, value, ui, sectionType));
                        }
                    }
                }
                return map;
            }
        };
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @NotNull
    @Override
    public DataExternalizer<DecPcdData> getValueExternalizer() {
        return new DataExternalizer<DecPcdData>() {
            @Override
            public void save(@NotNull DataOutput out, DecPcdData value) throws IOException {
                out.writeUTF(value.type);
                out.writeUTF(value.value);
                out.writeUTF(value.uniqueId);
                out.writeUTF(value.sectionType);
            }

            @Override
            public DecPcdData read(@NotNull DataInput in) throws IOException {
                return new DecPcdData(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF());
            }
        };
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return new DefaultFileTypeSpecificInputFilter(DecFileType.INSTANCE);
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }
}
