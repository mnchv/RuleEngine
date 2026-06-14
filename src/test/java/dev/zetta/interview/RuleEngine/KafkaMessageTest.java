package dev.zetta.interview.RuleEngine;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(topics = "testTopic", partitions = 1)
public class KafkaMessageTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void sendsAndReceivesMessage() {
        var consumerProps = KafkaTestUtils.consumerProps(embeddedKafkaBroker, "test-group", true);

        try (Consumer<String, String> consumer =
                     new KafkaConsumer<>(consumerProps, new StringDeserializer(), new StringDeserializer())) {
            consumer.subscribe(List.of("testTopic"));

            KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(1));
            kafkaTemplate.send("testTopic", "Test message");
            ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "testTopic", Duration.ofSeconds(5));

            assertThat(record.value()).isEqualTo("Test message");
        }
    }
}
