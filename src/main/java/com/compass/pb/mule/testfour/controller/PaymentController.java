package com.compass.pb.mule.testfour.controller;

import com.compass.pb.mule.testfour.domain.PaymentRequest;
import com.compass.pb.mule.testfour.domain.PaymentResponse;
import com.compass.pb.mule.testfour.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order/payment")
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponse> addNewPayment(@RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.ok(service.proccessNewPayment(request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(service.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPaymentById(id));
    }

}
