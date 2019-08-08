package com.lambda.server.handler;

public class CodeEnumUtil {
	
    /**
     * @param enumClass
     * @param code
     * @param <E>
     */
    public static <E extends Enum<?> & CodeBaseEnum> E codeOf(Class<E> enumClass, String code) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            if (e.name().equals(code))
                return e;
        }
        return null;
    }
}