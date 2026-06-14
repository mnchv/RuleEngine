package dev.zetta.interview.RuleEngine.rules.engine.transformation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TransformationEngineTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private TransformationEngine transformationEngine;

    private Transformation[] createFullNameField() {
        TransformationExpression expression = new TransformationExpression();
        expression.setFunction("concat");
        expression.setArgs(List.of("user.firstName", "user.lastName"));

        Transformation transformation = new Transformation();
        transformation.setOperation("put");
        transformation.setTarget("user.fullName");
        transformation.setExpression(expression);

        return new Transformation[]{transformation};
    }

    @Test
    void transformationShouldTransformAsExpected() {
        String inputMessage = """
                {
                  "user": {
                    "firstName": "John",
                    "lastName": "Smith",
                    "age": 20
                  }
                }
                """;

        String expectedMessage = """
                {
                  "user": {
                    "firstName": "John",
                    "lastName": "Smith",
                    "age": 20,
                    "fullName": "John Smith"
                  }
                }
                """;

        JsonNode output = transformationEngine.transform(
                objectMapper.readTree(inputMessage),
                createFullNameField()
        );

        assertThat(output).isEqualTo(objectMapper.readTree(expectedMessage));

    }
}
