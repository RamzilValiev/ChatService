package ru.iteco.test.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.iteco.test.model.entity.ChatEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    Optional<ChatEntity> findByChatName(String userName);

    @Query(
            value = """
                    SELECT c.id, c.name_chat, MAX(m.created_at) AS created_at
                    FROM chats c
                             JOIN chats_users cu ON c.id = cu.chat_id
                             JOIN messages m ON c.id = m.chat_id
                    WHERE cu.user_id = :userId
                    GROUP BY c.id, c.name_chat
                    """,
            nativeQuery = true
    )
    List<ChatEntity> findChatEntitiesByUserId(Long userId, Pageable pageable);
}
