package daw2026.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Model.Event;
import daw2026.Repository.EventRepository;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    // Listar todos los eventos
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByStartDateAsc();
    }

    // Obtener evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isPresent()) {
            return ResponseEntity.ok(event.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear evento
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event nuevoEvento) {

        // Validaciones según vuestro documento (Nombre obligatorio)
        if (nuevoEvento.getName() == null || nuevoEvento.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El nombre (name) es obligatorio");
        }

        // Validación de fechas
        if (nuevoEvento.getStartDate() != null && nuevoEvento.getEndDate() != null) {
            if (nuevoEvento.getStartDate().after(nuevoEvento.getEndDate())) {
                return ResponseEntity.badRequest().body("Error: La fecha de inicio no puede ser posterior a la fecha de fin");
            }
        }

        // Validar Local y Usuario
        try {
            Event eventoGuardado = eventRepository.save(nuevoEvento);
            return ResponseEntity.ok(eventoGuardado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar: Revisa que el Local y el User existan. " + e.getMessage());
        }
    }

    // Editar evento
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event datosNuevos) {
        Optional<Event> eventoExistente = eventRepository.findById(id);

        if (eventoExistente.isPresent()) {
            Event evento = eventoExistente.get();

            // Actualizar usando los Setters de Lombok (que se generan solos con @Data)
            evento.setName(datosNuevos.getName());
            evento.setDescription(datosNuevos.getDescription());
            evento.setCategory(datosNuevos.getCategory());
            evento.setStartDate(datosNuevos.getStartDate());
            evento.setEndDate(datosNuevos.getEndDate());
            evento.setCapacity(datosNuevos.getCapacity());
            evento.setRooms(datosNuevos.getRooms());

            // Guardar cambios
            eventRepository.save(evento);
            return ResponseEntity.ok("Evento actualizado correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar evento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return ResponseEntity.ok("Evento eliminado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}