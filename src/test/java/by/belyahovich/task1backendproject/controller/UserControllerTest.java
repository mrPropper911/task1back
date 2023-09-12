package by.belyahovich.task1backendproject.controller;

import by.belyahovich.task1backendproject.dto.UserDTO;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTestIT {

    private final static URI URI_API = URI.create("/api/v1/");
    private final static URI URI_EXISTING_USER = URI.create("/api/v1/users/1");
    private final static URI URI_NOT_EXISTING_USER = URI.create("/api/v1/users/-1");

    @Autowired
    TestRestTemplate testRestTemplate;

    static UserDTO createTestUser() {
        return UserDTO.builder()
                .name("Vlad")
                .surname("Dmitrov")
                .email("igoridze1@gmail.com")
                .uri_image("http://some_path")
                .active(true)
                .build();
    }

    @Test
    void getUserById_shouldReturnUser_whenUserIsExisting() {
        ResponseEntity<String> responseEntity =
                testRestTemplate.getForEntity(URI_EXISTING_USER, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
        var actual_id = documentContext.read("@.id");
        var actual_name = documentContext.read("@.email");
        assertThat(actual_id).isEqualTo(1);
        assertThat(actual_name).isNotNull();
    }

    @Test
    void getUserById_shouldReturnNotFound_whenUserIsNotExisting() {
        ResponseEntity<String> responseEntity =
                testRestTemplate.getForEntity(URI_NOT_EXISTING_USER, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUserActivity_shouldUpdateUserActivity_whenUserIsExisting() {
        String url = URI_API + "/users/{id}";
        Map<String, Long> urlParams = Map.of("id", 1L);
        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.fromUriString(url).queryParam("activity", false);

        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(
                        uriComponentsBuilder.buildAndExpand(urlParams).toUriString(),
                        HttpMethod.PUT,
                        new HttpEntity<>(new HttpHeaders()),
                        String.class
                );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
        var actual_id = documentContext.read("@.idUser");
        var actual_previousActivity = documentContext.read("@.previousActivity");
        var actual_actualActivity = documentContext.read("@.actualActivity");
        assertThat(actual_id).isEqualTo(1);
        assertThat(actual_previousActivity).isEqualTo(true);
        assertThat(actual_actualActivity).isEqualTo(false);
    }

    @Test
    void updateUserActivity_shouldReturnNotFound404_whenUserIsNotExisting() {
        String url = URI_API + "/users/{id}";
        Map<String, Long> urlParams = Map.of("id", -1L);
        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.fromUriString(url).queryParam("activity", false);

        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(
                        uriComponentsBuilder.buildAndExpand(urlParams).toUriString(),
                        HttpMethod.PUT,
                        new HttpEntity<>(new HttpHeaders()),
                        String.class
                );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createUser_shouldCreateANewUser_withNewUniqUser() {
        var userDTO = createTestUser();
        ResponseEntity<Void> createResponse =
                testRestTemplate.postForEntity(URI_API + "/users", userDTO, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNull();

        //Create new request with URI from privies request
        URI locationOfNewUser = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse =
                testRestTemplate.getForEntity(locationOfNewUser, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();

        //Checking for correctness of saving
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        var actual_id = documentContext.read("@.id");
        var actual_name = documentContext.read("@.name");
        var actual_active = documentContext.read("@.active");
        assertThat(actual_id).isNotNull();
        assertThat(actual_name).isEqualTo("Vlad");
        assertThat(actual_active).isEqualTo(true);
    }
}