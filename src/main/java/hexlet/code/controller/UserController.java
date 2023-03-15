package hexlet.code.controller;

import hexlet.code.dto.UserDtoRq;
import hexlet.code.dto.UserDtoRs;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.security.JwtTokenUtil;
import hexlet.code.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController()
@RequestMapping("${base-url}" + "/users")
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/api/users";
    public static final String ID = "/{id}";
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping(ID)
    public UserDtoRs getUser(@PathVariable Long id) {
        return userMapper.toUserDtoRsFromUser(userService.getUserById(id));
    }

    @GetMapping("")
    public List<UserDtoRs> getUsers() {
        return userService.getAllUsers()
                .stream()
                .map(e -> userMapper.toUserDtoRsFromUser(e))
                .toList();
    }

    @PostMapping("")
    public UserDtoRs createUser(@RequestBody UserDtoRq userDtoRq) {
        return userMapper.toUserDtoRsFromUser(userService.createUser(userDtoRq));
    }

    @PutMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserDtoRs updateUser(@RequestBody UserDtoRq userDtoRq,
                                @PathVariable Long id) {

        return userMapper.toUserDtoRsFromUser(userService.updateUser(userDtoRq, id));
    }

    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
