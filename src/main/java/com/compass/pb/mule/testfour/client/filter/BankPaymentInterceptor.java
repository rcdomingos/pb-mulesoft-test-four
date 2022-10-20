package com.compass.pb.mule.testfour.client.filter;

import com.compass.pb.mule.testfour.client.BankPaymentLoginClient;
import com.compass.pb.mule.testfour.domain.pbbank.BankAuthRequest;
import com.compass.pb.mule.testfour.domain.pbbank.BankAuthResponse;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class BankPaymentInterceptor implements RequestInterceptor {

    private final BankPaymentLoginClient bankClient;

    @Value("${app-custom.apikey}")
    private String apiKey;

    @Value("${app-custom.client-id}")
    private String clientId;

    private BankAuthResponse authResponse;

    private static final String BEARER = "Bearer ";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (isExpired(authResponse)) {
            getNewAuthorizationToken();
        }
        String token = BEARER.concat(authResponse.getAccessToken());
        requestTemplate.header("Authorization", token);
    }

    private void getNewAuthorizationToken() {
        log.debug("getNewAuthorizationToken() - Start get new token");
        BankAuthRequest bankAuthRequest = new BankAuthRequest(clientId, apiKey);
        ResponseEntity<BankAuthResponse> response = bankClient.getAuthenticationToken(bankAuthRequest);
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
