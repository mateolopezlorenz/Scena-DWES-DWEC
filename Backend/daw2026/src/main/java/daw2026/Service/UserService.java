package daw2026.Service;
import java.util.Optional;

import org.springframework.stereotype.Service;

import daw2026.Model.User;
import daw2026.Repository.UserRepository;
import daw2026.exception.ResourceNotFoundException;
import daw2026.exception.UserAlreadyExistsException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User createUser(User user) {
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        throw new UserAlreadyExistsException("El nombre de usuario '" + user.getUsername() + "' ya está en uso.");
    }
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new UserAlreadyExistsException("El email '" + user.getEmail() + "' ya está registrado.");
    }
    return userRepository.save(user);
}

    public User updateUser(User user) {
    Optional<User> existingUser = userRepository.findById(user.getId());
    if (existingUser.isEmpty()) {
        throw new ResourceNotFoundException("Usuario con ID " + user.getId() + " no encontrado.");
    }
    if (!existingUser.get().getUsername().equals(user.getUsername())) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario '" + user.getUsername() + "' ya está en uso.");
        }
    }
    if (!existingUser.get().getEmail().equals(user.getEmail())) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El email '" + user.getEmail() + "' ya está registrado.");
        }
    } 
    return userRepository.save(user);}
}
