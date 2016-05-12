/**
 * Copyright © 2016
 * Maksim Lozbin <maksmtua@gmail.com>
 * Oleksii Ihnachuk <legioner.alexei@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.github.nginate.commons.testing;

import lombok.experimental.UtilityClass;
import org.assertj.core.api.Condition;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.google.common.collect.Iterables.isEmpty;

/**
 * Conditions for assertJ to use with parent object without extracting actual field value that would break assertion
 * chain
 * <p>Example:</p>
 * <pre>
 *  {@code
 *     assertThat(obj.getField1()).isNotEmpty();
 *     assertThat(obj.getField2()).isNull();
 *
 *     assertThat(obj)
 *      .has(empty(Obj::getField1))
 *      .has(nullIn(Obj::getField2));
 *  }
 * </pre>
 *
 * @since 1.0
 */
@UtilityClass
public class Conditions {

    /**
     * Expect object to have same field value as other object of same type
     *
     * @param expected       object to compare to
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @param <F>            field type
     * @return true if both objects are not null and have equal field values
     */
    public static <T, F> Condition<T> sameAs(T expected, Function<T, F> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T actual) {
                return actual != null && expected != null &&
                        Objects.equals(fieldExtractor.apply(actual), fieldExtractor.apply(expected));
            }
        };
    }

    /**
     * Expect object to have non null field value
     *
     * @param fieldProvider pointer to an object field
     * @param <T>           object type
     * @return true if field value is not null
     */
    public static <T> Condition<T> nonNullIn(Function<T, Object> fieldProvider) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                return Objects.nonNull(fieldProvider.apply(value));
            }
        };
    }

    /**
     * Expect object to have null field
     *
     * @param fieldProvider pointer to an object field
     * @param <T>           object type
     * @return true if field value is null
     */
    public static <T> Condition<T> nullIn(Function<T, Object> fieldProvider) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                return Objects.isNull(fieldProvider.apply(value));
            }
        };
    }

    /**
     * Expect object's field to be equal to a provided value
     *
     * @param expected       expected value
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @param <F>            field type
     * @return true if provided value is not null and is equal to object's field value
     */
    public static <T, F> Condition<T> equalTo(F expected, Function<T, F> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                return value != null && Objects.equals(fieldExtractor.apply(value), expected);
            }
        };
    }

    /**
     * Expect object's field to be not equal to a provided value
     *
     * @param expected       not expected value
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @param <F>            field type
     * @return true if provided value is not null and is not equal to object's field value
     */
    public static <T, F> Condition<T> notEqualTo(F expected, Function<T, F> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                return value != null && !Objects.equals(fieldExtractor.apply(value), expected);
            }
        };
    }

    /**
     * Expect object's field to be a collection containing provided element
     *
     * @param expected       expected element
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @param <F>            collection type
     * @return true if collection is not null and is not empty and contains expected element
     */
    public static <T, F> Condition<T> collectionContaining(F expected, Function<T, Collection<F>> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                Collection<F> collection = fieldExtractor.apply(value);
                return value != null && !isEmpty(collection) && collection.contains(expected);
            }
        };
    }

    /**
     * Expect object's field to have empty collection
     *
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @param <F>            collection type
     * @return true if collection is not null and is empty
     */
    public static <T, F> Condition<T> empty(Function<T, Collection<F>> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                Collection<F> collection = fieldExtractor.apply(value);
                return collection != null && collection.isEmpty();
            }
        };
    }

    /**
     * Expect object to have collection of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @param <F>            collection type
     * @return true if collection is not null and has expected size
     */
    public static <T, F> Condition<T> hasSize(int size, Function<T, Collection<F>> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                Collection<F> collection = fieldExtractor.apply(value);
                return collection != null && collection.size() == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @param <A>            array type
     * @return true if array is not null and has expected size
     */
    public static <T, A> Condition<T> arraySize(int size, Function<T, A[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                A[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if array is not null and has expected size
     */
    public static <T> Condition<T> byteArraySize(int size, Function<T, byte[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                byte[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if array is not null and has expected size
     */
    public static <T> Condition<T> booleanArraySize(int size, Function<T, boolean[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                boolean[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if array is not null and has expected size
     */
    public static <T> Condition<T> charArraySize(int size, Function<T, char[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                char[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if array is not null and has expected size
     */
    public static <T> Condition<T> doubleArraySize(int size, Function<T, double[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                double[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if array is not null and has expected size
     */
    public static <T> Condition<T> floatArraySize(int size, Function<T, float[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                float[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if array is not null and has expected size
     */
    public static <T> Condition<T> intArraySize(int size, Function<T, int[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                int[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if array is not null and has expected size
     */
    public static <T> Condition<T> longArraySize(int size, Function<T, long[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                long[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object to have array of a given size
     *
     * @param size           expected size
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if array is not null and has expected size
     */
    public static <T> Condition<T> shortArraySize(int size, Function<T, short[]> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                short[] array = fieldExtractor.apply(value);
                return array != null && array.length == size;
            }
        };
    }

    /**
     * Expect object's field to have empty map
     *
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @param <K>            map key type
     * @param <V>            map value type
     * @return true is map is not null and is empty
     */
    public static <T, K, V> Condition<T> emptyMap(Function<T, Map<K, V>> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                Map<K, V> map = fieldExtractor.apply(value);
                return map != null && map.isEmpty();
            }
        };
    }

    /**
     * Expect object to have positive number (casting it to double)
     *
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if number is not null and is greater than zero
     */
    public static <T> Condition<T> positive(Function<T, Number> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                Number field = fieldExtractor.apply(value);
                return field != null && field.doubleValue() > 0.0;
            }
        };
    }

    /**
     * Expect object to have not empty string field
     *
     * @param fieldExtractor pointer to an object field
     * @param <T>            object type
     * @return true if string is not null and is not empty
     */
    public static <T> Condition<T> notEmpty(Function<T, CharSequence> fieldExtractor) {
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                CharSequence field = fieldExtractor.apply(value);
                return field != null && field.length() > 0;
            }
        };
    }
}
