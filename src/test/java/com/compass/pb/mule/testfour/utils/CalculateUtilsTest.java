package com.compass.pb.mule.testfour.utils;

import com.compass.pb.mule.testfour.domain.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CalculateUtils.class)
class CalculateUtilsTest {

    @Autowired
    private CalculateUtils calculate;

    @Test
    void calculateOrderAmount_shouldReturnTotalValue_whenCalculateWithValidNumbers() {
        List<Item> items = MockUtils.getItemsRequestList();
        BigDecimal result = calculate.calculateOrderAmount(items, 10D, 15D);

        assertEquals(BigDecimal.valueOf(576.8), result);
    }
}