package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.user.UserAlreadyExistException;
import ru.iteco.test.exception.authentication.UserNotFoundByNameException;
import ru.iteco.test.exception.user.UserNotFoundException;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.model.entity.UserEntity;
import ru.iteco.test.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(userEntity -> new UserDto(userEntity.getUserName(), userEntity.getCreatedAt(), userEntity.getPassword()))
                .toList();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userEntity -> new UserDto(userEntity.getUserName(), userEntity.getCreatedAt(), userEntity.getPassword()))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserEntity findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Optional<UserEntity> findUserByUserName(String userName) {
       return Optional.ofNullable(userRepository.findByUserName(userName)
               .orElseThrow(() -> new UserNotFoundByNameException(userName)));
    }

    public String save(UserDto userDto) {
        log.info("Request received to save user with name: '{}'", userDto.userName());

        Optional<UserEntity> userEntityOptional = userRepository.findByUserName(userDto.userName());
        if (userEntityOptional.isPresent()) {
            throw new UserAlreadyExistException(userDto.userName());
        }

        UserEntity userEntity = mapToUserEntity(userDto);
        userRepository.save(userEntity);

        log.info("User with name: {} saved in database under id: {}", userDto.userName(), userEntity.getId());
        return String.format("created new user id: %d", userEntity.getId());
    }

    public String save(UserEntity userEntity) {
        Optional<UserEntity> userName = userRepository.findByUserName(userEntity.getUserName());
        if (userName.isPresent()) {
            throw new UserAlreadyExistException(userEntity.getUserName());
        }

        userRepository.save(userEntity);

        return String.format("created new user id: %d", userEntity.getId());
    }

    public UserEntity mapToUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUserName(userDto.userName());
        userEntity.setCreatedAt(userDto.createdAt());
        userEntity.setPassword(userDto.password());
        userEntity.setRole("ROLE_USER");

        return userEntity;
    }
}
