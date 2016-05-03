package com.github.nginate.commons.testing;

import lombok.Builder;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.nginate.commons.testing.Conditions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ConditionsTest {
    private TestObject.TestObjectBuilder prototype;

    @Before
    public void setUp() throws Exception {
        prototype = TestObject.builder()
                .string("string")
                .integer(0)
                .list(Collections.emptyList())
                .map(Collections.emptyMap());
    }

    @Test
    public void testSameAs() throws Exception {
        TestObject actual = prototype.build();
        TestObject expected = prototype.build();

        assertThat(actual).has(sameAs(expected, TestObject::getString));
        assertThat(actual.getString()).isEqualTo(expected.getString());
    }

    @Test
    public void testNonNull() throws Exception {
        TestObject actual = prototype.build();

        assertThat(actual.getString()).isNotNull();
        assertThat(actual).has(nonNullIn(TestObject::getString));
    }

    @Test
    public void testNull() throws Exception {
        TestObject actual = prototype.string(null).build();

        assertThat(actual.getString()).isNull();
        assertThat(actual).has(nullIn(TestObject::getString));
    }

    @Test
    public void testEqualTo() throws Exception {
        TestObject actual = prototype.build();
        TestObject expected = prototype.build();

        assertThat(actual.getString()).isEqualTo(expected.getString());
        assertThat(actual).has(equalTo(expected.getString(), TestObject::getString));
    }

    @Test
    public void testCollectionContaining() throws Exception {
        int i = 1;
        TestObject actual = prototype.list(Collections.singletonList(i)).build();

        assertThat(actual.getList()).contains(i);
        assertThat(actual).has(collectionContaining(i, TestObject::getList));
    }

    @Test
    public void testEmptyCollection() throws Exception {
        TestObject actual = prototype.build();

        assertThat(actual.getList()).isEmpty();
        assertThat(actual).has(empty(TestObject::getList));
    }

    @Test
    public void testHasSize() throws Exception {
        TestObject emptyListObject = prototype.build();

        assertThat(emptyListObject.getList()).isEmpty();
        assertThat(emptyListObject).has(hasSize(0, TestObject::getList));

        TestObject notEmptyListObject = prototype.list(Collections.singletonList(1)).build();

        assertThat(notEmptyListObject.getList()).hasSize(1);
        assertThat(notEmptyListObject).has(hasSize(1, TestObject::getList));
    }

    @Test
    public void testEmptyMap() throws Exception {
        TestObject actual = prototype.build();

        assertThat(actual.getMap()).isEmpty();
        assertThat(actual).has(emptyMap(TestObject::getMap));
    }

    @Test
    public void testPositive() throws Exception {
        TestObject actual = prototype.integer(1).build();

        assertThat(actual.getInteger()).isPositive();
        assertThat(actual).has(positive(TestObject::getInteger));

        TestObject negativeIntegerObject = prototype.integer(-1).build();

        assertThat(negativeIntegerObject.getInteger()).isNegative();
        assertThat(negativeIntegerObject).doesNotHave(positive(TestObject::getInteger));

        TestObject zeroIntegerObject = prototype.integer(0).build();

        assertThat(zeroIntegerObject.getInteger()).isZero();
        assertThat(zeroIntegerObject).doesNotHave(positive(TestObject::getInteger));
    }

    @Test
    public void testNotEmpty() throws Exception {
        TestObject actual = prototype.build();

        assertThat(actual.getString()).isNotEmpty();
        assertThat(actual).has(notEmpty(TestObject::getString));

        TestObject emptyStringObject = prototype.string("").build();

        assertThat(emptyStringObject.getString()).isEmpty();
        assertThat(emptyStringObject).doesNotHave(notEmpty(TestObject::getString));
    }

    @Value
    @Builder(toBuilder = true)
    private static class TestObject {
        private String string;
        private Integer integer;
        private List<Integer> list;
        private Map<Long, Character> map;
    }
}
