package com.compass.pb.mule.testfour.repository;

import com.compass.pb.mule.testfour.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
