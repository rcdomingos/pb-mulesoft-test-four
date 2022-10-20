package com.compass.pb.mule.testfour.domain.pbbank;

import com.compass.pb.mule.testfour.constant.Brand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Card {

    @JsonProperty("number_token")
    private String numberToken;

    @JsonProperty("cardholder_name")
    private String cardholderName;

    @JsonProperty("security_code")
    private String securityCode;

    @JsonProperty("brand")
    private Brand brand;

    @JsonProperty("expiration_month")
    private Integer expirationMonth;

    @JsonProperty("expiration_year")
    private Integer expirationYear;
}
