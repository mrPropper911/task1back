package by.belyahovich.task1backendproject.repository;

import by.belyahovich.task1backendproject.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepositoryJpa extends JpaRepository<Image, Long> {
    Optional<Image> findByName (String name);
}
