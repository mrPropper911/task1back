package by.belyahovich.task1backendproject.service;

import by.belyahovich.task1backendproject.dto.UserDTO;
import by.belyahovich.task1backendproject.model.StatusResponse;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long id);

    StatusResponse updateStatusUser(Long id, boolean activity);

}
