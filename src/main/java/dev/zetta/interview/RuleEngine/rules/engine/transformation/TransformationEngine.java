package dev.zetta.interview.RuleEngine.rules.engine.transformation;

import dev.zetta.interview.RuleEngine.exceptions.TransformationApplyException;
import dev.zetta.interview.RuleEngine.exceptions.TransformationExpressionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class TransformationEngine {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.transformations.path}")
    private File transformationsFile;

    private Transformation[] readTransformations() throws IOException {
        JsonNode transformations = objectMapper.readTree(transformationsFile);
        return objectMapper.treeToValue(transformations, Transformation[].class);
    }

    public JsonNode transform(JsonNode inputMessage) throws IOException {
        log.info("Proceeding with message transformation...");

        for (Transformation transformation : readTransformations()) apply(transformation, (ObjectNode) inputMessage);

        return inputMessage;
    }

    public JsonNode transform(JsonNode inputMessage, Transformation[] transformations) {
        log.info("Proceeding with message transformation...");

        for (Transformation transformation : transformations) apply(transformation, (ObjectNode) inputMessage);

        return inputMessage;
    }

    private void apply(Transformation transformation, ObjectNode inputMessage) {
        switch (transformation.getOperation()) {
            case "put":
                insertValueAtTargetPath(
                        inputMessage,
                        transformation.getTarget(),
                        resolveExpression(transformation.getExpression(), inputMessage)
                );
                break;
            case "update":
                updateValueAtTargetPath(
                        inputMessage,
                        transformation.getTarget(),
                        resolveExpression(transformation.getExpression(), inputMessage)
                );
                break;
            case "remove":
                removeValueAtTargetPath(
                        inputMessage,
                        transformation.getTarget()
                );
                break;
            default:
                throw new TransformationApplyException("Unexpected operation: " + transformation.getOperation());
        }
    }

    private String resolveExpression(TransformationExpression expression, ObjectNode inputMessage) {
        return switch (expression.getFunction()) {
            case "create", "replace" -> String.valueOf(expression.getArgs().getFirst());
            case "add" -> expression.add(inputMessage);
            case "subtract" -> expression.subtract(inputMessage);
            case "multiply" -> expression.multiply(inputMessage);
            case "divide" -> expression.divide(inputMessage);
            case "concat" -> expression.concat(inputMessage);
            case "uppercase" -> expression.uppercase(inputMessage);
            case "lowercase" -> expression.lowercase(inputMessage);
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
