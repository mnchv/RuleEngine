package dev.zetta.interview.RuleEngine.rules.engine.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Condition {
    private String logicalOperation;
    private Criteria[] criteria;
}
