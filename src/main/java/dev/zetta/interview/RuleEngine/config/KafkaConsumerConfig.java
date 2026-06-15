package dev.zetta.interview.RuleEngine.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.zetta.interview.RuleEngine.exceptions.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    DefaultErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000L, 4L));

        handler.addNotRetryableExceptions(
                ConditionEvaluationException.class,
                MessageEvaluationException.class,
                TransformationApplyException.class,
                TransformationExpressionException.class,
                ConfigurationException.class,
                JsonProcessingException.class
        );

        return handler;
    }
}
