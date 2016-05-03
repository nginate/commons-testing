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
     * @return true if provided value is not not and is equal to object's field value
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
