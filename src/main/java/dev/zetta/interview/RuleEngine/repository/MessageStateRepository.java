package dev.zetta.interview.RuleEngine.repository;

import dev.zetta.interview.RuleEngine.entity.MessageState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageStateRepository extends JpaRepository<MessageState, Long> {
}
