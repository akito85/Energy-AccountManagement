package com.dbs.common.base.utils;

import javax.persistence.AttributeConverter;

public class BooleanTo10IntegerConverter2 implements AttributeConverter<Boolean, Integer> {

    /**
     * This implementation will return "Y" if the parameter is Boolean.TRUE,
     * otherwise it will return "N" when the parameter is Boolean.FALSE.
     * A null input value will yield a null return value.
     * @param b Boolean
     */
    @Override
    @SuppressWarnings("java:S2447")
    public Integer convertToDatabaseColumn(Boolean b) {
        if (b == null) {
            return null;
        }
        if (b.booleanValue()) {
            return 1;
        }
        return 0;
    }

    /**
     * This implementation will return Boolean.TRUE if the string
     * is "Y" or "y", otherwise it will ignore the value and return
     * Boolean.FALSE (it does not actually look for "N") for any
     * other non-null string. A null input value will yield a null
     * return value.
     * @param s String
     */
    @Override
    @SuppressWarnings("java:S2447")
    public Boolean convertToEntityAttribute(Integer s) {
        if (s == null) {
            return null;
        }
        if (s.equals(1)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
