# Event-Driven Rule Engine
## Local start-up
1. Start dependencies (Kafka)
    ```bash
   docker compose up -d
   ```
2. Run the app
    ```bash
   ./mvnw spring-boot:run
    ```
   
The app will start on port 8080 with:
- H2 file-based database at ./data/db
- Kafka at localhost:9092
- REST API at http://localhost:8080
- H2 Console at http://localhost:8080/h2-console

## Documentation
1. [Configuration guide](docs/CONFIG.md)
2. [Condition DSL](docs/CONDITIONS.md)
3. [Transformation syntax](docs/TRANSFORMATIONS.md)

## Examples
Using example condition and transformation rules from the documentation.
### Input
```json
{
  "user": {
    "firstName": "John",
    "lastName": "Smith",
    "age": "18",
    "totalBalance": 100
  }
}
```

### Output
```json
{
  "user": {
    "lastName": "Smith",
    "age": "19",
    "totalBalance": 100,
    "fullName": "John Smith"
  }
}
```
