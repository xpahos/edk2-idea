package org.tianocore.edk2idea.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.tianocore.edk2idea.Dec.DecSyntaxHighlighter;
import org.tianocore.edk2idea.Dec.psi.DecGuidName;
import org.tianocore.edk2idea.Dec.psi.DecGuidsSection;
import org.tianocore.edk2idea.Dec.psi.DecPpisSection;
import org.tianocore.edk2idea.Dec.psi.DecProtocolsSection;
import org.tianocore.edk2idea.Inf.InfSyntaxHighlighter;
import org.tianocore.edk2idea.Inf.psi.InfGuidName;
import org.tianocore.edk2idea.Inf.psi.InfGuidsSection;
import org.tianocore.edk2idea.Inf.psi.InfPpisSection;
import org.tianocore.edk2idea.Inf.psi.InfProtocolsSection;

public class Edk2GuidAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof DecGuidName) {
            TextAttributesKey attributesKey = null;
            if (PsiTreeUtil.getParentOfType(element, DecProtocolsSection.class) != null) {
                attributesKey = DecSyntaxHighlighter.PROTOCOL_NAME;
            } else if (PsiTreeUtil.getParentOfType(element, DecPpisSection.class) != null) {
                attributesKey = DecSyntaxHighlighter.PPI_NAME;
            } else if (PsiTreeUtil.getParentOfType(element, DecGuidsSection.class) != null) {
                attributesKey = DecSyntaxHighlighter.GUID_NAME;
            }
            if (attributesKey != null) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(element.getTextRange())
                        .textAttributes(attributesKey)
                        .create();
            }
        } else if (element instanceof InfGuidName) {
            TextAttributesKey attributesKey = null;
            if (PsiTreeUtil.getParentOfType(element, InfProtocolsSection.class) != null) {
                attributesKey = InfSyntaxHighlighter.PROTOCOL_NAME;
            } else if (PsiTreeUtil.getParentOfType(element, InfPpisSection.class) != null) {
                attributesKey = InfSyntaxHighlighter.PPI_NAME;
            } else if (PsiTreeUtil.getParentOfType(element, InfGuidsSection.class) != null) {
                attributesKey = InfSyntaxHighlighter.GUID_NAME;
            }
            if (attributesKey != null) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(element.getTextRange())
                        .textAttributes(attributesKey)
                        .create();
            }
        }
    }
}
