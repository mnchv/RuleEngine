package dev.zetta.interview.RuleEngine.rules.engine.transformation;

import dev.zetta.interview.RuleEngine.exceptions.TransformationApplyException;
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
        long result = 0;

        for (Object arg : args) {
            long newValue = arg instanceof String ? input.at(mapToJsonPath(arg)).asLong() : ((Number) arg).longValue();
            result += newValue;
        }

        return String.valueOf(result);
    }

    public String subtract(ObjectNode input) {
        long result = 0;

        for (Object arg : args) {
            long newValue = arg instanceof String ? input.at(mapToJsonPath(arg)).asLong() : ((Number) arg).longValue();
            result -= newValue;
        }

        return String.valueOf(result);
    }

    public String multiply(ObjectNode input) {
        if (args.size() > 2) throw new TransformationApplyException("Too many arguments provided, only 2 allowed");
        long currentValue = args.getFirst() instanceof String ? input.at(mapToJsonPath(args.getFirst())).asLong() : ((Number) args.getFirst()).longValue();
        long newValue = args.getLast() instanceof String ? input.at(mapToJsonPath(args.getLast())).asLong() : ((Number) args.getLast()).longValue();
        return String.valueOf(currentValue * newValue);
    }

    public String divide(ObjectNode input) {
        if (args.size() > 2) throw new TransformationApplyException("Too many arguments provided, only 2 allowed");
        long currentValue = args.getFirst() instanceof String ? input.at(mapToJsonPath(args.getFirst())).asLong() : ((Number) args.getFirst()).longValue();
        long newValue = args.getLast() instanceof String ? input.at(mapToJsonPath(args.getLast())).asLong() : ((Number) args.getLast()).longValue();

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
        if (args.size() > 1) throw new TransformationApplyException("Too many arguments provided, only a single value allowed");
        return input.at(mapToJsonPath(args.getFirst())).asText().toUpperCase();
    }

    public String lowercase(ObjectNode input) {
        if (args.size() > 1) throw new TransformationApplyException("Too many arguments provided, only a single value allowed");
        return input.at(mapToJsonPath(args.getFirst())).asText().toLowerCase();
    }

    private String mapToJsonPath(Object path) {
        return "/" + path.toString().replace(".", "/");
    }
}
