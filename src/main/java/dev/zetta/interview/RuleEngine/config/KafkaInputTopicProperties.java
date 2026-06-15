package dev.zetta.interview.RuleEngine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topics.input")
public record KafkaInputTopicProperties(
        String name,
        int partitions,
        int replicas,
        String groupId
) {
}
