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
    public List<Event> findById(Long id) {
        return eventRepository.findById(id);}
    public List<Event> findByStartDate(Date startDate) {
        return eventRepository.findByStartDate(startDate);}
    public Optional<Event> findByName(String name) {
        return eventRepository.findByName(name);}

    public Optional<Event> createEvent(Long userId, Long localId, Event event) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return Optional.empty();}
        Optional<Local> local = localRepository.findById(localId);
        if (local.isEmpty()) {
            return Optional.empty();}
        // Validar que la capacidad del evento no supere la del local
        if (event.getCapacity() > local.get().getCapacity()) {
            return Optional.empty();}
        // Validar que las salas no superen las del local
        if (event.getRooms() > local.get().getRooms()) {
            return Optional.empty();}
        // Validar fechas
        if (event.getEndDate().before(event.getStartDate())) {
            return Optional.empty();}
        event.setUser(user.get());
        event.setLocal(local.get());
        event.setCreatedAt(new Date(System.currentTimeMillis()));
        return Optional.of(eventRepository.save(event));}

    public Optional<Event> updateEvent(Long userId, Event event) {
        Optional<Event> existingEvent = eventRepository.findById(event.getId());
        if (existingEvent.isEmpty()) {
            return Optional.empty();}
        if (!existingEvent.get().getUser().getId().equals(userId)) {
            return Optional.empty();}
        event.setUser(existingEvent.get().getUser());
        event.setLocal(existingEvent.get().getLocal());
        event.setCreatedAt(existingEvent.get().getCreatedAt());
        return Optional.of(eventRepository.save(event));}

    public boolean deleteEvent(Long userId, Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return false;}
        if (!event.get().getUser().getId().equals(userId)) {
            return false;}
        eventRepository.deleteById(eventId);
        return true;}
}