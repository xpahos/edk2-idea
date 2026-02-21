package org.tianocore.edk2idea.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Dsc.psi.DscPcdEntry;
import org.tianocore.edk2idea.Dsc.psi.DscPcdSection;
import org.tianocore.edk2idea.index.DecPcdIndex;
import org.tianocore.edk2idea.index.DecPcdData;
import java.util.List;

public class DscPcdAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof DscPcdEntry) {
            DscPcdEntry entry = (DscPcdEntry) element;
            if (entry.getPcdName() == null)
                return;

            // Only check for FixedAtBuild redefinition inside [PcdsFixedAtBuild] DSC
            // sections
            DscPcdSection pcdSection = PsiTreeUtil.getParentOfType(entry, DscPcdSection.class);
            if (pcdSection == null)
                return;

            // Get the section header text, e.g. "[PcdsFixedAtBuild]" or
            // "[PcdsFixedAtBuild.common]"
            PsiElement firstChild = pcdSection.getFirstChild();
            if (firstChild == null)
                return;

            String sectionHeader = firstChild.getText();
            if (!sectionHeader.contains("FixedAtBuild"))
                return;

            String pcdName = entry.getPcdName().getText();
            Project project = element.getProject();

            GlobalSearchScope scope = GlobalSearchScope.allScope(project);
            List<DecPcdData> dataList = FileBasedIndex.getInstance().getValues(
                    DecPcdIndex.NAME, pcdName, scope);

            for (DecPcdData data : dataList) {
                if (data.sectionType != null && data.sectionType.contains("FixedAtBuild")) {
                    holder.newAnnotation(HighlightSeverity.ERROR, "FixedAtBuild variables cannot be redefined")
                            .range(entry.getPcdName())
                            .create();
                    break;
                }
            }
        }
    }
}
