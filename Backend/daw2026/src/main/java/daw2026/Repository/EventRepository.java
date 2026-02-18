package daw2026.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import daw2026.Model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    List<Event> findByStartDate(Date startDate);
    List<Event> findAllByOrderByStartDateAsc();
    List<Event> findByCategory(String category);

    @Query("SELECT ue.event FROM UserEvent ue WHERE ue.user.id = :userId AND ue.liked = true")
    List<Event> findLikedEventsByUserId(@Param("userId") Long userId);
}
