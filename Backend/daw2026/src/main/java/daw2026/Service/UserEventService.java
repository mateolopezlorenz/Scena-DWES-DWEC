package daw2026.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public UserEvent addLike(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado."));
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Evento con ID " + eventId + " no encontrado."));

        Optional<UserEvent> existing = userEventRepository.findByUserIdAndEventId(userId, eventId);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Ya has dado like a este evento.");
        }

        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEvent(event);
        userEvent.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return userEventRepository.save(userEvent);
    }

    @Transactional
    public void removeLike(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado.");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Evento con ID " + eventId + " no encontrado.");
        }
        Optional<UserEvent> existing = userEventRepository.findByUserIdAndEventId(userId, eventId);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("No has dado like a este evento.");
        }
        userEventRepository.deleteByUserIdAndEventId(userId, eventId);
    }

    public boolean isLiked(Long userId, Long eventId) {
        return userEventRepository.findByUserIdAndEventId(userId, eventId).isPresent();
    }

    public long countLikes(Long eventId) {
        return userEventRepository.countByEventId(eventId);
    }

    public List<Event> findLikedEventsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado.");
        }
        return eventRepository.findLikedEventsByUserId(userId);
    }
}
