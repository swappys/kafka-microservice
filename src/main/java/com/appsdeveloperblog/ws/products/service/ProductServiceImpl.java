package com.appsdeveloperblog.ws.products.service;

import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    KafkaTemplate <String,ProductCreatedEvent> kafkaTemplate;
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(productId)
                .price(productRestModel.getPrice())
                .quantity(productRestModel.getQuantity())
                .title(productRestModel.getTitle())
                .build();
//        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
//        productId, productRestModel.getTitle(), productRestModel.getPrice(), productRestModel.getQuantity()
//        );
        logger.info("Befor publishing a product created event");
        SendResult<String,ProductCreatedEvent> result = kafkaTemplate.send("product-created-events-topic",productId,productCreatedEvent).get();
        logger.info("Partition: "+result.getRecordMetadata().partition());
        logger.info("Topic: "+result.getRecordMetadata().topic());
        logger.info("Offset: "+result.getRecordMetadata().offset());
        logger.info("******Returning product id");
        return productId;
    }
}
