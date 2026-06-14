package dev.zetta.interview.RuleEngine.rules.engine.condition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ConditionEngineTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private ConditionEngine conditionEngine;

    private Condition allowUserAgeOver18() {
        Condition simpleCondition = new Condition();
        simpleCondition.setField("user.age");
        simpleCondition.setOperator(">");
        simpleCondition.setValue("18");

        Condition evaluation = new Condition();
        evaluation.setLogicalOperator("all");
        evaluation.setConditions(new Condition[]{simpleCondition});

        return evaluation;
    }

    @Test
    void conditionShouldReturnTrue() {
        String message = """
                {
                  "user": {
                    "firstName": "John",
                    "lastName": "Smith",
                    "age": 20
                  }
                }
                """;

        assertTrue(conditionEngine.evaluate(allowUserAgeOver18(), objectMapper.readTree(message)));
    }

    @Test
    void conditionShouldReturnFalse() {
        String message = """
                {
                  "user": {
                    "firstName": "John",
                    "lastName": "Smith",
                    "age": 16
                  }
                }
                """;

        Condition simpleCondition = new Condition();
        simpleCondition.setField("user.age");
        simpleCondition.setOperator(">");
        simpleCondition.setValue("18");

        Condition evaluation = new Condition();
        evaluation.setLogicalOperator("all");
        evaluation.setConditions(new Condition[]{simpleCondition});

        assertFalse(conditionEngine.evaluate(evaluation, objectMapper.readTree(message)));
    }
}
