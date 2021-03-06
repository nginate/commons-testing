/**
 * Copyright © 2016
 * Maksim Lozbin <maksmtua@gmail.com>
 * Oleksii Ihnachuk <legioner.alexei@gmail.com>
 * <p>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.github.nginate.commons.testing;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.overlay;

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
    private static final Iterator<Character> randomCharGenerator =
            getCharsStream(Pair.of('a', 'z'), Pair.of('A', 'Z'), Pair.of('0', '9')).iterator();
    private static final String UUID_TEMPLATE = "00000000000000000000000000000000";

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
        if (stringValue.endsWith("0")) {
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
     * Generate almost unique character from latin chars and digits.
     *
     * @return unique char
     */
    @Nonnull
    public static Character uniqueCharacter() {
        return randomCharGenerator.next();
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

    /**
     * Generate unique big integer from unique long
     *
     * @return unique big integer
     * @see BigInteger#valueOf(long)
     */
    @Nonnull
    public static BigInteger uniqueBigInteger() {
        return BigInteger.valueOf(uniqueLong());
    }

    /**
     * Generate unique UUID
     * @return unique UUID
     */
    @Nonnull
    public static UUID uniqueUUID() {
        String seed = uniqueLong().toString();
        String uuid = overlay(UUID_TEMPLATE, seed, UUID_TEMPLATE.length() - seed.length(), UUID_TEMPLATE.length());
        return UUID.fromString(new StringBuilder(uuid)
                .insert(8, '-')
                .insert(13, '-')
                .insert(18, '-')
                .insert(23, '-')
                .toString());
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static Stream<Character> getCharsStream(Pair<Character, Character>... fromToInclusivePairs) {
        int[] validChars = Arrays.stream(fromToInclusivePairs)
                .flatMapToInt(fromTo -> IntStream.rangeClosed(fromTo.getLeft(), fromTo.getRight()))
                .toArray();
        return IntStream.iterate(0, i -> i + 1)
                .map(i -> validChars[i - validChars.length * (i / validChars.length)])
                .mapToObj(charCode -> (char) charCode);
    }
}
