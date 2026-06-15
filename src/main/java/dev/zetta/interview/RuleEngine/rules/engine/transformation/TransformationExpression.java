package dev.zetta.interview.RuleEngine.rules.engine.transformation;

import dev.zetta.interview.RuleEngine.exceptions.TransformationExpressionException;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.ListIterator;

@Getter
@Setter
public class TransformationExpression {
    private String function;
    private List<Object> args;

    public String add(ObjectNode input) {
        long currentValue = input.at(mapToJsonPath(args.getFirst())).asLong();
        long newValue = ((Number) args.getLast()).longValue();
        return String.valueOf(currentValue + newValue);
    }

    public String subtract(ObjectNode input) {
        long currentValue = input.at(mapToJsonPath(args.getFirst())).asLong();
        long newValue = ((Number) args.getLast()).longValue();
        return String.valueOf(currentValue - newValue);
    }

    public String multiply(ObjectNode input) {
        long currentValue = input.at(mapToJsonPath(args.getFirst())).asLong();
        long newValue = ((Number) args.getLast()).longValue();
        return String.valueOf(currentValue * newValue);
    }

    public String divide(ObjectNode input) {
        long currentValue = input.at(mapToJsonPath(args.getFirst())).asLong();
        long newValue = ((Number) args.getLast()).longValue();

        if (newValue == 0) throw new TransformationExpressionException("Can't divide 0");

        return String.valueOf(currentValue / newValue);
    }

    public String concat(ObjectNode input) {
        StringBuilder stringBuilder = new StringBuilder();

        ListIterator<Object> listIterator = args.listIterator();
        while (listIterator.hasNext()) {
            String value = input.at(mapToJsonPath(listIterator.next())).asText();
            stringBuilder.append(value);
            if (listIterator.hasNext()) stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    public String uppercase(ObjectNode input) {
        return input.at(mapToJsonPath(args.getFirst())).asText().toUpperCase();
    }

    public String lowercase(ObjectNode input) {
        return input.at(mapToJsonPath(args.getFirst())).asText().toLowerCase();
    }

    private String mapToJsonPath(Object path) {
        return "/" + path.toString().replace(".", "/");
    }
}
