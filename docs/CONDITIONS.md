# Condition DSL
Conditions consist of two fields "logicalOperator" and "conditions" array.

The "logicalOperator" can be either "all", "any" or "none".

The "conditions" array can consist of a simple condition or a nested condition.

## Simple condition
Simple conditions consist of three values: "field", "operator" and "value".

"field" is the target field in the input message, which should be compared.

"operator" is the comparison operator.

"value" is the value, which the target field should be compared to.

## Nested condition
A nested condition is a condition with a "logicalOperator" and its own "conditions".
It serves for creating complex condition structures.

## Example
The following is an example of a conditions file which checks the following condition:

`user.age >= 18 AND (user.totalBalance == 50 OR user.totalBalance == 100)`

```json
{
  "logicalOperator": "all",
  "conditions": [
    {
      "field": "user.age",
      "operator": ">=",
      "value": 18
    },
    {
      "logicalOperator": "any",
      "conditions": [
        {
          "field": "user.totalBalance",
          "operator": "==",
          "value": 50
        },
        {
          "field": "user.totalBalance",
          "operator": "==",
          "value": 100
        }
      ]
    }
  ]
}
```