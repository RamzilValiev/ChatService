package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.user.UserAlreadyExistException;
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
                .map(userEntity -> new UserDto(userEntity.getUserName(), userEntity.getCreatedAt()))
                .toList();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userEntity -> new UserDto(userEntity.getUserName(), userEntity.getCreatedAt()))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserEntity findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public String save(UserDto userDto) {
        log.info(String.format("Request received to save user with name: '%s'", userDto.userName()));

        Optional<UserEntity> userEntityOptional = userRepository.findByUserName(userDto.userName());
        if (userEntityOptional.isPresent()) {
            throw new UserAlreadyExistException(userDto.userName());
        }

        UserEntity userEntity = mapToUserEntity(userDto);
        userRepository.save(userEntity);

        log.info(String.format("User with name: %s saved in database under id: %d", userDto.userName(), userEntity.getId()));
        return String.format("created new user id: %d", userEntity.getId());
    }

    private UserEntity mapToUserEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUserName(userDto.userName());
        userEntity.setCreatedAt(userDto.createdAt());

        return userEntity;
    }
}
