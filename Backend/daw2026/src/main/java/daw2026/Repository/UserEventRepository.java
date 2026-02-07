package daw2026.Repository;

import java.util.List;
import java.util.Optional;

import daw2026.Model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    Optional<UserEvent> findByUserIdAndEventId(Long userId, Long eventId);
    long countByEventIdAndLikedTrue(Long eventId);
    List<UserEvent> findByUserIdAndLikedTrue(Long userId);

}
