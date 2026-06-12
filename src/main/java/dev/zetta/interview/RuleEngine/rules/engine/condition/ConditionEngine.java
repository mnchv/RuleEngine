package dev.zetta.interview.RuleEngine.rules.engine.condition;

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

    private Condition readCondition() {
        JsonNode conditions = objectMapper.readTree(conditionsFile);
        return objectMapper.treeToValue(conditions, Condition.class);
    }

    public boolean evaluate(JsonNode input) {
        Condition condition = readCondition();
        return switch (condition.getLogicalOperation()) {
            case "all" ->
                    Arrays.stream(condition.getCriteria()).allMatch(criteria -> compareValues(criteria, input.get(criteria.getField()).asString(), criteria.getValue()));
            case "any" ->
                    Arrays.stream(condition.getCriteria()).anyMatch(criteria -> compareValues(criteria, input.get(criteria.getField()).asString(), criteria.getValue()));
            case "none" ->
                    Arrays.stream(condition.getCriteria()).noneMatch(criteria -> compareValues(criteria, input.get(criteria.getField()).asString(), criteria.getValue()));
            default -> throw new IllegalArgumentException("Unexpected operator: " + condition.getLogicalOperation());
        };
    }

    private boolean compareValues(Criteria criteria, String actual, String expected) {
       return switch (criteria.getOperator()) {
           case "eq" -> Objects.equals(actual, expected);
           case "neq" -> !Objects.equals(actual, expected);
           case "gt" -> Long.parseLong(actual) > Long.parseLong(expected);
           case "gte" -> Long.parseLong(actual) >= Long.parseLong(expected);
           case "lt" -> Long.parseLong(actual) < Long.parseLong(expected);
           case "lte" -> Long.parseLong(actual) <= Long.parseLong(expected);
           default -> throw new IllegalStateException("Unexpected value: " + criteria.getOperator());
       };
    }
}
