package daw2026.Repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import daw2026.Model.event;

public interface EventRepository extends JpaRepository<event, Long> {
    Optional<event> findById(Long id);
    Optional<event> findByName(String name);
    List<event> findAll();
}
