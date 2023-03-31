package hexlet.code.controller;

import hexlet.code.dto.UserDtoRequest;
import hexlet.code.dto.UserDtoResponse;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("${base-url}" + "/users")
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/api/users";
    public static final String ID = "/{id}";
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;


    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User with that ID was found"),
    })
    @GetMapping(ID)
    public UserDtoResponse getUser(@PathVariable Long id) {
        return userMapper.toUserDtoRsFromUser(userService.getUserById(id));
    }

    @Operation(summary = "Get a list of all users")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content =
            @Content(schema =
            @Schema(implementation = User.class)))
    })
    @GetMapping("")
    public List<UserDtoResponse> getUsers() {
        return userService.getAllUsers()
                .stream()
                .map(e -> userMapper.toUserDtoRsFromUser(e))
                .toList();
    }

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public UserDtoResponse createUser(@RequestBody UserDtoRequest userDtoRequest) {
        return userMapper.toUserDtoRsFromUser(userService.createUser(userDtoRequest));
    }

    @Operation(summary = "Update user by ID")
    @PutMapping(ID)
    public UserDtoResponse updateUser(@RequestBody UserDtoRequest userDtoRequest,
                                      @PathVariable Long id) {

        return userMapper.toUserDtoRsFromUser(userService.updateUser(userDtoRequest, id));
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping(ID)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
