package dev.zetta.interview.RuleEngine.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class RuleEngineController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RuleEngineController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(value = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publishMessage(@RequestBody String body) {
        kafkaTemplate.send("default", "key", body);
        return ResponseEntity.ok("Message received");
    }
}
