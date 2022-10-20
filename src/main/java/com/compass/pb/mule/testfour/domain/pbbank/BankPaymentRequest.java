package com.compass.pb.mule.testfour.domain.pbbank;

import com.compass.pb.mule.testfour.constant.CurrencyType;
import com.compass.pb.mule.testfour.constant.PaymentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BankPaymentRequest {

    @JsonProperty("seller_id")
    private String sellerId;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("payment_type")
    private PaymentType paymentType;

    @JsonProperty("currency")
    private CurrencyType currencyType;

    @JsonProperty("transaction_amount")
    private Double transactionAmount;

    private Card card;
}
