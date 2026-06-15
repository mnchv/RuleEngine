# Configuration guide
The configuration takes place in the [application.yaml](../src/main/resources/application.yaml)

## DB connection
The default DB connection is set to local H2 file-based DB.

If you want to set up another DB use the following ENV variables:
- DB_URL
- DB_USERNAME
- DB_PASSWORD
- DB_DRIVER

## Kafka configurations
### Kafka topics
The Kafka input and output topics can be configured using the following syntax:
```yaml
app:
  kafka:
    topics:
      input:
        name: input
        partitions: 1
        replicas: 1
        group-id: input-processors
      output:
        name: output
        partitions: 1
        replicas: 1
        group-id: output-processors
```

## Conditions
The conditions configuration file location is set as follows:
```yaml
app:
  conditions:
    path: classpath:rules/conditions.json
```

For creating a conditions json file see [Condition DSL](CONDITIONS.md)

## Transformations
The transformations configuration file location is set as follows:
```yaml
app:
  transformations:
    path: classpath:rules/transformations.json
```

For creating a transformations JSON file see [Transformation syntax](TRANSFORMATIONS.md)
