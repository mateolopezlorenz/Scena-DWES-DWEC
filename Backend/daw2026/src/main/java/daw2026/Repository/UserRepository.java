package daw2026.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import daw2026.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
