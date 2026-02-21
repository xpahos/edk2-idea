package org.tianocore.edk2idea.index;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Dsc.DscFileType;
import org.tianocore.edk2idea.Dsc.psi.DscFile;
import org.tianocore.edk2idea.Dsc.psi.DscPcdEntry;
import org.tianocore.edk2idea.Dsc.psi.DscPcdSection;
import org.tianocore.edk2idea.Dsc.psi.DscNestedPcdSection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DscPcdIndex extends FileBasedIndexExtension<String, DscPcdData> {
    public static final ID<String, DscPcdData> NAME = ID.create("edk2.dsc.pcd.index");

    @NotNull
    @Override
    public ID<String, DscPcdData> getName() {
        return NAME;
    }

    @NotNull
    @Override
    public DataIndexer<String, DscPcdData, FileContent> getIndexer() {
        return new DataIndexer<String, DscPcdData, FileContent>() {
            @NotNull
            @Override
            public Map<String, DscPcdData> map(@NotNull FileContent inputData) {
                Map<String, DscPcdData> map = new HashMap<>();
                PsiFile psiFile = inputData.getPsiFile();
                if (psiFile instanceof DscFile) {
                    Collection<DscPcdEntry> entries = PsiTreeUtil.findChildrenOfType(psiFile, DscPcdEntry.class);
                    for (DscPcdEntry entry : entries) {
                        String name = entry.getPcdName().getText();
                        String value = entry.getText(); // full entry for simplicity
                        String sectionType = "Unknown";

                        DscPcdSection pcdSection = PsiTreeUtil.getParentOfType(entry, DscPcdSection.class);
                        if (pcdSection != null) {
                            sectionType = pcdSection.getFirstChild().getText();
                        } else {
                            DscNestedPcdSection nested = PsiTreeUtil.getParentOfType(entry, DscNestedPcdSection.class);
                            if (nested != null) {
                                sectionType = nested.getFirstChild().getText();
                            }
                        }

                        map.put(name, new DscPcdData(value, sectionType));
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
    public DataExternalizer<DscPcdData> getValueExternalizer() {
        return new DataExternalizer<DscPcdData>() {
            @Override
            public void save(@NotNull DataOutput out, DscPcdData value) throws IOException {
                out.writeUTF(value.value);
                out.writeUTF(value.sectionType);
            }

            @Override
            public DscPcdData read(@NotNull DataInput in) throws IOException {
                return new DscPcdData(in.readUTF(), in.readUTF());
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
        return new DefaultFileTypeSpecificInputFilter(DscFileType.INSTANCE);
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }
}
