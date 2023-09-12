package by.belyahovich.task1backendproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String uri_image;
    private Timestamp timestamp;
    private boolean active;

}
