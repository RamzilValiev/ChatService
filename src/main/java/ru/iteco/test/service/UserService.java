package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.UserAlreadyExistException;
import ru.iteco.test.exception.UserNotFoundException;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.model.entity.UserEntity;
import ru.iteco.test.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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

    public Long save(UserDto userDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserName(userDto.getUserName());
        if (userEntityOptional.isPresent()) {
            throw new UserAlreadyExistException(userDto.getUserName());
        }

        UserEntity userEntity = convertToUserEntity(userDto);
        userRepository.save(userEntity);
        return userEntity.getId();
    }

    private UserEntity convertToUserEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
