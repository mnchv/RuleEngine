package dev.zetta.interview.RuleEngine.rules.engine.condition;

import dev.zetta.interview.RuleEngine.exceptions.ConditionEvaluationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@Component
public class ConditionEngine {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.conditions.path}")
    private File conditionsFile;

    private Condition readConditions() {
        JsonNode conditions = objectMapper.readTree(conditionsFile);
        return objectMapper.treeToValue(conditions, Condition.class);
    }

    public boolean evaluate(JsonNode input) {
        System.out.println("Evaluating message body...");
        return evaluate(readConditions(), input);
    }

    private boolean evaluate(Condition condition, JsonNode inputMessage) {
        if (!condition.isNested()) return compareValues(condition, inputMessage);

        return switch (condition.getLogicalOperator()) {
            case "all" -> Arrays.stream(condition.getConditions()).allMatch(c -> evaluate(c, inputMessage));
            case "any" -> Arrays.stream(condition.getConditions()).anyMatch(c -> evaluate(c, inputMessage));
            case "none" -> Arrays.stream(condition.getConditions()).noneMatch(c -> evaluate(c, inputMessage));
            default -> throw new ConditionEvaluationException("Unexpected logical operator: " + condition.getLogicalOperator());
        };
    }

    private boolean compareValues(Condition condition, JsonNode inputMessage) {
        JsonNode inputMessageField = inputMessage.get(condition.getField());
        if (inputMessageField == null) throw new ConditionEvaluationException("Field expected, but missing from input message: " + condition.getField());

        String currentValue = inputMessageField.asString();
        String compareValue = condition.getValue();

        return switch (condition.getOperator()) {
            case "==" -> Objects.equals(currentValue, compareValue);
            case "!==" -> !Objects.equals(currentValue, compareValue);
            case ">" -> Long.parseLong(currentValue) > Long.parseLong(compareValue);
            case ">=" -> Long.parseLong(currentValue) >= Long.parseLong(compareValue);
            case "<" -> Long.parseLong(currentValue) < Long.parseLong(compareValue);
            case "<=" -> Long.parseLong(currentValue) <= Long.parseLong(compareValue);
            default -> throw new ConditionEvaluationException("Unexpected operator: " + condition.getOperator());
        };
    }
}
