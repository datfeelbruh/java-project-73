package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data transfer class for Spring Security authentication.
 *
 * @author sobadxx
 * @see hexlet.code.filter.JWTAuthenticationFilter
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthDto implements Dto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 3)
    private String password;
}
