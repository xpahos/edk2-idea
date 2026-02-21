package org.tianocore.edk2idea.index;

public class DscPcdData {
    public final String value;
    public final String sectionType;

    public DscPcdData(String value, String sectionType) {
        this.value = value;
        this.sectionType = sectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DscPcdData that = (DscPcdData) o;
        return java.util.Objects.equals(value, that.value) &&
                java.util.Objects.equals(sectionType, that.sectionType);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(value, sectionType);
    }
}
