package dev.zetta.interview.RuleEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.zetta.interview.RuleEngine.config.KafkaOutputTopicProperties;
import dev.zetta.interview.RuleEngine.exceptions.MessageEvaluationException;
import dev.zetta.interview.RuleEngine.rules.engine.condition.ConditionEngine;
import dev.zetta.interview.RuleEngine.rules.engine.transformation.TransformationEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleEngineService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConditionEngine conditionEngine;
    private final TransformationEngine transformationEngine;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaOutputTopicProperties kafkaOutputTopicProperties;

    @KafkaListener(topics = "${app.kafka.topics.input.name}", groupId = "${app.kafka.topics.input.group-id}")
    public void onMessageReceive(String message) throws JsonProcessingException {
        log.info("Message received: \n{}", message);
        JsonNode inputMessage = objectMapper.readTree(message);

        JsonNode outputMessage = conditionEngine.evaluate(inputMessage) ? transformationEngine.transform(inputMessage) : null;

        if (outputMessage != null) {
            kafkaTemplate.send(kafkaOutputTopicProperties.name(), objectMapper.writeValueAsString(outputMessage));
            log.info("Message sent to output topic: \n{}", outputMessage.toPrettyString());
        } else {
            log.error("Message did not pass evaluation test");
        }
    }
}
