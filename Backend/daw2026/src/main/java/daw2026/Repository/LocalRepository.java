package daw2026.Repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import daw2026.Model.local;

public interface LocalRepository extends JpaRepository<local, Long> {
    
    Optional<local> findByName(String name);
    Optional<local> findById(Long id);
    List<local> findAll();
}