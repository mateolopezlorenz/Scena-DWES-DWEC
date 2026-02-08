package daw2026.Repository;

import java.util.Optional;

import daw2026.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import daw2026.Model.Local;

public interface LocalRepository extends JpaRepository<Local, Long> {
    Optional<Local> findByName(String name);
    Optional<Local> createLocal(User user, Local local);
    Optional<Local> updateLocal(User user, Local local);
}