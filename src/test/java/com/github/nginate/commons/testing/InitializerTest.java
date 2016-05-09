package com.github.nginate.commons.testing;

import com.google.common.reflect.TypeToken;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class InitializerTest {
    @Test
    public void generateSerializable() throws Exception {
        Serializable serializable = Initializer.uniqueObject(Serializable.class).generate();
        assertThat(serializable).isNotNull();
    }

    @Test
    public void generateStringClass() throws Exception {
        String string = Initializer.uniqueObject(String.class).generate();
        assertThat(string).isNotEmpty();
    }

    @Test
    public void generateCharSequenceInterface() throws Exception {
        CharSequence charSequence = Initializer.uniqueObject(CharSequence.class).generate();
        assertThat(charSequence).isNotEmpty();
    }

    @Test
    public void generateInteger() throws Exception {
        Integer integer = Initializer.uniqueObject(Integer.class).generate();
        assertThat(integer).isNotNull();
    }

    @Test
    public void generateInt() throws Exception {
        int value = Initializer.uniqueObject(int.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateLongObject() throws Exception {
        Long value = Initializer.uniqueObject(Long.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateLongPrimitive() throws Exception {
        long value = Initializer.uniqueObject(long.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateShortObject() throws Exception {
        Short value = Initializer.uniqueObject(Short.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateShortPrimitive() throws Exception {
        short value = Initializer.uniqueObject(short.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateDoubleObject() throws Exception {
        Double value = Initializer.uniqueObject(Double.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateDoublePrimitive() throws Exception {
        double value = Initializer.uniqueObject(double.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateFloatObject() throws Exception {
        Float value = Initializer.uniqueObject(Float.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateFloatPrimitive() throws Exception {
        float value = Initializer.uniqueObject(float.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateByteObject() throws Exception {
        Byte value = Initializer.uniqueObject(Byte.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateBytePrimitive() throws Exception {
        byte value = Initializer.uniqueObject(byte.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateCharacterObject() throws Exception {
        Character value = Initializer.uniqueObject(Character.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateCharPrimitive() throws Exception {
        char value = Initializer.uniqueObject(char.class).generate();
        assertThat(value).inUnicode();
    }

    @Test
    public void generateDate() throws Exception {
        Date date = Initializer.uniqueObject(Date.class).generate();
        assertThat(date).isNotNull();
    }

    @Test
    public void generateListOfSimpleObjects() throws Exception {
        int amount = 3;
        List<Integer> list = Initializer.uniqueList(Integer.class)
                .withCollectionSize(amount)
                .generate();

        assertThat(list).isNotNull().hasSize(amount).doesNotContainNull();
        list.forEach(v -> assertThat(v).isInstanceOf(Integer.class));
    }

    @Test
    public void generateSetOfSimpleObjects() throws Exception {
        int amount = 3;
        Set<Integer> set = Initializer.uniqueSet(Integer.class)
                .withCollectionSize(amount)
                .generate();

        assertThat(set).isNotNull().hasSize(amount).doesNotContainNull();
        set.forEach(v -> assertThat(v).isInstanceOf(Integer.class));
    }

    @Test
    public void generateQueueOfSimpleObjects() throws Exception {
        int amount = 3;
        Queue<Integer> queue = Initializer.uniqueQueue(Integer.class)
                .withCollectionSize(amount)
                .generate();

        assertThat(queue).isNotNull().hasSize(amount).doesNotContainNull();
        queue.forEach(v -> assertThat(v).isInstanceOf(Integer.class));
    }

    @Test
    public void generateMapOfSimpleObjects() throws Exception {
        int amount = 3;

        Map<Long, String> map = Initializer.uniqueMap(Long.class, String.class).withCollectionSize(amount).generate();
        assertThat(map).isNotNull().hasSize(amount);
        assertThat(map.keySet()).doesNotContainNull();
        assertThat(map.values()).doesNotContainNull();

        map.forEach((k, v) -> {
            assertThat(k).isInstanceOf(Long.class);
            assertThat(v).isInstanceOf(String.class);
        });
    }

    @Test
    public void generatePrimitiveArray() throws Exception {
        byte[] array = Initializer.uniqueObject(byte[].class).generate();
        assertThat(array).isNotEmpty();
    }

    @Test
    public void generateSimpleObjectArray() throws Exception {
        Short[] array = Initializer.uniqueObject(Short[].class).generate();
        assertThat(array).isNotEmpty();
    }

    @Test
    public void generateSimpleInterfaceArray() throws Exception {
        Serializable[] array = Initializer.uniqueObject(Serializable[].class).generate();
        assertThat(array).isNotEmpty();
    }

    @Test
    public void generateArrayOfParametrized() throws Exception {
        TypeToken<Map<Long, String>[]> token = new TypeToken<Map<Long, String>[]>() {};
        Map<Long, String>[] array = Initializer.uniqueObject(token).generate();
        assertThat(array).isNotEmpty();

        for (Map<Long, String> map : array) {
            map.forEach((k, v) -> {
                assertThat(k).isInstanceOf(Long.class);
                assertThat(v).isInstanceOf(String.class);
            });
        }
    }
}
