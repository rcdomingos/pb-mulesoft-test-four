package com.compass.pb.mule.testfour.service;

import com.compass.pb.mule.testfour.domain.pbbank.BankAuthResponse;
import com.compass.pb.mule.testfour.utils.MockUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BankAuthenticationService.class)
class BankAuthenticationServiceTest {

    @Autowired
    private BankAuthenticationService service;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void getAuthorizationToken_shouldGetAnAuthToken_whenResquestIsCorrect() {
        when(restTemplate.postForEntity(anyString(), any(), eq(BankAuthResponse.class)))
                .thenReturn(ResponseEntity.ok(MockUtils.getBankAuthResponse()));

        service.getAuthorizationToken();

        BankAuthResponse authResponse = (BankAuthResponse) ReflectionTestUtils.getField(service, "authResponse");
        assertNotNull(authResponse);
        verify(restTemplate, only()).postForEntity(anyString(), any(), eq(BankAuthResponse.class));
    }

    @Test
    @Order(1)
    void getAuthorizationToken_shouldReturnResponseStatusException_whenResquestFail() {
        when(restTemplate.postForEntity(anyString(), any(), eq(BankAuthResponse.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

        assertThrows(ResponseStatusException.class, () -> service.getAuthorizationToken());
    }
}