package hexlet.code.mapper;

import hexlet.code.dto.UserDtoResponse;
import hexlet.code.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDtoResponse toUserDtoRsFromUser(User user) {
        return UserDtoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
