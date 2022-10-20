package com.compass.pb.mule.testfour.service;

import com.compass.pb.mule.testfour.domain.pbbank.BankPaymentRequest;
import com.compass.pb.mule.testfour.domain.pbbank.BankPaymentResponse;
import com.compass.pb.mule.testfour.domain.pbbank.PaymentAuthorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankPaymentService {

    private final RestTemplate restTemplate;

    private final BankAuthenticationService authService;

    @Value("${app-custom.bank-url}")
    private String bankUrl;

    public BankPaymentResponse processPaymentAtBank(BankPaymentRequest bankRequest) {
        try {
            log.info("processPaymentAtBank() - Start - send request to bank type: {}", bankRequest.getPaymentType());
            String fullUrl = bankUrl + "/payments/credit-card";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authService.getAuthorizationToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BankPaymentRequest> request = new HttpEntity<>(bankRequest, headers);

            ResponseEntity<BankPaymentResponse> bankResponse = restTemplate.postForEntity(fullUrl, request, BankPaymentResponse.class);

            log.info("processPaymentAtBank() - End - finished with status code: {}", bankResponse.getStatusCode());
            return bankResponse.getBody();
        } catch (HttpClientErrorException e) {
            log.error("processPaymentAtBank() - Error - status code {}, message: {}", e.getStatusCode(), e.getMessage());
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return getPaymentWithError(e.getMessage());
            }
        } catch (Exception e) {
            log.error("processPaymentAtBank() - Error to process: {}", e.getMessage());
        }
        return null;
    }

    private BankPaymentResponse getPaymentWithError(String error) {
        BankPaymentResponse response = new BankPaymentResponse();
        response.setStatus("ERROR");
        PaymentAuthorization authorizationResponse = new PaymentAuthorization();
        authorizationResponse.setReasonMessage(error);
        response.setAuthorization(authorizationResponse);
        return response;
    }
}
