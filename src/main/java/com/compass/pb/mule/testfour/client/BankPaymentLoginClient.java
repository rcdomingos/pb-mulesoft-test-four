package com.compass.pb.mule.testfour.client;

import com.compass.pb.mule.testfour.domain.pbbank.BankAuthRequest;
import com.compass.pb.mule.testfour.domain.pbbank.BankAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BankPaymentLoginClient", url = "${app-custom.bank-url}")
public interface BankPaymentLoginClient {

    @PostMapping("/auth")
    ResponseEntity<BankAuthResponse> getAuthenticationToken(@RequestBody BankAuthRequest request);

}
