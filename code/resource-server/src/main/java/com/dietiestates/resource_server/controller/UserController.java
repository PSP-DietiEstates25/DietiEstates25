package com.dietiestates.resource_server.controller;

import com.dietiestates.resource_server.dto.request.UserRequest;
import com.dietiestates.resource_server.dto.response.UserResponse;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.exception.notfound.UserNotFoundException;
import com.dietiestates.resource_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody UserRequest request
    ) throws RoleNotFoundException {

        var user = userService.setupRegister(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{userid}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userid
    ) throws UserNotFoundException {

        var user = userService.getUserById(userid);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}