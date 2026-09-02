package com.finza.user;

import com.finza.user.UserRepository;
import com.finza.user.dto.CreateUserRequest;
import com.finza.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(CreateUserRequest request) {
        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());

        return userRepository.save(user);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
}