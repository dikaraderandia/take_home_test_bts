package com.dikara.bts.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {

    @NotBlank
    private String title;

    @NotNull
    @Positive
    private BigDecimal price;

    private String description;

    @NotBlank
    private String category;

    @NotEmpty
    private List<String> images;
}
