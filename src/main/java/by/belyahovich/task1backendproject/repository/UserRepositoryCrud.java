package by.belyahovich.task1backendproject.repository;

import by.belyahovich.task1backendproject.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface UserRepositoryCrud extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Iterable<User> findByActive (Boolean activity);
    Iterable<User> findByTimestampAfter (Timestamp timestamp);
    Iterable<User> findByActiveAndTimestampAfter(Boolean activity, Timestamp timestamp);
}
