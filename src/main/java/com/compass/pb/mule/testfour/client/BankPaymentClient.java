package com.compass.pb.mule.testfour.client;

import com.compass.pb.mule.testfour.client.filter.BankPaymentInterceptor;
import com.compass.pb.mule.testfour.domain.pbbank.BankPaymentRequest;
import com.compass.pb.mule.testfour.domain.pbbank.BankPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BankPaymentClient", url = "${app-custom.bank-url}", configuration = BankPaymentInterceptor.class)
public interface BankPaymentClient {

    @PostMapping("/payments/credit-card")
    ResponseEntity<BankPaymentResponse> sendCreditCardPayment(@RequestBody BankPaymentRequest request);
}