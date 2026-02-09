package daw2026.Service;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import daw2026.Model.Event;
import daw2026.Model.User;
import daw2026.Model.UserEvent;
import daw2026.Repository.EventRepository;
import daw2026.Repository.UserEventRepository;
import daw2026.Repository.UserRepository;

@Service
public class UserEventService {
    private final UserEventRepository userEventRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public UserEventService(UserEventRepository userEventRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.userEventRepository = userEventRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;}
    
    public Optional<UserEvent> toggleLike(Long userId, Long eventId) {
        Optional<UserEvent> userEvent = userEventRepository.findByUserIdAndEventId(userId, eventId);
        if (userEvent.isEmpty()) {
            return Optional.empty();
        }
        userEvent.get().setLiked(!userEvent.get().getLiked());
        return Optional.of(userEventRepository.save(userEvent.get()));
    }



    public Optional<UserEvent> findByUserAndEvent(Long userId, Long eventId) {
        return userEventRepository.findByUserIdAndEventId(userId, eventId);
    }

    public long countLikes(Long eventId) {
        return userEventRepository.countByEventIdAndLikedTrue(eventId);}

    public List<UserEvent> findLikedByUser(Long userId) {
        return userEventRepository.findByUserIdAndLikedTrue(userId);}
}