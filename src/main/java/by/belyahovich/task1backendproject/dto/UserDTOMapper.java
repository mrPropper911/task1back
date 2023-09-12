package by.belyahovich.task1backendproject.dto;

import by.belyahovich.task1backendproject.model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .uri_image(user.getUri_image())
                .timestamp(user.getTimestamp())
                .active(user.isActive())
                .build();
    }

    public User userDTOtoUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getEmail(),
                userDTO.getUri_image(),
                userDTO.getTimestamp(),
                userDTO.isActive());
    }
}
