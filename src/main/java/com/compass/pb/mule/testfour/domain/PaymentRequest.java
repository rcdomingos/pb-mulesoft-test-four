package com.compass.pb.mule.testfour.domain;

import com.compass.pb.mule.testfour.constant.CurrencyType;
import com.compass.pb.mule.testfour.constant.PaymentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
public class PaymentRequest {

    @NotBlank
    @JsonProperty("cpf")
    private String clientId;

    @NotNull
    @Valid
    private List<Item> items;

    @PositiveOrZero
    private Double shipping;

    @PositiveOrZero
    private Double discount;

    @NotNull
    @JsonProperty("payment_type")
    private PaymentType paymentType;

    @NotNull
    @JsonProperty("currency_type")
    private CurrencyType currencyType;

    @NotNull
    @Valid
    private CardPayment payment;

}
