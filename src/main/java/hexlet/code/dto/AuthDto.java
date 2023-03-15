package hexlet.code.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDto implements Dto {
    private String email;
    private String password;
}
