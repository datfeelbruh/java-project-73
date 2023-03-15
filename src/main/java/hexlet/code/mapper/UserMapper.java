package hexlet.code.mapper;

import hexlet.code.dto.UserDtoRs;
import hexlet.code.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDtoRs toUserDtoRsFromUser(User user) {
        return UserDtoRs.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
