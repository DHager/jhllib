package com.technofovea.hllib;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Darien Hager
 */
class EnumConverter implements TypeConverter {

    private static final Logger logger = LoggerFactory.getLogger(EnumConverter.class);

    public Object fromNative(Object input, FromNativeContext context) {
        Integer i = (Integer) input;
        Class targetClass = context.getTargetType();
        if (!JnaEnum.class.isAssignableFrom(targetClass)) {
            return null;
        }
        Object[] enums = targetClass.getEnumConstants();
        if (enums.length == 0) {
            logger.error("Could not convert desired enum type (), no valid values are defined.",targetClass.getName());
            return null;
        }
        JnaEnum instance = (JnaEnum) enums[0];
        return instance.getForValue(i);

    }

    public Object toNative(Object input, ToNativeContext context) {
        JnaEnum j = (JnaEnum) input;
        return new Integer(j.getIntValue());
    }

    public Class nativeType() {
        return Integer.class;
    }
}
