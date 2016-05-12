package com.github.nginate.commons.testing.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
public class ObjectArraysFieldsDto {
    private String[] stringField;
    private Character[] characterField;
    private Byte[] byteField;
    private Boolean[] booleanField;
    private Short[] shortField;
    private Integer[] integerField;
    private Double[] doubleField;
    private Float[] floatField;
    private Long[] longField;
    private BigDecimal[] bigDecimalField;
    private BigInteger[] bigIntegerField;
    private Date[] dateField;
    private Instant[] instantField;
    private UUID[] uuidField;
}
