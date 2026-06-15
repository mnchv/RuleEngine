package dev.zetta.interview.RuleEngine.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaInputTopicProperties inputTopicProperties;
    private final KafkaOutputTopicProperties outputTopicProperties;

    @Bean
    NewTopic inputTopic() {
        return TopicBuilder.name(inputTopicProperties.name())
                .partitions(inputTopicProperties.partitions())
                .replicas(inputTopicProperties.replicas())
                .build();
    }

    @Bean
    NewTopic outputTopic() {
        return TopicBuilder.name(outputTopicProperties.name())
                .partitions(outputTopicProperties.partitions())
                .replicas(outputTopicProperties.replicas())
                .build();
    }

}
