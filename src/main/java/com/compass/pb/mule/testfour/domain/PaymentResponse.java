package com.compass.pb.mule.testfour.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentResponse {

    @JsonProperty("order_id")
    private Long orderId;

    private Double total;

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("payment_status")
    private String paymentStatus;

    private String message;
}
