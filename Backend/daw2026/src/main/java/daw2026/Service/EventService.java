package daw2026.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

    public EventService(EventRepository eventRepository, UserRepository userRepository, LocalRepository localRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.localRepository = localRepository;}
    public List<Event> findAllOrderByStartDate() {
        return eventRepository.findAllByOrderByStartDateAsc();}
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);}
    public List<Event> findByStartDate(Date startDate) {
        return eventRepository.findByStartDate(startDate);}
    public Optional<Event> findByName(String name) {
        return eventRepository.findByName(name);}

    public Event createEvent(Long userId, Long localId, Event event) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado"));
        if (event.getCapacity() > local.getCapacity()) {
            throw new IllegalArgumentException("La capacidad del evento supera la del local");}
        if (event.getRooms() > local.getRooms()) {
            throw new IllegalArgumentException("Las salas del evento superan las del local");}
        if (event.getEndDate().before(event.getStartDate())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");}
        event.setUser(user);
        event.setLocal(local);
        event.setCreatedAt(new Date(System.currentTimeMillis()));
        return eventRepository.save(event);}

    public Event updateEvent(Long userId, Event event) {
        Event existingEvent = eventRepository.findById(event.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));
        if (!existingEvent.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("No tienes permisos para actualizar este evento");}
        event.setUser(existingEvent.getUser());
        event.setLocal(existingEvent.getLocal());
        event.setCreatedAt(existingEvent.getCreatedAt());
        return eventRepository.save(event);}

    public void deleteEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));
        if (!event.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("No tienes permisos para eliminar este evento");}
        eventRepository.deleteById(eventId);}
}