package daw2026.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import daw2026.Model.Category;
import daw2026.Model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    List<Event> findAllByOrderByStartDateAsc();
    List<Event> findByCategory(Category category);

    List<Event> findByCategoryOrderByStartDateAsc(Category category);

    @Query("SELECT e FROM Event e WHERE e.startDate <= :endOfDay AND e.endDate >= :startOfDay ORDER BY e.startDate ASC")
    List<Event> findByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT e FROM Event e WHERE e.category = :category AND e.startDate <= :endOfDay AND e.endDate >= :startOfDay ORDER BY e.startDate ASC")
    List<Event> findByCategoryAndDate(@Param("category") Category category, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT e FROM Event e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')) ORDER BY e.startDate ASC")
    List<Event> searchByText(@Param("text") String text);

    @Query("SELECT e FROM Event e WHERE (LOWER(e.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) AND e.category = :category ORDER BY e.startDate ASC")
    List<Event> searchByTextAndCategory(@Param("text") String text, @Param("category") Category category);

    @Query("SELECT e FROM Event e WHERE (LOWER(e.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) AND e.startDate <= :endOfDay AND e.endDate >= :startOfDay ORDER BY e.startDate ASC")
    List<Event> searchByTextAndDate(@Param("text") String text, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT e FROM Event e WHERE (LOWER(e.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) AND e.category = :category AND e.startDate <= :endOfDay AND e.endDate >= :startOfDay ORDER BY e.startDate ASC")
    List<Event> searchByTextAndCategoryAndDate(@Param("text") String text, @Param("category") Category category, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT ue.event FROM UserEvent ue WHERE ue.user.id = :userId")
    List<Event> findLikedEventsByUserId(@Param("userId") Long userId);
}
