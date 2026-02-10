package daw2026.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import daw2026.Model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    List<Event> findByStartDate(Date startDate);
    List<Event> findAllByOrderByStartDateAsc();
    List<Event> findByCategory(String category);
}
