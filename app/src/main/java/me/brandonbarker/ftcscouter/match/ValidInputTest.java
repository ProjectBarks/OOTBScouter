package me.brandonbarker.ftcscouter.match;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = {"m"})
public abstract class ValidInputTest {
    @Getter
    protected Class<?> mType;
    @Getter
    private boolean mNullPermitted;

    public ValidInputTest(Class<?> classType) {
        this(false, classType);
    }

    public ValidInputTest(boolean nullPermitted, Class<?> classType) {
        if (classType.isPrimitive()) {
            throw new ClassCastException("Class cannot be a primitive");
        }
        this.mType = classType;
        this.mNullPermitted = nullPermitted;
    }

    protected abstract boolean isValidSecondary(Object input);

    protected boolean isValid(Object input) {
        return !(!this.mNullPermitted && input == null)
                && !(this.mType != null && !this.mType.isInstance(input))
                && isValidSecondary(input);
    }
}
