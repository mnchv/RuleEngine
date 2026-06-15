package dev.zetta.interview.RuleEngine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topics.output")
public record KafkaOutputTopicProperties(
        String name,
        int partitions,
        int replicas,
        String groupId
) {
}
