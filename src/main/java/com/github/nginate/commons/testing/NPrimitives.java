package com.github.nginate.commons.testing;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.github.nginate.commons.testing.Unique.*;

/**
 * Utility to generate or|and set primitive type fields for test objects
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess")
@UtilityClass
public class NPrimitives {
    private static final Map<Class<?>, Supplier<Object>> PRIMITIVE_GENERATORS = new HashMap<>();

    static {
        PRIMITIVE_GENERATORS.put(int.class, Unique::uniqueInteger);
        PRIMITIVE_GENERATORS.put(long.class, Unique::uniqueLong);
        PRIMITIVE_GENERATORS.put(double.class, Unique::uniqueDouble);
        PRIMITIVE_GENERATORS.put(short.class, Unique::uniqueShort);
        PRIMITIVE_GENERATORS.put(char.class, Unique::uniqueCharacter);
        PRIMITIVE_GENERATORS.put(boolean.class, Unique::uniqueBoolean);
        PRIMITIVE_GENERATORS.put(float.class, Unique::uniqueFloat);
        PRIMITIVE_GENERATORS.put(byte.class, Unique::uniqueByte);
    }

    /**
     * Create unique primitive for provided type
     *
     * @param primitiveType class of primitive type
     * @param <T>           primitive type
     * @return unique primitive
     * @throws ObjectInitializationException if type is not supported
     * @see Unique
     */
    @SuppressWarnings("unchecked")
    public static <T> T createUnique(Class<T> primitiveType) {
        return (T) PRIMITIVE_GENERATORS.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAssignableFrom(primitiveType))
                .findAny()
                .orElseThrow(() ->
                        new ObjectInitializationException("Unsupported primitive field type : " + primitiveType))
                .getValue()
                .get();
    }

    /**
     * In order to set primitive value to object's field we need to use different reflective calls, so, all these if
     * clauses are hidden here and all you need to do - is to provide test instance and field to initialize.
     *
     * @param instance       test object
     * @param primitiveField field to initialize
     * @throws ObjectInitializationException if type is not supported or there is no access
     */
    public static void setField(Object instance, Field primitiveField) {
        try {
            primitiveField.setAccessible(true);
            Class<?> type = primitiveField.getType();
            if (int.class.isAssignableFrom(type)) {
                primitiveField.setInt(instance, uniqueInteger());
            } else if (long.class.isAssignableFrom(type)) {
                primitiveField.setLong(instance, uniqueLong());
            } else if (double.class.isAssignableFrom(type)) {
                primitiveField.setDouble(instance, uniqueDouble());
            } else if (short.class.isAssignableFrom(type)) {
                primitiveField.setShort(instance, uniqueShort());
            } else if (char.class.isAssignableFrom(type)) {
                primitiveField.setChar(instance, uniqueCharacter());
            } else if (boolean.class.isAssignableFrom(type)) {
                primitiveField.setBoolean(instance, uniqueBoolean());
            } else if (byte.class.isAssignableFrom(type)) {
                primitiveField.setByte(instance, uniqueByte());
            } else if (float.class.isAssignableFrom(type)) {
                primitiveField.setFloat(instance, uniqueFloat());
            } else {
                throw new ObjectInitializationException("Unsupported primitive field type : " + type);
            }
        } catch (IllegalAccessException e) {
            throw new ObjectInitializationException(e.getMessage(), e);
        }
    }
}
