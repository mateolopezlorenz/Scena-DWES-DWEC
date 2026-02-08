package daw2026.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import daw2026.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> createUser(User user);
    Optional<User> updateUser(User user);
}
