package com.github.nginate.commons.testing.dto;

import lombok.Data;

@Data
public class RecursiveDto {
    private RecursiveDto recursiveDto;
}
