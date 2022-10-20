package com.compass.pb.mule.testfour.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @JsonProperty("item")
    private String name;

    @NotNull
    @PositiveOrZero
    private Double value;

    @NotNull
    @PositiveOrZero
    @JsonProperty("qty")
    private Integer quantity;
}
