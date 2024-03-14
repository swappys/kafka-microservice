package com.appsdeveloperblog.ws.products.rest;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRestModel {
    private String title;
    private BigDecimal price;
    private Integer quantity;

}
