package com.lightning.orderservice.service;

import com.lightning.common.entities.Product;
import com.lightning.common.repositories.ProductRepository;
import com.lightning.orderservice.consumer.OrderConsumer;
import com.lightning.orderservice.handler.MyWebSocketHandler;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private OrderConsumer orderConsumer;
    @Autowired
    private ProductRepository productRepository;

    public String placeOrder(Long productId) {
        String redisKey = "product:" + productId + ":stock";

        Object currentStock = redisTemplate.opsForValue().get(redisKey);

        if (currentStock == null && Integer.parseInt(currentStock.toString()) <= 0) {
            return "SOLD_OUT";
        }

        Long remainingStock = redisTemplate.opsForValue().decrement(redisKey);
        if (remainingStock != null && remainingStock >= 0) {
            MyWebSocketHandler.broadcastUpdate(String.valueOf(remainingStock));
            kafkaTemplate.send("order-topic", productId.toString());
            return "Success! Remaining stock in Redis: " + remainingStock;
        } else {
            if (remainingStock != null && remainingStock < 0) {
                redisTemplate.opsForValue().set(redisKey, 0);
            }
            return "Sold Out! Better luck next time.";
        }
    }

    @Transactional
    public String addStock(Long productId, int quantity) {
        String redisKey = "product:" + productId + ":stock";
        orderConsumer.updateDbStockManual(productId, quantity);
        Long newStock = redisTemplate.opsForValue().increment(redisKey, quantity);
        if (newStock != null) {
            MyWebSocketHandler.broadcastUpdate(String.valueOf(newStock));
            return "Stock added! New stock: " + newStock;
        }
        return "Failed to add stock";
    }


    public Integer getCurrentStock(Long productId) {
        String redisKey = "product:" + productId + ":stock";
        Object stock = redisTemplate.opsForValue().get(redisKey);

        if (stock == null) {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                redisTemplate.opsForValue().set(redisKey, product.getStock());
                return product.getStock();
            }
            return 0;
        }
        return Integer.parseInt(stock.toString());
    }
}
