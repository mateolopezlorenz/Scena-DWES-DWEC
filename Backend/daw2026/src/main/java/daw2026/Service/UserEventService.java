package daw2026.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import daw2026.Model.Event;
import daw2026.Model.User;
import daw2026.Model.UserEvent;
import daw2026.Repository.EventRepository;
import daw2026.Repository.UserEventRepository;
import daw2026.Repository.UserRepository;
import daw2026.exception.ResourceNotFoundException;

@Service
public class UserEventService {
    private final UserEventRepository userEventRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public UserEventService(UserEventRepository userEventRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.userEventRepository = userEventRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }
    
    public UserEvent toggleLike(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado."));
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Evento con ID " + eventId + " no encontrado."));
        
        Optional<UserEvent> userEvent = userEventRepository.findByUserIdAndEventId(userId, eventId);
        if (userEvent.isEmpty()) {
            UserEvent newUserEvent = new UserEvent();
            newUserEvent.setUser(user);
            newUserEvent.setEvent(event);
            newUserEvent.setLiked(false);
            newUserEvent.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            return userEventRepository.save(newUserEvent);
        } else {
            userEvent.get().setLiked(!userEvent.get().getLiked());
            return userEventRepository.save(userEvent.get());
        }
    }

    public Optional<UserEvent> findByUserAndEvent(Long userId, Long eventId) {
        return userEventRepository.findByUserIdAndEventId(userId, eventId);
    }

    public long countLikes(Long eventId) {
        return userEventRepository.countByEventIdAndLikedTrue(eventId);
    }

    public List<UserEvent> findLikedByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado.");
        }
        return userEventRepository.findByUserIdAndLikedTrue(userId);
    }
}