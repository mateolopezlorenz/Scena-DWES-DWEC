package daw2026.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import daw2026.Model.Category;
import daw2026.Model.Event;
import daw2026.Model.Local;
import daw2026.Model.User;
import daw2026.Repository.EventRepository;
import daw2026.Repository.LocalRepository;
import daw2026.Repository.UserRepository;
import daw2026.exception.ResourceNotFoundException;
import daw2026.exception.UnauthorizedException;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocalRepository localRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository,
            LocalRepository localRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.localRepository = localRepository;
    }

    public List<Event> findAllOrderByStartDate() {
        return eventRepository.findAllByOrderByStartDateAsc();
    }

    public List<Event> findFiltered(Category category, LocalDate date, String search) {
        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasCategory = category != null;
        boolean hasDate = date != null;

        if (hasSearch && hasCategory && hasDate) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return eventRepository.searchByTextAndCategoryAndDate(search.trim(), category, startOfDay, endOfDay);
        } else if (hasSearch && hasCategory) {
            return eventRepository.searchByTextAndCategory(search.trim(), category);
        } else if (hasSearch && hasDate) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return eventRepository.searchByTextAndDate(search.trim(), startOfDay, endOfDay);
        } else if (hasSearch) {
            return eventRepository.searchByText(search.trim());
        } else if (hasCategory && hasDate) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return eventRepository.findByCategoryAndDate(category, startOfDay, endOfDay);
        } else if (hasCategory) {
            return eventRepository.findByCategoryOrderByStartDateAsc(category);
        } else if (hasDate) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return eventRepository.findByDate(startOfDay, endOfDay);
        }

        return eventRepository.findAllByOrderByStartDateAsc();
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> findByCategory(Category category) {
        return eventRepository.findByCategory(category);
    }

    public Optional<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }

    public Event createEvent(Long userId, Event event, Long localId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        // Validaciones
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del evento es obligatorio");
        }
        if (event.getStartDate() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        if (event.getEndDate() == null) {
            throw new IllegalArgumentException("La fecha de fin es obligatoria");
        }
        if (event.getEndDate().isBefore(event.getStartDate())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }

        // Si se asocia un local
        if (localId != null) {
            Local local = localRepository.findById(localId)
                    .orElseThrow(() -> new ResourceNotFoundException("Local con ID " + localId + " no encontrado"));
            event.setLocal(local);
            event.setLatitude(local.getLatitude());
            event.setLongitude(local.getLongitude());
            event.setAddress(local.getUbication());
        } else {
            // Sin local
            if (event.getLatitude() == null) {
                throw new IllegalArgumentException("La latitud es obligatoria");
            }
            if (event.getLongitude() == null) {
                throw new IllegalArgumentException("La longitud es obligatoria");
            }
        }

        if (event.getLatitude().abs().compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
        }
        if (event.getLongitude().abs().compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
        }
        
        event.setUser(user);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long userId, Long eventId, Event eventDetails, Long localId) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));
        
        if (!existingEvent.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("No tienes permisos para actualizar este evento");
        }
        
        // Actualizar campos si se proporcionan
        if (eventDetails.getName() != null && !eventDetails.getName().isEmpty()) {
            existingEvent.setName(eventDetails.getName());
        }
        if (eventDetails.getDescription() != null) {
            existingEvent.setDescription(eventDetails.getDescription());
        }
        if (eventDetails.getCategory() != null) {
            existingEvent.setCategory(eventDetails.getCategory());
        }
        if (eventDetails.getStartDate() != null) {
            existingEvent.setStartDate(eventDetails.getStartDate());
        }
        if (eventDetails.getEndDate() != null) {
            existingEvent.setEndDate(eventDetails.getEndDate());
        }
        if (localId != null) {
            if (localId == -1L) {
                // si quitamosel local las coordenadas quedan iguales pero el evento se queda sin associacion
                existingEvent.setLocal(null);
            } else {
                Local local = localRepository.findById(localId)
                        .orElseThrow(() -> new ResourceNotFoundException("Local con ID " + localId + " no encontrado"));
                existingEvent.setLocal(local);
                existingEvent.setLatitude(local.getLatitude());
                existingEvent.setLongitude(local.getLongitude());
                existingEvent.setAddress(local.getUbication());
            }
        }

        // Solo actualizar coordenadas manuales si NO se acaba de asociar un local
        if (localId == null || localId == -1L) {
            if (eventDetails.getLatitude() != null) {
                if (eventDetails.getLatitude().abs().compareTo(BigDecimal.valueOf(90)) > 0) {
                    throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
                }
                existingEvent.setLatitude(eventDetails.getLatitude());
            }
            if (eventDetails.getLongitude() != null) {
                if (eventDetails.getLongitude().abs().compareTo(BigDecimal.valueOf(180)) > 0) {
                    throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
                }
                existingEvent.setLongitude(eventDetails.getLongitude());
            }
            if (eventDetails.getAddress() != null) {
                existingEvent.setAddress(eventDetails.getAddress());
            }
        }
        
        // Validar coherencia de fechas después de actualizar
        if (existingEvent.getEndDate().isBefore(existingEvent.getStartDate())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }
        
        return eventRepository.save(existingEvent);
    }

    @Transactional
    public void deleteEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));
        
        if (!event.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("No tienes permisos para eliminar este evento");
        }

        // Con cascade delete configurado, los registros de user_event se eliminan automáticamente
        eventRepository.delete(event);
    }
}
