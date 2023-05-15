package ru.iteco.test.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.iteco.test.model.converter.TokenConverter;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TokenConverter.class)
    @Column(name = "token")
    private String token;

    @Convert(converter = TokenConverter.class)
    @Column(name = "refresh_token")
    private String refreshToken;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne
    private UserEntity userEntity;
}
