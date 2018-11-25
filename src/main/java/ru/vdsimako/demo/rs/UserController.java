package ru.vdsimako.demo.rs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vdsimako.demo.model.User;
import ru.vdsimako.demo.repository.UserRepository;
import ru.vdsimako.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;
    private UserService userService;

    public UserController(UserRepository userRepository,
                          UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/getUserList")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PatchMapping("/logOut")
    public ResponseEntity<User> logOutUser(@RequestBody User user) {
        user = userService.logOutUser(user);

        System.out.println(user);

        userService.spreadRequestsBetweenActiveUsers(user.getRequestList());
        return ResponseEntity.ok(null);
    }
}
