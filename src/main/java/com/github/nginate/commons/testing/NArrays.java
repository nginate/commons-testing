package com.github.nginate.commons.testing;

import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Array utilities
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess")
@UtilityClass
public class NArrays {
    /**
     * Add value to generic array
     *
     * @param array non-null array
     * @param index the index into the array
     * @param entry the new value of the indexed component
     * @param type  type class
     * @param <T>   array type
     */
    public static <T> void addElement(Object array, int index, T entry, Class<T> type) {
        if (int.class.isAssignableFrom(type)) {
            Array.setInt(array, index, (Integer) entry);
        } else if (long.class.isAssignableFrom(type)) {
            Array.setLong(array, index, (Long) entry);
        } else if (double.class.isAssignableFrom(type)) {
            Array.setDouble(array, index, (Double) entry);
        } else if (short.class.isAssignableFrom(type)) {
            Array.setShort(array, index, (Short) entry);
        } else if (char.class.isAssignableFrom(type)) {
            Array.setChar(array, index, (Character) entry);
        } else if (boolean.class.isAssignableFrom(type)) {
            Array.setBoolean(array, index, (Boolean) entry);
        } else if (byte.class.isAssignableFrom(type)) {
            Array.setByte(array, index, (Byte) entry);
        } else if (float.class.isAssignableFrom(type)) {
            Array.setFloat(array, index, (Float) entry);
        } else {
            Array.set(array, index, entry);
        }
    }

    public static <T> Object generateArray(Class<T> type, int size, Function<TypeToken<T>, T> uniqueValueProvider) {
        return generateArray(TypeToken.of(type), size, uniqueValueProvider);
    }

    /**
     * Create array with unique objects of provided type
     *
     * @param type                array element type
     * @param size                array size
     * @param uniqueValueProvider unique value generator
     * @param <T>                 array type
     * @return array of given size, filled with unique non-null objects
     */
    @SuppressWarnings("unchecked")
    public static <T> Object generateArray(TypeToken<?> type, int size, Function<TypeToken<T>, T>
            uniqueValueProvider) {
        TypeToken<T> token = (TypeToken<T>) type.getComponentType();
        Class<T> elementClass = (Class<T>) token.getRawType();
        Object o = Array.newInstance(elementClass, size);
        IntStream.range(0, size).forEach(index -> addElement(o, index, uniqueValueProvider.apply(token), elementClass));
        return o;
    }

    /**
     * Set array to an object's field
     *
     * @param instance test object
     * @param field    test object's field
     * @param array    non-null array
     * @see Field#setAccessible(boolean)
     * @see Field#set(Object, Object)
     */
    public static void setArrayField(Object instance, Field field, Object[] array) {
        try {
            field.setAccessible(true);
            field.set(instance, array);
        } catch (IllegalAccessException e) {
            throw new ObjectInitializationException(e.getMessage(), e);
        }
    }
}
