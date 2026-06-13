package dev.zetta.interview.RuleEngine.rules.engine.transformation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transformation {
    private String operation;
    private String target;
    private TransformationExpression expression;
}
