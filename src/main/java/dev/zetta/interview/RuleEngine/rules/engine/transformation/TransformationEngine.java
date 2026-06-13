package dev.zetta.interview.RuleEngine.rules.engine.transformation;

import dev.zetta.interview.RuleEngine.exceptions.TransformationApplyException;
import dev.zetta.interview.RuleEngine.exceptions.TransformationExpressionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;

@Component
public class TransformationEngine {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.transformations.path}")
    private File transformationsFile;

    private Transformation[] readTransformations() {
        JsonNode transformations = objectMapper.readTree(transformationsFile);
        return objectMapper.treeToValue(transformations, Transformation[].class);
    }

    public JsonNode transform(JsonNode input) {
        ObjectNode object = (ObjectNode) input;
        Transformation[] transformations = readTransformations();

        for (Transformation transformation : transformations) apply(transformation, object);

        return object;
    }

    private void apply(Transformation transformation, ObjectNode input) {
        switch (transformation.getOperation()) {
            case "put":
                insertValueAtTargetPath(
                        input,
                        transformation.getTarget(),
                        resolveExpression(transformation.getExpression(), input)
                );
                break;
            case "update":
                updateValueAtTargetPath(
                        input,
                        transformation.getTarget(),
                        resolveExpression(transformation.getExpression(), input)
                );
                break;
            case "remove":
                removeValueAtTargetPath(
                        input,
                        transformation.getTarget()
                );
                break;
            default:
                throw new TransformationApplyException("Unexpected operation: " + transformation.getOperation());
        }
    }

    private String resolveExpression(TransformationExpression expression, ObjectNode input) {
        return switch (expression.getFunction()) {
            case "create", "replace" -> String.valueOf(expression.getArgs().getFirst());
            case "add" -> expression.add(input);
            case "subtract" -> expression.subtract(input);
            case "multiply" -> expression.multiply(input);
            case "divide" -> expression.divide(input);
            case "concat" -> expression.concat(input);
            case "uppercase" -> expression.uppercase(input);
            case "lowercase" -> expression.lowercase(input);
            default -> throw new TransformationExpressionException("Unexpected expression function: " + expression.getFunction());
        };
    }

    private void insertValueAtTargetPath(ObjectNode root, String path, String value) {
        String[] nodes = path.split("\\.");
        ObjectNode rootNode = root;
        for (int i = 0; i < nodes.length - 1; i++) {
            rootNode = rootNode.withObject(nodes[i]);
        }
        rootNode.put(nodes[nodes.length - 1], value);
    }

    private void updateValueAtTargetPath(ObjectNode root, String path, String value) {
        String[] nodes = path.split("\\.");
        ObjectNode rootNode = root;
        for (int i = 0; i < nodes.length - 1; i++) {
            rootNode = rootNode.withObject(nodes[i]);
        }

        if (rootNode.get(nodes[nodes.length - 1]) == null)
            throw new TransformationApplyException("Can't update non existant field: " + nodes[nodes.length - 1]);
        rootNode.put(nodes[nodes.length - 1], value);
    }

    private void removeValueAtTargetPath(ObjectNode root, String path) {
        String[] nodes = path.split("\\.");
        ObjectNode rootNode = root;
        for (int i = 0; i < nodes.length - 1; i++) {
            rootNode = rootNode.withObject(nodes[i]);
        }
        rootNode.remove(nodes[nodes.length - 1]);
    }
}
