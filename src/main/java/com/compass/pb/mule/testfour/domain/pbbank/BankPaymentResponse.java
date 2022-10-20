package com.compass.pb.mule.testfour.domain.pbbank;

import com.compass.pb.mule.testfour.constant.CurrencyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BankPaymentResponse {

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("seller_id")
    private String sellerId;

    @JsonProperty("transaction_amount")
    private Double transactionAmount;

    @JsonProperty("currency")
    private CurrencyType currencyType;

    private String status;

    @JsonProperty("received_at")
    private LocalDateTime receivedAt;

    private PaymentAuthorization authorization;

}
