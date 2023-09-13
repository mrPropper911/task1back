package by.belyahovich.task1backendproject.controller;

import by.belyahovich.task1backendproject.dto.UserDTO;
import by.belyahovich.task1backendproject.model.StatusResponse;
import by.belyahovich.task1backendproject.service.userServiceImpl.UserServiceImpl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> updateUserActivity(@PathVariable Long id,
                                                             @RequestParam boolean activity) {
        return ResponseEntity.ok(userService.updateStatusUser(id, activity));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO,
                                           UriComponentsBuilder uriComponentsBuilder) {
        var savedUserDTO = userService.createUser(userDTO);
        URI locationOfNewUser = uriComponentsBuilder
                .path("api/v1/users/{id}")
                .buildAndExpand(savedUserDTO.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewUser).build();
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @GetMapping("/server-stat")
    public ResponseEntity<List<UserDTO>> getServerStatistic(@RequestParam(required = false) Boolean activity,
                                                   @RequestParam(required = false) Long id){
        if(id != null){
            Timestamp timestamp = new Timestamp(id);
            if (activity != null){
                return ResponseEntity.ok(userService.findByActivityStatusTimestampAfter(activity, timestamp));
            }
            return ResponseEntity.ok(userService.findByTimestampAfter(timestamp));
        } else if (activity != null){
            return ResponseEntity.ok(userService.findByActivity(activity));
        }
        return ResponseEntity.ok(userService.findAll());
    }
}
