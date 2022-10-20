package com.compass.pb.mule.testfour.utils;

import com.compass.pb.mule.testfour.domain.Item;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CalculateUtils {

    public BigDecimal calculateOrderAmount(List<Item> items, Double discount, Double shipping) {
        BigDecimal totalItems = BigDecimal.ZERO;
        for (Item item : items) {
            totalItems = totalItems.add(BigDecimal.valueOf(item.getQuantity() * item.getValue()));
        }
        return totalItems.add(BigDecimal.valueOf(shipping)).subtract(BigDecimal.valueOf(discount));
    }
}
