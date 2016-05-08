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

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In order to remove dependency on magic numbers in test, we often use just random values. But the problem is that we
 * need test that are generating same output no matter how many times we run them. So the real goal for test value is
 * to make it rather globally unique for whole build than just randomize each time.
 *
 * <p>This utility is using atomic as a provider for unique values, so, as result, each test is getting unique input</p>
 *
 * @since 1.0
 */
@UtilityClass
public class Unique {
    private static final long initMillis = System.currentTimeMillis();
    private static final AtomicLong idCounter = new AtomicLong();

    /**
     * Generate unique long. Uses plain output of atomic counter. Produces values greater than 0.
     *
     * @return unique long
     * @see AtomicLong#incrementAndGet()
     */
    @Nonnull
    public static Long uniqueLong() {
        return idCounter.incrementAndGet();
    }

    /**
     * Generate unique double. Uses two unique longs to produce integral and fractional parts. Produces values greater
     * than 0.
     *
     * @return unique double
     * @see Long#doubleValue()
     */
    @Nonnull
    public static Double uniqueDouble() {
        long value = uniqueLong();
        String stringValue = String.valueOf(value);

        StringBuilder fractionalBuilder = new StringBuilder(stringValue);
        // adding dot for fractional view
        fractionalBuilder.insert(0, ".");
        // append non zero ending for fractional part
        if (stringValue.endsWith("0")){
            fractionalBuilder.append("1");
        }
        fractionalBuilder.insert(0, value);
        return Double.parseDouble(fractionalBuilder.toString());
    }

    /**
     * Generate unique float from unique double. Produces values greater
     * than 0.
     *
     * @return unique float
     * @see Long#floatValue()
     */
    @Nonnull
    public static Float uniqueFloat() {
        return uniqueDouble().floatValue();
    }

    /**
     * Generate unique short. Using same approach as in hashcode generation to reduce collisions on casting from long
     * to short.
     *
     * @return unique short
     * @see Long#hashCode(long)
     */
    @Nonnull
    public static Short uniqueShort() {
        long value = uniqueLong();
        return (short) (value ^ (value >>> 48));
    }

    /**
     * Generate unique int. Uses {@link Long#hashCode(long) long hashcode function} to reduce collisions on casting from
     * long to int. Produces values greater than 0.
     *
     * @return unique int
     * @see Long#hashCode(long)
     */
    @Nonnull
    public static Integer uniqueInteger() {
        return Long.hashCode(uniqueLong());
    }

    /**
     * Generate unique string. Uses constant prefix with unique long suffix to product string values. Produces non-null
     * non empty values.
     *
     * @return unique string
     * @see Unique#uniqueLong()
     */
    @Nonnull
    public static String uniqueString() {
        return "testValue" + uniqueLong();
    }

    /**
     * Generate unique character as first char of string representation of unique long.
     *
     * @return unique char
     * @see Long#toString(long)
     * @see Unique#uniqueLong()
     */
    @Nonnull
    public static Character uniqueCharacter() {
        return Long.toString(uniqueLong())
                .charAt(0);
    }

    /**
     * Generate unique millis as a sum of constant millis, saved on class load, and unique long multiplied by 1000
     *
     * @return unique millis
     * @see Unique#initMillis
     */
    public static long uniqueMillis() {
        return initMillis + uniqueLong() * 1000;
    }

    /**
     * Generate unique date from unique millis.
     *
     * @return unique date
     * @see Unique#uniqueMillis()
     */
    @Nonnull
    public static Date uniqueDate() {
        return new Date(uniqueMillis());
    }

    /**
     * Generate unique instant from unique date value
     *
     * @return unique instant
     * @see Unique#uniqueDate()
     * @see Date#toInstant()
     */
    @Nonnull
    public static Instant uniqueInstant() {
        return uniqueDate().toInstant();
    }

    /**
     * Generate unique boolean from unique long.
     *
     * @return true if long value is even number, false otherwise
     */
    @Nonnull
    public static Boolean uniqueBoolean() {
        return uniqueLong() % 2 == 1;
    }

    /**
     * Generate unique byte. Using same approach as in hashcode generation to reduce collisions on casting from short
     * to byte.
     *
     * @return unique byte
     * @see Long#hashCode(long)
     * @see Unique#uniqueShort()
     */
    @Nonnull
    public static Byte uniqueByte() {
        short value = uniqueShort();
        return (byte) (value ^ (value >>> 8));
    }

    /**
     * Generate unique bigdecimal from unique long
     *
     * @return unique bigdecimal
     * @see BigDecimal#valueOf(long)
     */
    @Nonnull
    public static BigDecimal uniqueBigDecimal() {
        return BigDecimal.valueOf(uniqueLong());
    }
}
