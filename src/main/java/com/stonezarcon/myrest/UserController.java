package com.stonezarcon.myrest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;

    UserController(UserRepository userRepository, UserModelAssembler userModelAssembler) {
        this.userRepository = userRepository;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome!";
    }

    @GetMapping("/user")
    public CollectionModel<EntityModel<User>> userList() {

        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).userList()).withSelfRel());
    }

    @GetMapping("/user/{id}")
    public EntityModel<User> userById(@PathVariable String id) {
        int userId = Integer.parseInt(id);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        return userModelAssembler.toModel(user);
    }

    @PostMapping("/user")
    public User addUser(@RequestBody Map<String, String> body) {
        String firstname = body.get("firstname");
        String lastname = body.get("lastname");
        String email = body.get("email");
        String ipaddress = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipaddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return userRepository.save(new User(firstname, lastname, email, ipaddress));
    }

    @DeleteMapping("/user/{id}")
    public boolean delete(@PathVariable String id) {
        int userId = Integer.parseInt(id);
        userRepository.deleteById(userId);
        return true;
    }

    @PutMapping("/user/{id}")
    public User update(@PathVariable String id, @RequestBody Map<String, String> body) {
        int userId = Integer.parseInt(id);
        User user = userRepository.findById(userId).get();
        user.setFirstName(body.get("firstname"));
        user.setLastName(body.get("lastname"));
        user.setEmail(body.get("email"));
        return userRepository.save(user);
    }
}