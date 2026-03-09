package com.example.search.messaging;

import com.example.search.entity.ProductIndex;
import com.example.search.repository.ProductIndexRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProductConsumer {

    private final ProductIndexRepository productIndexRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "product-events", groupId = "search-group")
    public void consumeProductEvent(ConsumerRecord<String, String> record) {
        log.info("Consumed product event: {}", record.value());
        try {
            // We assume the event is a ProductCreatedEvent JSON
            // For simplicity, we'll map it to a Map first or a DTO if we shared it
            // Ideally, we should have a shared library or duplicate the DTO
            Map<String, Object> event = objectMapper.readValue(record.value(), Map.class);
            
            ProductIndex index = ProductIndex.builder()
                    .id((String) event.get("id"))
                    .name((String) event.get("name"))
                    .description((String) event.get("description"))
                    .price(((Number) event.get("price")).doubleValue())
                    .categoryId((String) event.get("categoryId"))
                    .attributes((Map<String, String>) event.get("attributes"))
                    .build();

            productIndexRepository.save(index);
            log.info("Indexed product: {}", index.getId());

        } catch (Exception e) {
            log.error("Failed to process product event", e);
        }
    }
}
