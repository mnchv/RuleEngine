# Transformation syntax
Transformations consist of the following fields: "operation", "target", "expression".
An "expression" further consist of: "function" and "args".

## Transformation
Supported "operation" types are the following:
- put: Used for creating new fields
- update: Used for updating existing fields
- remove: Used for removing existing fields

"target" points towards the target field that should be created, updated or removed

## Transformation expression
"expression" is the type of transformation that should be applied to the target field.

"expression.function" is the type of modification that should be applied. 
All functions use the arguments provided in the "expression.args" array to apply modifications.
Possibles values are:
- create: Used for creating a single new field
- replace: Used for replacing a single existing field

Arithmetic operations (Values in "args" can be Numbers or JSON fields from the input message)
- add: Used to sum up all the values provided in "args" and then write them to the target field
- subtract: Used to subtract all the values provided in "args" and then write them to the target field
- multiply: Used to multiply the first value provided in "args" with the second one (only 2 values allowed)
- divide: Used to divide the first value provided in "args" with the second one (only 2 values allowed)

String operations
- concat: Used to concat the values provided in "args" and write them to the target field, inserting a blank space between each entry
- uppercase: Used to replace all letters in the field supplied in "args" with uppercase letters (only a single value allowed)
- lowercase: Used to replace all letters in the field supplied in "args" with lowercase letters (only a single value allowed)

## Example
This example transformation creates a new field named fullName, which combines the first and last name of a user,
then updates the users age by 1, and at the end removes the first name field of the user.
```json
[
  {
    "operation": "put",
    "target": "user.fullName",
    "expression": {
      "function": "concat",
      "args": [
        "user.firstName",
        "user.lastName"
      ]
    }
  },
  {
    "operation": "update",
    "target": "user.age",
    "expression": {
      "function": "add",
      "args": [
        "user.age",
        1
      ]
    }
  },
  {
    "operation": "remove",
    "target": "user.firstName"
  }
]
```
