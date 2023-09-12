package by.belyahovich.task1backendproject.service.userServiceImpl;

import by.belyahovich.task1backendproject.config.ResourceNotFoundException;
import by.belyahovich.task1backendproject.dto.UserDTO;
import by.belyahovich.task1backendproject.dto.UserDTOMapper;
import by.belyahovich.task1backendproject.model.StatusResponse;
import by.belyahovich.task1backendproject.model.User;
import by.belyahovich.task1backendproject.repository.UserRepositoryCrud;
import by.belyahovich.task1backendproject.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Log4j
public class UserServiceImpl implements UserService {

    private final UserRepositoryCrud userRepositoryCrud;
    private final UserDTOMapper userDTOMapper;

    public UserServiceImpl(UserRepositoryCrud userRepositoryCrud,
                           UserDTOMapper userDTOMapper) {
        this.userRepositoryCrud = userRepositoryCrud;
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        var userByEmail = userRepositoryCrud.findByEmail(userDTO.getEmail());
        if (userByEmail.isPresent()) {
            log.info("TRYING TO SAVE ALREADY EXISTS USER WITH EMAIL " + userDTO.getEmail());

            throw new ResourceNotFoundException("THIS USER WITH LOGIN: " +
                    userDTO.getEmail() + "ALREADY EXISTS");
        }
        var userToSave = userDTOMapper.userDTOtoUser(userDTO);
        userToSave.setTimestamp(new Timestamp(System.currentTimeMillis()));
        var savedUserToDB = userRepositoryCrud.save(userToSave);
        return userDTOMapper.apply(savedUserToDB);
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> userFromDBbyId = userRepositoryCrud.findById(id);
        if (userFromDBbyId.isEmpty()) {
            log.info("TRYING TO GET NOT EXISTS USER WITH ID " + id);
            throw new ResourceNotFoundException("USER WITH ID: " +
                    id + " NOT EXISTS");
        }
        return userDTOMapper.apply(userFromDBbyId.get());
    }

    @Override
    public StatusResponse updateStatusUser(Long id, boolean activity) {
        Optional<User> userFromDBbyId = userRepositoryCrud.findById(id);
        if (userFromDBbyId.isEmpty()) {
            log.info("TRYING TO UPDATE NOT EXISTS USER WITH ID " + id);
            throw new ResourceNotFoundException("USER WITH ID: " +
                    id + " NOT EXISTS");
        }
        var userToUpdate = userFromDBbyId.get();
        boolean previousActivity = userToUpdate.isActive();
        userToUpdate.setActive(activity);
        userToUpdate.setTimestamp(new Timestamp(System.currentTimeMillis()));
        User updatedUserFromBD = userRepositoryCrud.save(userToUpdate);

        //Create stub by task 5(1)
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error("ERROR FROM TIMEUNIT SLEEP " + e);
        }

        return new StatusResponse(updatedUserFromBD.getId(), previousActivity, updatedUserFromBD.isActive());
    }
}
