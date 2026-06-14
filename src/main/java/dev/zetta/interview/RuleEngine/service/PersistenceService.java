package dev.zetta.interview.RuleEngine.service;

import dev.zetta.interview.RuleEngine.entity.MessageState;
import dev.zetta.interview.RuleEngine.repository.MessageStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class PersistenceService {

    private final MessageStateRepository repository;

    public void save(JsonNode payload) {
        System.out.println("Persisting message state...");

        MessageState messageState = new MessageState();

        messageState.setPayload(payload.toString());
        repository.save(messageState);
    }
}
