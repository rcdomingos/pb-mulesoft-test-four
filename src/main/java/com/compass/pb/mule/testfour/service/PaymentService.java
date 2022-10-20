package com.compass.pb.mule.testfour.service;

import com.compass.pb.mule.testfour.client.BankPaymentClient;
import com.compass.pb.mule.testfour.domain.CardPayment;
import com.compass.pb.mule.testfour.domain.PaymentRequest;
import com.compass.pb.mule.testfour.domain.PaymentResponse;
import com.compass.pb.mule.testfour.domain.pbbank.*;
import com.compass.pb.mule.testfour.entity.PaymentEntity;
import com.compass.pb.mule.testfour.repository.PaymentRepository;
import com.compass.pb.mule.testfour.utils.CalculateUtils;
import com.compass.pb.mule.testfour.utils.MapperUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final BankPaymentClient bankPaymentClient;

    private final PaymentRepository paymentRepository;

    private final MapperUtils mapperUtils;

    private final CalculateUtils calculateUtils;

    @Value("${app-custom.seller-id}")
    private String sellerId;

    public List<PaymentResponse> getAllPayments() {
        log.info("getAllPayments() - Start - looking for all payment records");
        List<PaymentEntity> entityList = paymentRepository.findAll();
        List<PaymentResponse> responseList = entityList.stream().map(mapperUtils::convertPaymentEntityToResponse).collect(Collectors.toList());
        log.info("getAllPayments() - End - returning the total of {} payments", responseList.size());
        return responseList;
    }

    public PaymentResponse getPaymentById(Long id) {
        log.info("getPaymentById() - Start - find payment by id: {}", id);
        PaymentEntity paymentEntity = paymentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("getPaymentById() - End - finded payment");
        return mapperUtils.convertPaymentEntityToResponse(paymentEntity);
    }

    public PaymentResponse proccessNewPayment(PaymentRequest request) {
        log.info("proccessNewPayment() - Start - proccess new payment request");
        BankPaymentRequest bankRequest = getBankPaymentRequest(request);
        log.debug("proccessNewPayment() - Info - processing payment at bank...");
        BankPaymentResponse bankResponse = processPaymentAtBank(bankRequest);
        PaymentResponse response = getPaymentResponse(bankResponse);
        log.debug("proccessNewPayment() - Info - saving the result in the database...");
        PaymentEntity resultSaved = paymentRepository.save(new PaymentEntity(null, response.getTotal(), response.getPaymentId(), response.getPaymentStatus(), response.getMessage()));
        response.setOrderId(resultSaved.getId());
        log.info("proccessNewPayment() - End - process completed successfully, order id: {}", response.getOrderId());
        return response;
    }

    private BankPaymentResponse processPaymentAtBank(BankPaymentRequest bankRequest) {
        try {
            ResponseEntity<BankPaymentResponse> response = bankPaymentClient.sendCreditCardPayment(bankRequest);
            return response.getBody();
        } catch (FeignException e) {
            log.error("processPaymentAtBank() - error to process credit card payment - status: {}, erro: {}:", e.status(), e.contentUTF8());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private BankPaymentRequest getBankPaymentRequest(PaymentRequest request) {
        log.debug("getPaymentResponse() - Info - preparing information to send to the bank...");
        BankPaymentRequest bankRequest = new BankPaymentRequest();
        bankRequest.setSellerId(sellerId);
        bankRequest.setPaymentType(request.getPaymentType());
        bankRequest.setCustomer(new Customer("CPF", request.getClientId()));
        bankRequest.setCurrencyType(request.getCurrencyType());
        bankRequest.setTransactionAmount(calculateUtils.calculateOrderAmount(request.getItems(), request.getDiscount(), request.getShipping()).doubleValue());
        bankRequest.setCard(getCreditCard(request.getPayment()));
        return bankRequest;
    }

    private Card getCreditCard(CardPayment request) {
        log.debug("getCardInformations() - Info - Get card informations...");
        Card card = new Card();
        card.setNumberToken(request.getCardNumber());
        card.setCardholderName(request.getCardHolderName());
        card.setSecurityCode(request.getSecureCode());
        card.setBrand(request.getBrand());
        card.setExpirationMonth(request.getExpirationMonth());
        card.setExpirationYear(request.getExpirationYear());
        return card;
    }

    private PaymentResponse getPaymentResponse(BankPaymentResponse bankResponse) {
        log.debug("getPaymentResponse() - Info - Get payment informations...");
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(bankResponse.getPaymentId());
        response.setPaymentStatus(bankResponse.getStatus());
        response.setTotal(bankResponse.getTransactionAmount());
        response.setMessage(bankResponse.getAuthorization().getReasonMessage());
        return response;
    }
}
