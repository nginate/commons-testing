package com.github.nginate.commons.testing;

import com.github.nginate.commons.testing.dto.BinArrayDto;
import com.github.nginate.commons.testing.dto.FileDto;
import com.github.nginate.commons.testing.dto.MiscSimpleFieldsDto;
import com.github.nginate.commons.testing.dto.NonDefaultConstructorDto;
import com.google.common.reflect.TypeToken;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

import static com.github.nginate.commons.testing.Conditions.nonNullIn;
import static com.github.nginate.commons.testing.Initializer.uniqueObject;
import static org.assertj.core.api.Assertions.assertThat;

public class InitializerTest {
    @Test
    public void generateSerializable() throws Exception {
        Serializable serializable = uniqueObject(Serializable.class).generate();
        assertThat(serializable).isNotNull();
    }

    @Test
    public void generateStringClass() throws Exception {
        String string = uniqueObject(String.class).generate();
        assertThat(string).isNotEmpty();
    }

    @Test
    public void generateCharSequenceInterface() throws Exception {
        CharSequence charSequence = uniqueObject(CharSequence.class).generate();
        assertThat(charSequence).isNotEmpty();
    }

    @Test
    public void generateInteger() throws Exception {
        Integer integer = uniqueObject(Integer.class).generate();
        assertThat(integer).isNotNull();
    }

    @Test
    public void generateInt() throws Exception {
        int value = uniqueObject(int.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateLongObject() throws Exception {
        Long value = uniqueObject(Long.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateLongPrimitive() throws Exception {
        long value = uniqueObject(long.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateShortObject() throws Exception {
        Short value = uniqueObject(Short.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateShortPrimitive() throws Exception {
        short value = uniqueObject(short.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateDoubleObject() throws Exception {
        Double value = uniqueObject(Double.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateDoublePrimitive() throws Exception {
        double value = uniqueObject(double.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateFloatObject() throws Exception {
        Float value = uniqueObject(Float.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateFloatPrimitive() throws Exception {
        float value = uniqueObject(float.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateByteObject() throws Exception {
        Byte value = uniqueObject(Byte.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateBytePrimitive() throws Exception {
        byte value = uniqueObject(byte.class).generate();
        assertThat(value).isNotZero();
    }

    @Test
    public void generateCharacterObject() throws Exception {
        Character value = uniqueObject(Character.class).generate();
        assertThat(value).isNotNull();
    }

    @Test
    public void generateCharPrimitive() throws Exception {
        char value = uniqueObject(char.class).generate();
        assertThat(value).inUnicode();
    }

    @Test
    public void generateDate() throws Exception {
        Date date = uniqueObject(Date.class).generate();
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
        byte[] array = uniqueObject(byte[].class).generate();
        assertThat(array).isNotEmpty();
    }

    @Test
    public void generateSimpleObjectArray() throws Exception {
        Short[] array = uniqueObject(Short[].class).generate();
        assertThat(array).isNotEmpty();
    }

    @Test
    public void generateSimpleInterfaceArray() throws Exception {
        Serializable[] array = uniqueObject(Serializable[].class).generate();
        assertThat(array).isNotEmpty();
    }

    @Test
    public void generateArrayOfParametrized() throws Exception {
        TypeToken<Map<Long, String>[]> token = new TypeToken<Map<Long, String>[]>() {
        };
        Map<Long, String>[] array = uniqueObject(token).generate();
        assertThat(array).isNotEmpty();

        for (Map<Long, String> map : array) {
            map.forEach((k, v) -> {
                assertThat(k).isInstanceOf(Long.class);
                assertThat(v).isInstanceOf(String.class);
            });
        }
    }

    @Test
    public void testBinArrayField() throws Exception {
        BinArrayDto dto = uniqueObject(BinArrayDto.class).generate();
        assertThat(dto).isNotNull();
        assertThat(dto.getBinArray()).isNotNull().isNotEmpty().doesNotHaveDuplicates();
    }

    @Test
    public void testObjectWithMiscSimpleObjectFields() throws Exception {
        MiscSimpleFieldsDto dto = uniqueObject(MiscSimpleFieldsDto.class).generate();

        assertThat(dto).isNotNull()
                .has(nonNullIn(MiscSimpleFieldsDto::getBigDecimalField))
                .has(nonNullIn(MiscSimpleFieldsDto::getBigIntegerField))
                .has(nonNullIn(MiscSimpleFieldsDto::getByteField))
                .has(nonNullIn(MiscSimpleFieldsDto::getCharacterField))
                .has(nonNullIn(MiscSimpleFieldsDto::getDateField))
                .has(nonNullIn(MiscSimpleFieldsDto::getDoubleField))
                .has(nonNullIn(MiscSimpleFieldsDto::getFloatField))
                .has(nonNullIn(MiscSimpleFieldsDto::getInstantField))
                .has(nonNullIn(MiscSimpleFieldsDto::getIntegerField))
                .has(nonNullIn(MiscSimpleFieldsDto::getLongField))
                .has(nonNullIn(MiscSimpleFieldsDto::getShortField))
                .has(nonNullIn(MiscSimpleFieldsDto::getUuidField))
                .has(nonNullIn(MiscSimpleFieldsDto::getStringField));
    }

    @Test
    public void testFileDto() throws Exception {
        FileDto fileDto = uniqueObject(FileDto.class).generate();
        assertThat(fileDto).isNotNull();
    }

    @Test
    public void testNonDefaultConstructor() throws Exception {
        NonDefaultConstructorDto dto = uniqueObject(NonDefaultConstructorDto.class).generate();
        assertThat(dto).isNotNull().has(nonNullIn(NonDefaultConstructorDto::getString));
    }
}
