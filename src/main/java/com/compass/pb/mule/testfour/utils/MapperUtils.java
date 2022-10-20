package com.compass.pb.mule.testfour.utils;

import com.compass.pb.mule.testfour.domain.PaymentResponse;
import com.compass.pb.mule.testfour.entity.PaymentEntity;
import org.springframework.stereotype.Component;


@Component
public class MapperUtils {

    public PaymentResponse convertPaymentEntityToResponse(PaymentEntity entity) {
        PaymentResponse response = new PaymentResponse();
        response.setOrderId(entity.getId());
        response.setPaymentId(entity.getPaymentId());
        response.setPaymentStatus(entity.getPaymentStatus());
        response.setTotal(entity.getTotal());
        response.setMessage(entity.getMessage());
        return response;
    }
}
