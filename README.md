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