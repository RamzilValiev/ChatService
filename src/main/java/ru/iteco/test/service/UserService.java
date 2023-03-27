package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.iteco.test.model.dto.UserDto;
import ru.iteco.test.model.entity.UserEntity;
import ru.iteco.test.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(userEntity -> new UserDto(userEntity.getUserName(), userEntity.getCreatedAt()))
                .toList();
    }

    public UserDto findById(Integer id) {
     return userRepository.findById(id)
             .map(userEntity -> new UserDto(userEntity.getUserName(), userEntity.getCreatedAt()))
             .orElse(null);
    }


    public Long save(UserDto userDto) {
        UserEntity userEntity = convertToUserEntity(userDto);
        UserEntity save = userRepository.save(userEntity);
        return save.getId();
    }

    private UserEntity convertToUserEntity (UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userDto.getUserName());
        return userEntity;
    }

}

