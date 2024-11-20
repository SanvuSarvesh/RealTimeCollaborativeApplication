package com.project.realtime_collaborative_doc_editing.config;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String,String> kafkaTemplate;

    @Value("spring.kafka.topic.name")
    private String topic;

    public void sendMessage(String message){
        LOGGER.info("Message sent through kafka is : {}",message);
        kafkaTemplate.send("topic001",message);
    }

//    public static void main(String[] args) {
//        String msg = "Hello Hi bye bye";
//        KafkaProducer kafkaProducer = new KafkaProducer(new KafkaTemplate<>());
//        kafkaProducer.sendMessage(msg);
//    }

// this set of code is for, if we want to send Json object, we can use this in Rest API(Post/Put method is needed)
//    need a separate topic for this which will consume type of DocumentDetails
//    private final KafkaTemplate<String, DocumentDetails> kafkaTemplateJson;
//
//    public void sendJsonMessage(DocumentDetails data){
//        Message<DocumentDetails> message = MessageBuilder.withPayload(data)
//                .setHeader(KafkaHeaders.TOPIC,"topicName")
//                .build();
//        kafkaTemplate.send(message);
//    }


}
