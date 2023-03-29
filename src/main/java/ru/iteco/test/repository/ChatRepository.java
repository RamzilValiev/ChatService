package ru.iteco.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iteco.test.model.entity.ChatEntity;
import ru.iteco.test.model.entity.UserEntity;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    Optional<ChatEntity> findByChatName(String userName);
}
