package daw2026.Controller;

import java.sql.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Model.Event;
import daw2026.Model.User;
import daw2026.Repository.UserRepository;
import daw2026.Service.EventService;
import daw2026.exception.ResourceNotFoundException;
import daw2026.exception.UnauthorizedException;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    // Listar todos los eventos
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.findAllOrderByStartDate();
    }

    // Obtener evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        try {
            Optional<Event> event = eventService.findById(id);
            if (event.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(event.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }
     // Filtrar eventos por fecha de inicio
    @GetMapping("/startDate/{startDate}")
    public List<Event> getEventsByStartDate(@PathVariable String startDate) {
        return eventService.findByStartDate(Date.valueOf(startDate));
    }

    // Filtrar eventos por categoría
    @GetMapping("/category/{category}")
    public List<Event> getEventsByCategory(@PathVariable String category) {
        return eventService.findByCategory(category);
    }

    // Buscar evento por nombre
    @GetMapping("/searchEvent/{name}")
    public ResponseEntity<Event> getEventByName(@PathVariable String name) {
        try {
        Optional<Event> event = eventService.findByName(name);
                if (event.isPresent()) {
                    return ResponseEntity.status(HttpStatus.OK).body(event.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }

    // Crear evento 
    @PostMapping("/createEvent")
    public ResponseEntity<?> createEvent(@RequestBody Event nuevoEvento,
                                         @RequestParam Long localId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Event eventoGuardado = eventService.createEvent(user.getId(), localId, nuevoEvento);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventoGuardado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el evento: " + e.getMessage());
        }
    }

    // Editar evento 
    @PutMapping("/updateEvent/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,
                                         @RequestBody Event datosNuevos,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            datosNuevos.setId(id);
            Event eventoActualizado = eventService.updateEvent(user.getId(), datosNuevos);
            return ResponseEntity.status(HttpStatus.OK).body(eventoActualizado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el evento");
        }
    }

    // Eliminar evento 
    @DeleteMapping("/deleteEvent/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            eventService.deleteEvent(user.getId(), id);
            return ResponseEntity.status(HttpStatus.OK).body("Evento eliminado");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el evento");
        }
    }
}