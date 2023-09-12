package by.belyahovich.task1backendproject.service.userServiceImpl;

import by.belyahovich.task1backendproject.config.ResourceNotFoundException;
import by.belyahovich.task1backendproject.dto.UserDTO;
import by.belyahovich.task1backendproject.dto.UserDTOMapper;
import by.belyahovich.task1backendproject.model.StatusResponse;
import by.belyahovich.task1backendproject.model.User;
import by.belyahovich.task1backendproject.repository.UserRepositoryCrud;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepositoryCrud userRepositoryCrud;

    @Mock
    UserDTOMapper userDTOMapper;

    @InjectMocks
    UserServiceImpl userService;

    static User createTestUser() {
        return new User(1L, "Vadim", "Cow", "example@gmail.com", "/test_img.img",
                new Timestamp(System.currentTimeMillis()), true);
    }

    @Test
    void createUser_shouldCreateNewUser_whenUserIsUniq() {
        Mockito.doReturn(Optional.empty()).when(this.userRepositoryCrud).findByEmail(anyString());
        Mockito.doReturn(createTestUser()).when(this.userDTOMapper).userDTOtoUser(any());
        Mockito.doReturn(createTestUser()).when(this.userRepositoryCrud).save(any());
        Mockito.doReturn(UserDTO.builder()
                .id(1L)
                .name("Vadim")
                .surname("Cow")
                .email("exam2ple@gmail.com")
                .uri_image("/test_img.img")
                .active(true)
                .build()
        ).when(this.userDTOMapper).apply(any());
        UserDTO actualUser = userService.createUser(UserDTO.builder()
                .id(1L)
                .name("Vadim")
                .email("example@gmail.com")
                .build());
        assertThat(actualUser).isNotNull();
    }

    @Test
    void createUser_shouldReturnException_whenUserExisting(){
        Mockito.doReturn(Optional.of(createTestUser())).when(this.userRepositoryCrud).findByEmail(anyString());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.createUser(UserDTO.builder()
                        .id(1L)
                        .name("Vadim")
                        .email("example@gmail.com")
                        .build()));
        verify(userRepositoryCrud, never()).save(any());
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExisting() {
        Mockito.doReturn(Optional.of(createTestUser())).when(this.userRepositoryCrud).findById(anyLong());
        Mockito.doReturn(UserDTO.builder()
                .id(1L)
                .name("Vadim")
                .email("example@gmail.com")
                .build()
        ).when(this.userDTOMapper).apply(any());
        UserDTO actualUserById = userService.getUserById(anyLong());
        assertThat(actualUserById).isNotNull();
    }

    @Test
    void getUserById_shouldReturnNotFoundException_whenUserNotExisting() {
        Mockito.doReturn(Optional.empty()).when(this.userRepositoryCrud).findById(anyLong());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(anyLong()));
    }

    @Test
    void updateStatusUser_shouldSaveNewUser_withNotExistingInDB() {
        Optional<User> optionalUser = Optional.of(createTestUser());
        Mockito.doReturn(optionalUser).when(this.userRepositoryCrud).findById(1L);
        Mockito.doReturn(optionalUser.get()).when(this.userRepositoryCrud).save(optionalUser.get());

        StatusResponse statusResponse = userService.updateStatusUser(1L, true);

        assertThat(statusResponse).isNotNull();
        verify(userRepositoryCrud, times(1)).findById(anyLong());
        verify(userRepositoryCrud, times(1)).save(optionalUser.get());
    }

    @Test
    void updateStatusUser_shouldReturnNotFoundException_withExistingInDB() {

        Mockito.doReturn(Optional.empty()).when(this.userRepositoryCrud).findById(anyLong());
        assertThatThrownBy(() -> userService.updateStatusUser(1L, anyBoolean()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("USER WITH ID: 1 NOT EXISTS");
        verify(userRepositoryCrud, never()).save(any());
    }
}