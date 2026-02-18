package daw2026.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Dto.CreateEventRequest;
import daw2026.Dto.LikeCountResponse;
import daw2026.Dto.UpdateEventRequest;
import daw2026.Model.Category;
import daw2026.Model.Event;
import daw2026.Model.User;
import daw2026.Repository.UserRepository;
import daw2026.Service.EventService;
import daw2026.Service.UserEventService;
import daw2026.exception.ResourceNotFoundException;
import daw2026.exception.UnauthorizedException;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserEventService userEventService;

    @Autowired
    private UserRepository userRepository;

    /**
     * GET /api/events
     * Listar todos los eventos (público)
     * Retorna los eventos ordenados por fecha de inicio (próximos primero)
     * Incluye información del usuario creador
     */
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        try {
            List<Event> eventos = eventService.findAllOrderByStartDate();
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/events/{id}
     * Obtener detalle de un evento específico (público)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        try {
            Optional<Event> evento = eventService.findById(id);
            if (evento.isPresent()) {
                return ResponseEntity.ok(evento.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/events
     * Crear un nuevo evento (protegido)
     * 
     * Validaciones:
     * - Nombre obligatorio
     * - Fecha inicio < Fecha fin
     * - Coordenadas válidas (latitud [-90, 90], longitud [-180, 180])
     * 
     * El evento se asocia automáticamente con el usuario autenticado
     */
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody CreateEventRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Se requiere autenticación");
            }

            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            // Crear evento a partir del request
            Event evento = new Event();
            evento.setName(request.getName());
            evento.setDescription(request.getDescription());
            evento.setCategory(request.getCategory());
            evento.setStartDate(request.getStartDate());
            evento.setEndDate(request.getEndDate());
            evento.setLatitude(request.getLatitude());
            evento.setLongitude(request.getLongitude());
            evento.setAddress(request.getAddress());

            // El servicio se encarga de todas las validaciones
            Event eventoCreado = eventService.createEvent(user.getId(), evento);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventoCreado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el evento: " + e.getMessage());
        }
    }

    /**
     * PUT /api/events/{id}
     * Editar un evento (protegido, solo el creador)
     * 
     * Solo el creador del evento puede editarlo
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody UpdateEventRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Se requiere autenticación");
            }

            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            // Crear objeto con los datos nuevos
            Event eventoDatos = new Event();
            eventoDatos.setName(request.getName());
            eventoDatos.setDescription(request.getDescription());
            eventoDatos.setCategory(request.getCategory());
            eventoDatos.setStartDate(request.getStartDate());
            eventoDatos.setEndDate(request.getEndDate());
            eventoDatos.setLatitude(request.getLatitude());
            eventoDatos.setLongitude(request.getLongitude());
            eventoDatos.setAddress(request.getAddress());

            Event eventoActualizado = eventService.updateEvent(user.getId(), id, eventoDatos);
            return ResponseEntity.ok(eventoActualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el evento");
        }
    }

    /**
     * DELETE /api/events/{id}
     * Eliminar un evento (protegido, solo el creador)
     * 
     * Solo el creador del evento puede eliminarlo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Se requiere autenticación");
            }

            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            eventService.deleteEvent(user.getId(), id);
            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el evento");
        }
    }

    /**
     * Endpoints adicionales para funcionalidad de likes/favoritos
     */

    // Filtrar eventos por categoría
    @GetMapping("/category/{category}")
    public List<Event> getEventsByCategory(@PathVariable Category category) {
        return eventService.findByCategory(category);
    }

    // Buscar evento por nombre
    @GetMapping("/search/{name}")
    public ResponseEntity<Event> getEventByName(@PathVariable String name) {
        try {
            Optional<Event> evento = eventService.findByName(name);
            if (evento.isPresent()) {
                return ResponseEntity.ok(evento.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Añadir evento a favoritos (like)
    @PostMapping("/{id}/like")
    public ResponseEntity<?> addLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            userEventService.addLike(user.getId(), id);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Like añadido\"}");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al dar like");
        }
    }

    // Eliminar evento de favoritos (unlike)
    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> removeLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            userEventService.removeLike(user.getId(), id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al quitar like");
        }
    }

    // Contar likes de un evento (público)
    @GetMapping("/{id}/likes/count")
    public ResponseEntity<?> countLikes(@PathVariable Long id) {
        try {
            long count = userEventService.countLikes(id);
            return ResponseEntity.ok(new LikeCountResponse(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al contar likes");
        }
    }
}