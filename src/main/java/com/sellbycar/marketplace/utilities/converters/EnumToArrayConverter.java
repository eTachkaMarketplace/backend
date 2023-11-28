package com.sellbycar.marketplace.utilities.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface EnumToArrayConverter {

    @SuppressWarnings("unchecked")
    static <T extends Enum<T>> String[] convertEnumToArray(Class<T> enumType) {
        try {
            Method valuesMethod = enumType.getMethod("values");
            Enum<T>[] enumValues = (Enum<T>[]) valuesMethod.invoke(null);
            String[] valuesArray = new String[enumValues.length];

            for (int i = 0; i < enumValues.length; i++) {
                valuesArray[i] = enumValues[i].toString();
            }

            return valuesArray;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
