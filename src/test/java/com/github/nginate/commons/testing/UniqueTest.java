package com.github.nginate.commons.testing;

import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.github.nginate.commons.testing.Unique.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class UniqueTest {

    @Test
    public void testUniqueLong() throws Exception {
        Long value = uniqueLong();
        assertThat(value).isNotNull().isPositive();

        int amount = 30;

        List<Long> values = IntStream.range(0, amount).parallel().mapToObj(value1 -> uniqueLong()).collect(toList());
        assertThat(values).hasSize(30).doesNotHaveDuplicates().doesNotContain(value);
    }

    @Test
    public void testUniqueDouble() throws Exception {
        assertThat(uniqueDouble()).isNotNull().isPositive();
    }

    @Test
    public void checkFractionalAppendIfZeroTrailing() throws Exception {
        while (!uniqueLong().toString().endsWith("9")) {
            // waiting for proper counter state
        }

        Double actual = uniqueDouble();
        assertThat(actual).isNotNull().isPositive();
        assertThat(actual.toString()).endsWith("1");
    }

    @Test
    public void checkFractionalPrediction() throws Exception {
        Integer intCondition = 1;
        while (!uniqueLong().toString().endsWith(intCondition.toString())) {
            // waiting for proper counter state
        }

        Double actual = uniqueDouble();
        assertThat(actual).isNotNull().isPositive();
        assertThat(actual.toString()).endsWith(String.valueOf(intCondition + 1));
    }

    @Test
    public void testUniqueFloat() throws Exception {
        assertThat(uniqueFloat()).isNotNull().isPositive();
    }

    @Test
    public void testUniqueShort() throws Exception {
        assertThat(uniqueShort()).isNotNull();
    }

    @Test
    public void checkShortPrediction() throws Exception {
        while (uniqueLong() <= Short.MAX_VALUE) {
            // exhausting short values to prevent simple cast
        }
        Long uniqueLong = uniqueLong() + 1;
        Short uniqueShort = uniqueShort();

        short calculated = (short) (uniqueLong ^ (uniqueLong >>> 48));
        assertThat(uniqueShort).isEqualTo(calculated);
    }

    @Test
    public void testUniqueInteger() throws Exception {
        assertThat(uniqueInteger()).isNotNull().isPositive();
    }

    @Test
    public void testUniqueString() throws Exception {
        assertThat(uniqueString()).isNotNull().isNotEmpty();
    }

    @Test
    public void testUniqueCharacter() throws Exception {
        assertThat(uniqueCharacter()).isNotNull();
    }

    @Test
    public void testUniqueMillis() throws Exception {
        assertThat(uniqueMillis()).isPositive();
    }

    @Test
    public void checkMillisPrediction() throws Exception {
        long millis = uniqueMillis();
        long millis1 = uniqueMillis();

        assertThat(millis1).isGreaterThan(millis);
        assertThat(millis1 - millis).isEqualTo(1000);
    }

    @Test
    public void testUniqueDate() throws Exception {
        assertThat(uniqueDate()).isNotNull();
    }

    @Test
    public void checkDatePrediction() throws Exception {
        Date date1 = uniqueDate();
        Date date2 = uniqueDate();

        assertThat(date2).isAfter(date1);
        assertThat(date2.getTime() - date1.getTime()).isEqualTo(1000);
    }

    @Test
    public void testUniqueInstant() throws Exception {
        assertThat(uniqueInstant()).isNotNull();
    }

    @Test
    public void testUniqueBoolean() throws Exception {
        assertThat(uniqueBoolean()).isNotNull();
    }

    @Test
    public void checkBooleanChangePrediction() throws Exception {
        Boolean uniqueBoolean = uniqueBoolean();
        Boolean uniqueBoolean1 = uniqueBoolean();

        assertThat(uniqueBoolean).isNotEqualTo(uniqueBoolean1);
    }

    @Test
    public void checkBooleanGenerationDependsOnEvenNumber() throws Exception {
        Long uniqueLong = uniqueLong();

        boolean nextBooleanValue = uniqueLong % 2 != 1;
        assertThat(uniqueBoolean()).isEqualTo(nextBooleanValue);
    }

    @Test
    public void testUniqueByte() throws Exception {
        assertThat(uniqueByte()).isNotNull();
    }

    @Test
    public void checkBytePrediction() throws Exception {
        while (uniqueShort() <= Byte.MAX_VALUE) {
            // exhausting byte values to prevent simple casts
        }
        Short value = (short) (uniqueShort() + 1);
        Byte uniqueByte = uniqueByte();

        Byte calculated = (byte) (value ^ (value >>> 8));
        assertThat(uniqueByte).isEqualTo(calculated);
    }

    @Test
    public void testUniqueBigDecimal() throws Exception {
        assertThat(uniqueBigDecimal()).isNotNull().isPositive();
    }

    @Test
    public void testUniqueBigInteger() throws Exception {
        assertThat(uniqueBigInteger()).isNotNull();
    }

    @Test
    public void testUniqueUUID() throws Exception {
        assertThat(uniqueUUID()).isNotNull();
    }

    @Test
    public void testUniqueUUIDDoesNotRepeat() throws Exception {
        UUID uuid = uniqueUUID();
        assertThat(uniqueUUID()).isNotEqualTo(uuid);
    }
}
