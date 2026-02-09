package daw2026.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import daw2026.Model.User;
import daw2026.Repository.UserRepository;

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

    public Optional<User> createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return Optional.empty();
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> updateUser(User user) {
        if (userRepository.findById(user.getId()).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userRepository.save(user));
    }
}
