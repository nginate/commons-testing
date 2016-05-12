package com.github.nginate.commons.testing;

import com.github.nginate.commons.testing.dto.*;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import org.assertj.core.util.Maps;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

import static com.github.nginate.commons.testing.Conditions.*;
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
        ObjectFieldsDto dto = uniqueObject(ObjectFieldsDto.class).generate();

        assertThat(dto).isNotNull()
                .has(nonNullIn(ObjectFieldsDto::getBigDecimalField))
                .has(nonNullIn(ObjectFieldsDto::getBigIntegerField))
                .has(nonNullIn(ObjectFieldsDto::getByteField))
                .has(nonNullIn(ObjectFieldsDto::getBooleanField))
                .has(nonNullIn(ObjectFieldsDto::getCharacterField))
                .has(nonNullIn(ObjectFieldsDto::getDateField))
                .has(nonNullIn(ObjectFieldsDto::getDoubleField))
                .has(nonNullIn(ObjectFieldsDto::getFloatField))
                .has(nonNullIn(ObjectFieldsDto::getInstantField))
                .has(nonNullIn(ObjectFieldsDto::getIntegerField))
                .has(nonNullIn(ObjectFieldsDto::getLongField))
                .has(nonNullIn(ObjectFieldsDto::getShortField))
                .has(nonNullIn(ObjectFieldsDto::getUuidField))
                .has(nonNullIn(ObjectFieldsDto::getStringField));
    }

    @Test
    public void testObjectWithMiscSimpleObjectArrayFields() throws Exception {
        int amount = 5;
        ObjectArraysFieldsDto dto = uniqueObject(ObjectArraysFieldsDto.class).withCollectionSize(amount).generate();

        assertThat(dto).isNotNull()
                .has(arraySize(amount, ObjectArraysFieldsDto::getBigDecimalField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getBigIntegerField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getByteField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getBooleanField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getCharacterField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getDateField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getDoubleField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getFloatField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getInstantField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getIntegerField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getLongField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getShortField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getUuidField))
                .has(arraySize(amount,ObjectArraysFieldsDto::getStringField));
    }

    @Test
    public void testObjectWithSimplePrimitiveFields() throws Exception {
        PrimitiveFieldsDto dto = uniqueObject(PrimitiveFieldsDto.class).generate();

        assertThat(dto).isNotNull()
                .has(notEqualTo(0, PrimitiveFieldsDto::getByteField))
                .has(notEqualTo('\u0000', PrimitiveFieldsDto::getCharacterField))
                .has(notEqualTo(0, PrimitiveFieldsDto::getDoubleField))
                .has(notEqualTo(0, PrimitiveFieldsDto::getFloatField))
                .has(notEqualTo(0, PrimitiveFieldsDto::getIntegerField))
                .has(notEqualTo(0, PrimitiveFieldsDto::getLongField))
                .has(notEqualTo(0, PrimitiveFieldsDto::getShortField));
    }

    @Test
    public void testObjectWithMiscPrimitiveArrayFields() throws Exception {
        int amount = 5;
        PrimitiveArrayFieldsDto dto = uniqueObject(PrimitiveArrayFieldsDto.class).withCollectionSize(amount).generate();

        assertThat(dto).isNotNull()
                .has(byteArraySize(amount, PrimitiveArrayFieldsDto::getByteField))
                .has(booleanArraySize(amount, PrimitiveArrayFieldsDto::getBooleanField))
                .has(charArraySize(amount, PrimitiveArrayFieldsDto::getCharacterField))
                .has(doubleArraySize(amount, PrimitiveArrayFieldsDto::getDoubleField))
                .has(floatArraySize(amount, PrimitiveArrayFieldsDto::getFloatField))
                .has(intArraySize(amount, PrimitiveArrayFieldsDto::getIntegerField))
                .has(longArraySize(amount, PrimitiveArrayFieldsDto::getLongField))
                .has(shortArraySize(amount, PrimitiveArrayFieldsDto::getShortField));
    }

    @Test
    public void testFileDto() throws Exception {
        FileDto fileDto = uniqueObject(FileDto.class).generate();
        assertThat(fileDto).isNotNull();
    }

    @Test
    public void testFileArrayDto() throws Exception {
        int amount = 5;
        FileArrayDto fileDto = uniqueObject(FileArrayDto.class).withCollectionSize(amount).generate();
        assertThat(fileDto).isNotNull().has(arraySize(amount, FileArrayDto::getFileField));
    }

    @Test
    public void testNonDefaultConstructor() throws Exception {
        NonDefaultConstructorDto dto = uniqueObject(NonDefaultConstructorDto.class).generate();
        assertThat(dto).isNotNull().has(nonNullIn(NonDefaultConstructorDto::getString));
    }

    @Test
    public void testSimpleObjectField() throws Exception {
        SimpleObjectFieldDto dto = uniqueObject(SimpleObjectFieldDto.class).generate();
        assertThat(dto).isNotNull().has(nonNullIn(SimpleObjectFieldDto::getObject));
    }

    @Test
    public void testSimpleObjectArrayField() throws Exception {
        int amount = 5;
        SimpleObjectArrayFieldDto dto = uniqueObject(SimpleObjectArrayFieldDto.class).withCollectionSize(amount)
                .generate();
        assertThat(dto).isNotNull().has(arraySize(amount, SimpleObjectArrayFieldDto::getObject));
    }

    @Test
    public void testFieldsExclusion() throws Exception {
        FileDto dto = uniqueObject(FileDto.class)
                .withExcludedFields(Maps.newHashMap(TypeToken.of(FileDto.class), Sets.newHashSet("fileField")))
                .generate();
        assertThat(dto).isNotNull().has(nullIn(FileDto::getFileField));
    }

    @Test
    public void testFieldExclusion() throws Exception {
        FileDto dto = uniqueObject(FileDto.class)
                .withExcludedFieldsFor(FileDto.class, "fileField")
                .generate();
        assertThat(dto).isNotNull().has(nullIn(FileDto::getFileField));
    }

    @Test
    public void testNumberField() throws Exception {
        NumberFieldDto dto = uniqueObject(NumberFieldDto.class).generate();
        assertThat(dto).isNotNull().has(positive(NumberFieldDto::getNumber));
    }

    @Test
    public void testCustomNumberFieldMapping() throws Exception {
        NumberFieldDto dto = uniqueObject(NumberFieldDto.class).withMapping(Number.class, Integer.class).generate();
        assertThat(dto).isNotNull().has(positive(NumberFieldDto::getNumber));
        assertThat(dto.getNumber()).isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testRecursiveGeneration() throws Exception {
        RecursiveDto dto = uniqueObject(RecursiveDto.class).generate();
        assertThat(dto).isNotNull().has(nonNullIn(RecursiveDto::getRecursiveDto));
    }
}
