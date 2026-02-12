package daw2026.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Model.User;
import daw2026.Model.UserEvent;
import daw2026.Repository.UserRepository;
import daw2026.Service.UserEventService;
import daw2026.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/user-events")
public class UserEventController {

    @Autowired
    private UserEventService userEventService;

    @Autowired
    private UserRepository userRepository;

    // Dar o no like en un evento 
    @PostMapping("/like")
    public ResponseEntity<?> toggleLike(@RequestParam Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            UserEvent userEvent = userEventService.toggleLike(user.getId(), eventId);
            return ResponseEntity.status(HttpStatus.OK).body(userEvent);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al hacer toggle del like");
        }
    }

    // Obtener relación entre usuario y evento
    @GetMapping("/relation")
    public ResponseEntity<?> getUserEvent(@RequestParam Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Optional<UserEvent> userEvent = userEventService.findByUserAndEvent(user.getId(), eventId);
            if (userEvent.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(userEvent.get());
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
            return ResponseEntity.status(HttpStatus.OK).body("{\"likes\": " + likeCount + "}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al contar likes");
        }
    }

    // Obtener eventos que le gustan al usuario 
    @GetMapping("/liked")
    public ResponseEntity<?> getLikedByUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            List<UserEvent> likedEvents = userEventService.findLikedByUser(user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(likedEvents);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener eventos liked");
        }
    }
}
