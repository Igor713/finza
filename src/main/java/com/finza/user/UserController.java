package com.finza.user;

import com.finza.user.dto.CreateUserRequest;
import com.finza.user.dto.UserResponse;
import com.finza.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService  userService) {
        this.userService = userService;
    }

    @PostMapping()
    public User create(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @GetMapping()
    public Page<UserResponse> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }
}