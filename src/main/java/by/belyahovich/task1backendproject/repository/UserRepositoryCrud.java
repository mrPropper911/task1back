package by.belyahovich.task1backendproject.repository;

import by.belyahovich.task1backendproject.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryCrud extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
