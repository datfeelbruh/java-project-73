package hexlet.code.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class UserDtoRs {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Date createdAt;
}
