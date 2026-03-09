package com.lightning.orderservice.consumer;

import com.lightning.common.entities.Product;
import com.lightning.common.repositories.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final ProductRepository productRepository;

    public OrderConsumer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void listen(String productIdStr) {
        try {
            Long productId = Long.parseLong(productIdStr);

            // 1. Database Update (Persistent Storage)
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setStock(product.getStock() - 1);
            productRepository.save(product);

            System.out.println("Kafka Consumer: Database updated for Product " + productId);

        } catch (Exception e) {
            System.err.println("Error in Kafka Consumer: " + e.getMessage());
        }
    }

    public void updateDbStockManual(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }
}