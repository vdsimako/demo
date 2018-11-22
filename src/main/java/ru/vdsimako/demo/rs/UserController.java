package ru.vdsimako.demo.rs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vdsimako.demo.model.Request;
import ru.vdsimako.demo.model.enums.RequestStatus;
import ru.vdsimako.demo.model.User;
import ru.vdsimako.demo.model.enums.UserStatus;
import ru.vdsimako.demo.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/getUserList")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/getTest")
    public ResponseEntity<User> getTestUser() {
        return ResponseEntity.ok(User.builder()
                .id(1l)
                .login("login")
                .userStatus(UserStatus.ONLINE)
                .fullName("fullname")
                .requestList(Collections.singletonList(Request.builder()
                        .id(1l)
                        .requestName("requestName")
                        .requestDesc("requestDescription")
                        .requestStatus(RequestStatus.OPEN)
                        .responsibleUser(1l)
                        .build()))
                .build());
    }
}
