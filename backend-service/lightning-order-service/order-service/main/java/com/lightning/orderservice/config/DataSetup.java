package com.lightning.orderservice.config;

import com.lightning.common.entities.Product;
import com.lightning.common.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class DataSetup {

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository, RedisTemplate<String, Object> redisTemplate) {
        return args -> {
            String redisKey = "product:1:stock";
            if (repository.count() == 0) {
                Product flashSaleItem = Product.builder()
                        .name("iPhone 17 Limited Addition")
                        .stock(100)
                        .price(750.0)
                        .build();
                repository.save(flashSaleItem);

                redisTemplate.opsForValue().set(redisKey, 100);
                System.out.println("Flash Sale Item created in DB & Stock pushed to Redis!");
            } else {
                redisTemplate.opsForValue().set(redisKey, 100);
                System.out.println("Redis Warm-up complete!");
            }
        };
    }
}