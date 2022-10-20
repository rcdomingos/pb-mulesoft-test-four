package com.compass.pb.mule.testfour.utils;

import com.compass.pb.mule.testfour.domain.Item;
import com.compass.pb.mule.testfour.domain.PaymentRequest;
import com.compass.pb.mule.testfour.domain.PaymentResponse;
import com.compass.pb.mule.testfour.domain.pbbank.BankAuthResponse;
import com.compass.pb.mule.testfour.domain.pbbank.BankPaymentResponse;
import com.compass.pb.mule.testfour.entity.PaymentEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class MockUtils {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addModule(new JavaTimeModule()).build();

    public static List<Item> getItemsRequestList() {
        try {
            String string = readFile("payload/ItemsRequest.json");
            return List.of(mapper.readValue(string, Item[].class));
        } catch (Exception e) {
            log.error("Error to get file: {}", e.getMessage());
            throw new RuntimeException("Error to mapper string.", e);
        }
    }

    public static PaymentRequest getPaymentRequestValid() {
        try {
            String string = readFile("payload/PaymentRequestValid.json");
            return mapper.readValue(string, PaymentRequest.class);
        } catch (Exception e) {
            log.error("Error to get file: {}", e.getMessage());
            throw new RuntimeException("Error to mapper string.", e);
        }
    }

    public static BankPaymentResponse getBankPaymentResponseAproved() {
        try {
            String string = readFile("payload/BankPaymentResponseAproved.json");
            return mapper.readValue(string, BankPaymentResponse.class);
        } catch (Exception e) {
            log.error("Error to get file: {}", e.getMessage());
            throw new RuntimeException("Error to mapper string.", e);
        }
    }

    public static PaymentEntity getPaymentEntity() {
        try {
            String string = readFile("payload/PaymentEntity.json");
            return mapper.readValue(string, PaymentEntity.class);
        } catch (Exception e) {
            log.error("Error to get file: {}", e.getMessage());
            throw new RuntimeException("Error to mapper string.", e);
        }
    }

    public static PaymentResponse getPaymentResponse() {
        try {
            String string = readFile("payload/PaymentResponse.json");
            return mapper.readValue(string, PaymentResponse.class);
        } catch (Exception e) {
            log.error("Error to get file: {}", e.getMessage());
            throw new RuntimeException("Error to mapper string.", e);
        }
    }

    public static BankAuthResponse getBankAuthResponse() {
        try {
            String string = readFile("payload/BankAuthResponse.json");
            return mapper.readValue(string, BankAuthResponse.class);
        } catch (Exception e) {
            log.error("Error to get file: {}", e.getMessage());
            throw new RuntimeException("Error to mapper string.", e);
        }
    }

    public static String readFile(String filePath) throws IOException {
        final File input = new ClassPathResource(filePath).getFile();
        return new String(Files.readAllBytes(Paths.get(input.getPath())));
    }

    //    public static List<Item> getItemsRequestList() throws IOException {
//
//        File input = new ClassPathResource("payload/ItemsRequest.json").getFile();
//        String string = new String(Files.readAllBytes(Paths.get(input.getPath())));
//        return List.of(mapper.readValue(string, Item[].class));
//
//    }


}
