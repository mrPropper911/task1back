package by.belyahovich.task1backendproject;

import by.belyahovich.task1backendproject.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserJsonTest {

    @Autowired
    private JacksonTester<UserDTO> json;

    @Test
    public void userDTOSerializationTest() throws IOException {
        Timestamp timestamp = Timestamp.valueOf("2012-04-23 18:25:43");
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .name("Vlad")
                .surname("Dmitrov")
                .email("igoridze@gmail.com")
                .uri_image("http://some_path")
                .timestamp(timestamp)
                .active(true)
        .build();

        var clsPath = new ClassPathResource("/expected.json");
        assertThat(json.write(userDTO)).isStrictlyEqualToJson(clsPath);

        assertThat(json.write(userDTO)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(userDTO)).extractingJsonPathNumberValue("@.id").isEqualTo(1);

        assertThat(json.write(userDTO)).hasJsonPathStringValue("@.name");
        assertThat(json.write(userDTO)).extractingJsonPathStringValue("@.name").isEqualTo("Vlad");

        assertThat(json.write(userDTO)).hasJsonPathStringValue("@.surname");
        assertThat(json.write(userDTO)).extractingJsonPathStringValue("@.surname").isEqualTo("Dmitrov");

        assertThat(json.write(userDTO)).hasJsonPathStringValue("@.email");
        assertThat(json.write(userDTO)).extractingJsonPathStringValue("@.email").isEqualTo("igoridze@gmail.com");

        assertThat(json.write(userDTO)).hasJsonPathStringValue("@.uri_image");
        assertThat(json.write(userDTO)).extractingJsonPathStringValue("@.uri_image").isEqualTo("http://some_path");

//        assertThat(json.write(userDTO)).hasJsonPathValue("@.timestamp");

        assertThat(json.write(userDTO)).hasJsonPathBooleanValue("@.active");
        assertThat(json.write(userDTO)).extractingJsonPathBooleanValue("@.active").isEqualTo(true);
    }

    @Test
    public void userDTODeserializationTest() throws IOException{
        String expected = """
                    {
                      "id": 1,
                      "name": "Vlad",
                      "surname": "Dmitrov",
                      "email": "igoridze@gmail.com",
                      "uri_image": "http://some_path",
                      "timestamp": "2012-04-23T15:25:43.000+00:00",
                      "active": true
                    }
                """;
        assertThat(json.parse(expected).getObject()).isEqualTo(new UserDTO(1L, "Vlad", "Dmitrov",
                "igoridze@gmail.com", "http://some_path", Timestamp.valueOf("2012-04-23 18:25:43") , true));
        assertThat(json.parseObject(expected).getId()).isEqualTo(1);
        assertThat(json.parseObject(expected).getName()).isEqualTo("Vlad");
        assertThat(json.parseObject(expected).getSurname()).isEqualTo("Dmitrov");
        assertThat(json.parseObject(expected).getEmail()).isEqualTo("igoridze@gmail.com");
        assertThat(json.parseObject(expected).getUri_image()).isEqualTo("http://some_path");
        assertThat(json.parseObject(expected).isActive()).isEqualTo(true);
    }
}
