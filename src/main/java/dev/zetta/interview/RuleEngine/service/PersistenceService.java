package dev.zetta.interview.RuleEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zetta.interview.RuleEngine.entity.MessageState;
import dev.zetta.interview.RuleEngine.repository.MessageStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersistenceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MessageStateRepository repository;

    public void save(JsonNode payload) throws JsonProcessingException {
        log.info("Transformation rule applied. Persisting message state...");

        MessageState messageState = new MessageState();

        messageState.setPayload(objectMapper.writeValueAsString(payload));
        repository.save(messageState);
    }
}
