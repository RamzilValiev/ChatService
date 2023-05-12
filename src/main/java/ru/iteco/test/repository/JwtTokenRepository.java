package ru.iteco.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iteco.test.model.entity.JwtTokenEntity;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity, Long> {

    Optional<JwtTokenEntity> findByUserEntityUserName(String username);

    boolean existsByToken(String token);
}
