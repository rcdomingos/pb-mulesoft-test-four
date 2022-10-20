package com.compass.pb.mule.testfour.domain;

import com.compass.pb.mule.testfour.constant.Brand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CardPayment {

    @NotBlank
    @JsonProperty("card_number")
    private String cardNumber;

    @NotBlank
    @JsonProperty("cardholder_name")
    private String cardHolderName;

    @NotBlank
    @JsonProperty("security_code")
    private String secureCode;

    @NotNull
    @Max(12)
    @JsonProperty("expiration_month")
    private Integer expirationMonth;

    @NotNull
    @JsonProperty("expiration_year")
    private Integer expirationYear;

    @NotNull
    private Brand brand;
}
