package dev.zetta.interview.RuleEngine.rules.engine.condition;

import dev.zetta.interview.RuleEngine.exceptions.ConditionEvaluationException;
import dev.zetta.interview.RuleEngine.exceptions.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class ConditionEngine {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.conditions.path}")
    private Resource conditionsFile;

    private Condition readConditions() {
        try {
            JsonNode conditions = objectMapper.readTree(conditionsFile.getInputStream());
            return objectMapper.treeToValue(conditions, Condition.class);
        } catch (IOException e) {
            throw new ConfigurationException("Conditions configuration file could not be loaded: " + e.getMessage());
        }
    }

    public boolean evaluate(JsonNode input) {
        log.info("Evaluating message body...");
        return evaluate(readConditions(), input);
    }

    public boolean evaluate(Condition condition, JsonNode inputMessage) {
        if (!condition.isNested()) return compareValues(condition, inputMessage);

        return switch (condition.getLogicalOperator()) {
            case "all" -> Arrays.stream(condition.getConditions()).allMatch(c -> evaluate(c, inputMessage));
            case "any" -> Arrays.stream(condition.getConditions()).anyMatch(c -> evaluate(c, inputMessage));
            case "none" -> Arrays.stream(condition.getConditions()).noneMatch(c -> evaluate(c, inputMessage));
            default -> throw new ConditionEvaluationException("Unexpected logical operator: " + condition.getLogicalOperator());
        };
    }

    private boolean compareValues(Condition condition, JsonNode inputMessage) {
        JsonNode inputMessageField = inputMessage.at(mapToJsonPath(condition.getField()));
        if (inputMessageField == null) throw new ConditionEvaluationException("Field expected, but missing from input message: " + condition.getField());

        long currentValue = Long.parseLong(inputMessageField.asText());
        long compareValue = Long.parseLong(condition.getValue());

        return switch (condition.getOperator()) {
            case "==" -> currentValue == compareValue;
            case "!==" -> currentValue != compareValue;
            case ">" -> currentValue > compareValue;
            case ">=" -> currentValue >= compareValue;
            case "<" -> currentValue < compareValue;
            case "<=" -> currentValue <= compareValue;
            default -> throw new ConditionEvaluationException("Unexpected operator: " + condition.getOperator());
        };
    }

    private String mapToJsonPath(Object path) {
        return "/" + path.toString().replace(".", "/");
    }
}
