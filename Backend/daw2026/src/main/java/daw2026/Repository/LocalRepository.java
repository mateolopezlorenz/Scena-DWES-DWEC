package daw2026.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import daw2026.Model.Local;

public interface LocalRepository extends JpaRepository<Local, Long> {
    Optional<Local> findByName(String name);
}