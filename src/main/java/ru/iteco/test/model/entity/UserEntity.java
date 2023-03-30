package ru.iteco.test.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id")
    @JoinColumn(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    //@NotEmpty(message = "Name shouldn't be empty")  //TODO do validate!
    private String userName;

    @Column(name = "created_at")
    @CreationTimestamp // Сохраняет в entity localtime автоматически
    private LocalDateTime createdAt;
}
