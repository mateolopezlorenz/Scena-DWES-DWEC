package daw2026.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Model.UserEvent;
import daw2026.Service.UserEventService;
import daw2026.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/user-events")
public class UserEventController {

    @Autowired
    private UserEventService userEventService;

    // Toggle like en un evento
    @PostMapping("/like")
    public ResponseEntity<?> toggleLike(@RequestParam Long userId, @RequestParam Long eventId) {
        try {
            UserEvent userEvent = userEventService.toggleLike(userId, eventId);
            return ResponseEntity.ok(userEvent);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al hacer toggle del like");
        }
    }

    // Obtener relación entre usuario y evento
    @GetMapping
    public ResponseEntity<?> getUserEvent(@RequestParam Long userId, @RequestParam Long eventId) {
        try {
            Optional<UserEvent> userEvent = userEventService.findByUserAndEvent(userId, eventId);
            if (userEvent.isPresent()) {
                return ResponseEntity.ok(userEvent.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Relación usuario-evento no encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la relación");
        }
    }

    // Contar likes de un evento
    @GetMapping("/likes/{eventId}")
    public ResponseEntity<?> countLikes(@PathVariable Long eventId) {
        try {
            long likeCount = userEventService.countLikes(eventId);
            return ResponseEntity.ok("{\"likes\": " + likeCount + "}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al contar likes");
        }
    }

    // Obtener eventos que le gustan a un usuario
    @GetMapping("/liked/{userId}")
    public ResponseEntity<?> getLikedByUser(@PathVariable Long userId) {
        try {
            List<UserEvent> likedEvents = userEventService.findLikedByUser(userId);
            return ResponseEntity.ok(likedEvents);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener eventos liked");
        }
    }
}
