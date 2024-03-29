package hexlet.code.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data transfer class for entity type {@link hexlet.code.model.User}.
 *
 * @author sobadxx
 * @see hexlet.code.controller.UserController
 * @see hexlet.code.service.UserService
 */
@Builder
@Getter
@Setter
public class UserDtoRequest implements Dto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 3)
    private String password;
}
