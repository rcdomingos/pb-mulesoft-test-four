package com.compass.pb.mule.testfour.service;

import com.compass.pb.mule.testfour.domain.pbbank.BankAuthRequest;
import com.compass.pb.mule.testfour.domain.pbbank.BankAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankAuthenticationService {

    private final RestTemplate restTemplate;
    private BankAuthResponse authResponse;

    @Value("${app-custom.apikey}")
    private String apiKey;

    @Value("${app-custom.client-id}")
    private String clientId;

    @Value("${app-custom.bank-url}")
    private String bankUrl;

    public String getAuthorizationToken() {
        log.debug("getAuthorizationToken() - Start get token");
        if (isExpired(authResponse)) {
            getNewAuthorizationToken();
        }
        return authResponse.getAccessToken();
    }

    private void getNewAuthorizationToken() {
        HttpEntity<BankAuthRequest> request = new HttpEntity<>(new BankAuthRequest(clientId, apiKey));
        ResponseEntity<BankAuthResponse> response = restTemplate.postForEntity(bankUrl.concat("/auth"), request, BankAuthResponse.class);
        authResponse = response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;

        if (authResponse != null) {
            authResponse.setObtainedAt(LocalDateTime.now());
        } else {
            log.error("getNewAuthorizationToken() - Error to get new Token, status:{}", response.getStatusCode());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isExpired(BankAuthResponse token) {
        return token == null || LocalDateTime.now().isAfter(token.getObtainedAt().plusSeconds(token.getExpiresIn()));
    }
}
