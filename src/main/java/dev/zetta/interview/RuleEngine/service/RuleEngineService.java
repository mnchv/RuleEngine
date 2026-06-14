package dev.zetta.interview.RuleEngine.service;

import dev.zetta.interview.RuleEngine.config.KafkaTopicProperties;
import dev.zetta.interview.RuleEngine.exceptions.MessageEvaluationException;
import dev.zetta.interview.RuleEngine.rules.engine.condition.ConditionEngine;
import dev.zetta.interview.RuleEngine.rules.engine.transformation.TransformationEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RuleEngineService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConditionEngine conditionEngine;
    private final TransformationEngine transformationEngine;
    private final PersistenceService persistenceService;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @KafkaListener(topics = "${app.kafka.input-topic}", groupId = "default")
    public void onMessageReceive(String message) throws IOException {
        System.out.println("Message received: \n" + message);
        JsonNode inputMessage = objectMapper.readTree(message);

        JsonNode outputMessage = conditionEngine.evaluate(inputMessage) ? transformationEngine.transform(inputMessage) : null;

        if (outputMessage != null) {
            persistenceService.save(outputMessage);

            kafkaTemplate.send(kafkaTopicProperties.outputTopic(), outputMessage.toString());
            System.out.println("Message sent to output topic: \n" + outputMessage.toPrettyString());
        } else {
            throw new MessageEvaluationException("Message body did non pass evaluation test");
        }
    }
}
