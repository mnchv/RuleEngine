package dev.zetta.interview.RuleEngine.exceptions;

public class ConditionEvaluationException extends RuntimeException {
    public ConditionEvaluationException(String message) {
        super("Condition evaluation failed: " + message);
    }
}
