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
import org.tianocore.edk2idea.Dec.psi.DecGuidEntry;
import org.tianocore.edk2idea.Dec.psi.DecGuidsSection;
import org.tianocore.edk2idea.Dec.psi.DecProtocolsSection;
import org.tianocore.edk2idea.Dec.psi.DecPpisSection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DecGuidIndex extends FileBasedIndexExtension<String, DecGuidData> {
    public static final ID<String, DecGuidData> NAME = ID.create("edk2.dec.guid.index");

    @NotNull
    @Override
    public ID<String, DecGuidData> getName() {
        return NAME;
    }

    @NotNull
    @Override
    public DataIndexer<String, DecGuidData, FileContent> getIndexer() {
        return new DataIndexer<String, DecGuidData, FileContent>() {
            @NotNull
            @Override
            public Map<String, DecGuidData> map(@NotNull FileContent inputData) {
                Map<String, DecGuidData> map = new HashMap<>();
                PsiFile psiFile = inputData.getPsiFile();
                if (psiFile instanceof DecFile) {

                    // Protocols
                    Collection<DecProtocolsSection> protocolsSections = PsiTreeUtil.findChildrenOfType(psiFile,
                            DecProtocolsSection.class);
                    for (DecProtocolsSection section : protocolsSections) {
                        for (DecGuidEntry entry : section.getGuidEntryList()) {
                            map.put(entry.getGuidName().getText(),
                                    new DecGuidData("Protocol", entry.getGuidValue().getText()));
                        }
                    }

                    // Guids
                    Collection<DecGuidsSection> guidsSections = PsiTreeUtil.findChildrenOfType(psiFile,
                            DecGuidsSection.class);
                    for (DecGuidsSection section : guidsSections) {
                        for (DecGuidEntry entry : section.getGuidEntryList()) {
                            map.put(entry.getGuidName().getText(),
                                    new DecGuidData("Guid", entry.getGuidValue().getText()));
                        }
                    }

                    // Ppis
                    Collection<DecPpisSection> ppisSections = PsiTreeUtil.findChildrenOfType(psiFile,
                            DecPpisSection.class);
                    for (DecPpisSection section : ppisSections) {
                        for (DecGuidEntry entry : section.getGuidEntryList()) {
                            map.put(entry.getGuidName().getText(),
                                    new DecGuidData("Ppi", entry.getGuidValue().getText()));
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
    public DataExternalizer<DecGuidData> getValueExternalizer() {
        return new DataExternalizer<DecGuidData>() {
            @Override
            public void save(@NotNull DataOutput out, DecGuidData value) throws IOException {
                out.writeUTF(value.type);
                out.writeUTF(value.value);
            }

            @Override
            public DecGuidData read(@NotNull DataInput in) throws IOException {
                return new DecGuidData(in.readUTF(), in.readUTF());
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
