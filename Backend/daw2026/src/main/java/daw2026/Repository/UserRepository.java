package daw2026.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import daw2026.Model.user;

public interface UserRepository extends JpaRepository<user, Long> {
    Optional<user> findByEmail(String email);
    Optional<user> findById(Long id);
    List<user> findAll();
    
}
