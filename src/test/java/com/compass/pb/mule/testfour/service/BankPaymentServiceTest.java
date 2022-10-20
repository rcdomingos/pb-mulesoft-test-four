package com.compass.pb.mule.testfour.service;

import com.compass.pb.mule.testfour.domain.pbbank.BankPaymentRequest;
import com.compass.pb.mule.testfour.domain.pbbank.BankPaymentResponse;
import com.compass.pb.mule.testfour.utils.MockUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BankPaymentService.class)
class BankPaymentServiceTest {

    @Autowired
    BankPaymentService service;

    @MockBean
    private BankAuthenticationService authService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void processPaymentAtBank_shouldGetTokenAndReturnSucess_whenSendCorrectPayment() {
        when(restTemplate.postForEntity(anyString(), any(), eq(BankPaymentResponse.class)))
                .thenReturn(ResponseEntity.ok(MockUtils.getBankPaymentResponseAproved()));

        BankPaymentResponse result = service.processPaymentAtBank(new BankPaymentRequest());

        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        verify(authService, only()).getAuthorizationToken();
    }

    @Test
    void processPaymentAtBank_shouldReturnErrorMessage_whenReturningABadRequestFromTheBank() {
        when(restTemplate.postForEntity(anyString(), any(), eq(BankPaymentResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        BankPaymentResponse result = service.processPaymentAtBank(new BankPaymentRequest());

        assertNotNull(result);
        assertEquals("ERROR", result.getStatus());
    }

    @Test
    void processPaymentAtBank_shouldReturnNull_whenReturningAnUnhandledBankException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(BankPaymentResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        BankPaymentResponse result = service.processPaymentAtBank(new BankPaymentRequest());

        assertNull(result);
    }

    @Test
    void processPaymentAtBank_shouldReturnNull_whenReturningException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(BankPaymentResponse.class)))
                .thenThrow(new RuntimeException("Error to process"));

        BankPaymentResponse result = service.processPaymentAtBank(new BankPaymentRequest());

        assertNull(result);
    }
}