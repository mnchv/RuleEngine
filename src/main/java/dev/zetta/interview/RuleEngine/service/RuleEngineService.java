package dev.zetta.interview.RuleEngine.service;

import dev.zetta.interview.RuleEngine.rules.engine.condition.ConditionEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class RuleEngineService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConditionEngine conditionEngine;

    @KafkaListener(topics = "default", groupId = "default")
    public void onReceive(String message) {
        System.out.println("Received " + message);
        JsonNode input = objectMapper.readTree(message);
        System.out.println(conditionEngine.evaluate(input));

    }
}
