package com.compass.pb.mule.testfour.service;

import com.compass.pb.mule.testfour.domain.PaymentRequest;
import com.compass.pb.mule.testfour.domain.PaymentResponse;
import com.compass.pb.mule.testfour.domain.pbbank.BankPaymentResponse;
import com.compass.pb.mule.testfour.entity.PaymentEntity;
import com.compass.pb.mule.testfour.repository.PaymentRepository;
import com.compass.pb.mule.testfour.utils.CalculateUtils;
import com.compass.pb.mule.testfour.utils.MapperUtils;
import com.compass.pb.mule.testfour.utils.MockUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PaymentService.class)
class PaymentServiceTest {

    @Autowired
    private PaymentService service;

    @MockBean
    private BankPaymentService bankService;

    @MockBean
    private PaymentRepository paymentRepository;

    @SpyBean
    private MapperUtils mapperUtils;

    @SpyBean
    private CalculateUtils calculateUtils;


    @Test
    void proccessNewPayment_shouldReturnPaymentResponse_whenProcessValidPayment() {
        PaymentRequest request = MockUtils.getPaymentRequestValid();
        BankPaymentResponse bankResponse = MockUtils.getBankPaymentResponseAproved();
        PaymentEntity paymentEntity = MockUtils.getPaymentEntity();
        when(bankService.processPaymentAtBank(any())).thenReturn(bankResponse);
        when(paymentRepository.save(any())).thenReturn(paymentEntity);

        PaymentResponse result = service.proccessNewPayment(request);

        assertNotNull(result);
        assertEquals(paymentEntity.getId(), result.getOrderId());
    }

    @Test
    void getPaymentById_shouldReturnPaymentResponse_whenFindOne() {
        Long id = 1L;
        when(paymentRepository.findById(id)).thenReturn(Optional.of(MockUtils.getPaymentEntity()));

        PaymentResponse result = service.getPaymentById(id);

        assertNotNull(result);
        assertEquals(id, result.getOrderId());
    }

    @Test
    void getPaymentById_shouldReturnResponseStatusException_whenNotFindOne() {
        Long id = 1L;
        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.getPaymentById(id));
    }


    @Test
    void getAllPayments_shouldReturnAListOfPayments_whenFindAll() {
        List<PaymentEntity> paymentList = List.of(MockUtils.getPaymentEntity());
        when(paymentRepository.findAll()).thenReturn(paymentList);

        List<PaymentResponse> result = service.getAllPayments();

        assertNotNull(result);
        assertEquals(paymentList.size(), result.size());
    }
}