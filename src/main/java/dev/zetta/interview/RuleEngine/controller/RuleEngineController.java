package dev.zetta.interview.RuleEngine.controller;

import dev.zetta.interview.RuleEngine.config.KafkaInputTopicProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class RuleEngineController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaInputTopicProperties kafkaInputTopicProperties;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessageToTopic(@RequestBody String body) {
        kafkaTemplate.send(kafkaInputTopicProperties.name(), body);
        return ResponseEntity.ok("Message received: \n" + body);
    }
}
