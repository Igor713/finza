package com.finza.user;

import com.finza.user.dto.CreateUserRequest;
import com.finza.user.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("User")
public class UserController {
    private final UserService userService;

    public UserController(UserService  userService) {
        this.userService = userService;
    }

    @PostMapping()
    public User create(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }
}