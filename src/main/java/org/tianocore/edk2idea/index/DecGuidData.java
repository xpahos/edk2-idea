package org.tianocore.edk2idea.index;

public class DecGuidData {
    public final String type;
    public final String value;

    public DecGuidData(String type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DecGuidData that = (DecGuidData) o;
        return java.util.Objects.equals(type, that.type) &&
                java.util.Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, value);
    }
}
