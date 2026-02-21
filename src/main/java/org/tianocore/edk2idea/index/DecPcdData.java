package org.tianocore.edk2idea.index;

public class DecPcdData {
    public final String type;
    public final String value;
    public final String uniqueId;
    public final String sectionType;

    public DecPcdData(String type, String value, String uniqueId, String sectionType) {
        this.type = type;
        this.value = value;
        this.uniqueId = uniqueId;
        this.sectionType = sectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DecPcdData that = (DecPcdData) o;
        return java.util.Objects.equals(type, that.type) &&
                java.util.Objects.equals(value, that.value) &&
                java.util.Objects.equals(uniqueId, that.uniqueId) &&
                java.util.Objects.equals(sectionType, that.sectionType);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, value, uniqueId, sectionType);
    }
}
