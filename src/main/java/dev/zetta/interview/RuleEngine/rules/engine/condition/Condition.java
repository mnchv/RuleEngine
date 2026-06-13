package dev.zetta.interview.RuleEngine.rules.engine.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Condition {
    private String logicalOperator;
    private Condition[] conditions;

    private String field;
    private String operator;
    private String value;

    public boolean isNested() {
        return getLogicalOperator() != null;
    }
}
