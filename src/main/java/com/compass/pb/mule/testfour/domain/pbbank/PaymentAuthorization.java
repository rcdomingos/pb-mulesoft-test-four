package com.compass.pb.mule.testfour.domain.pbbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentAuthorization {

    @JsonProperty("authorization_code")
    private String authorizationCode;

    @JsonProperty("authorized_at")
    private String authorizedAt;

    @JsonProperty("reason_code")
    private String reasonCode;

    @JsonProperty("reason_message")
    private String reasonMessage;

}
