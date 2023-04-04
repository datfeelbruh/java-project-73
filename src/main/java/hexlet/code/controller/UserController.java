package hexlet.code.controller;

import hexlet.code.dto.UserDtoRequest;
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


/**
 * Rest controller to process a request for an endpoint /users.
 * @author sobadxx
 */
@RestController
@RequestMapping("${base-url}" + "/users")
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/api/users";
    public static final String ID = "/{id}";
    @Autowired
    private UserService userService;

    /**
     * GET request handler on endpoint "api/users/{id}".
     *
     * @param id path variable from request
     * @return {@link User} from {@link UserService#getUserById(Long)}
     */
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User with that ID was found"),
    })
    @GetMapping(ID)
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * GET request handler on endpoint "api/users".
     *
     * @return All entities {@link User} from {@link UserService#getAllUsers()}
     */
    @Operation(summary = "Get a list of all users")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content =
            @Content(schema =
            @Schema(implementation = User.class)))
    })
    @GetMapping("")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    /**
     * POST request handler on endpoint "api/users".
     *
     * @param userDtoRequest JSON request body
     * @return Created {@link User} from {@link UserService#createUser(UserDtoRequest)}
     */
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public User createUser(@RequestBody UserDtoRequest userDtoRequest) {
        return userService.createUser(userDtoRequest);
    }

    /**
     * PUT request handler on endpoint "api/users/{id}".
     *
     * @param userDtoRequest JSON request body
     * @param id path variable from request
     * @return Created {@link User} from {@link UserService#createUser(UserDtoRequest)}
     */
    @Operation(summary = "Update user by ID")
    @PutMapping(ID)
    public User updateUser(@RequestBody UserDtoRequest userDtoRequest,
                                      @PathVariable Long id) {

        return userService.updateUser(userDtoRequest, id);
    }

    /**
     * DELETE request handler on endpoint "api/users/{id}".
     *
     * @param id path variable from request
     * @see UserService#deleteUser(Long)
     */
    @Operation(summary = "Delete user by ID")
    @DeleteMapping(ID)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
