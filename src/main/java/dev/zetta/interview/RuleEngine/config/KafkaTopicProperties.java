package dev.zetta.interview.RuleEngine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaTopicProperties(String inputTopic, String outputTopic) {
}
