package by.belyahovich.task1backendproject.service;

import by.belyahovich.task1backendproject.dto.UserDTO;
import by.belyahovich.task1backendproject.model.StatusResponse;
import by.belyahovich.task1backendproject.model.User;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long id);

    StatusResponse updateStatusUser(Long id, boolean activity);

    List<UserDTO> findByActivityStatusTimestampAfter(Boolean activity, Timestamp timestamp);

    List<UserDTO> findByTimestampAfter(Timestamp timestamp);

    List<UserDTO> findByActivity(Boolean activity);

    List<UserDTO> findAll();
}

