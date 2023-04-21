package ru.iteco.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.iteco.test.exception.authentication.UserNotFoundByNameException;
import ru.iteco.test.model.UserEntityDetails;
import ru.iteco.test.model.entity.UserEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserEntityDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userService.findUserByUserName(userName);

        if (userEntityOptional.isEmpty()) {
            throw new UserNotFoundByNameException(userName);
        }
        return new UserEntityDetails(userEntityOptional.get());
    }
}
