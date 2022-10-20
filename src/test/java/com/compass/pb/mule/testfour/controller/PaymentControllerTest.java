package com.compass.pb.mule.testfour.controller;

import com.compass.pb.mule.testfour.domain.PaymentRequest;
import com.compass.pb.mule.testfour.service.PaymentService;
import com.compass.pb.mule.testfour.utils.MockUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService service;

    private static final ObjectMapper mapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addModule(new JavaTimeModule()).build();

    @Test
    void addNewPayment_shouldReturnOkAndPaymentApproved_whenRequestIsCorrect() throws Exception {
        PaymentRequest request = MockUtils.getPaymentRequestValid();
        when(service.proccessNewPayment(request)).thenReturn(MockUtils.getPaymentResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payment_status", is("APPROVED")));
    }

    @Test
    void addNewPayment_shouldReturnFAil_whenRequestIsNotCorrect() throws Exception {
        PaymentRequest request = MockUtils.getPaymentRequestValid();
        request.getItems().get(0).setValue(-30D);
        request.setDiscount(-10D);
        request.setShipping(-9D);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages",
                        containsInAnyOrder(
                                containsString("O campo 'items[0].value' must be greater than or equal to 0"),
                                containsString("O campo 'discount' must be greater than or equal to 0"),
                                containsString("O campo 'shipping' must be greater than or equal to 0"))));
    }

    @Test
    void getAllPayments_shoulReturnOKAndAListOfPayments_whenFindAll() throws Exception {
        when(service.getAllPayments()).thenReturn(List.of(MockUtils.getPaymentResponse()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order/payment"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].payment_id").exists());


    }

    @Test
    void getPaymentById_shoulReturnOKAndAPayment_whenFindWithSucess() throws Exception {
        Long id = 1L;
        when(service.getPaymentById(id)).thenReturn(MockUtils.getPaymentResponse());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order/payment/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payment_id").exists());
    }
}