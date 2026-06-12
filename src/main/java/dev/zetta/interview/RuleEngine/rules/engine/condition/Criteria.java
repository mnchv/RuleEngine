package dev.zetta.interview.RuleEngine.rules.engine.condition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {
    private String field;
    private String operator;
    private String value;
}
