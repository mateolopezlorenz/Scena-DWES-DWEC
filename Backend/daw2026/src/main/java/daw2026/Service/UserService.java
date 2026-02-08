package daw2026.Service;

import java.util.*;
import daw2026.Model.user;
import daw2026.Repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Método para poder listar todos los usuarios.
    public List<user> getAllUsers() {
        return userRepository.findAll();
    }

    //Método para poder listar un usuario por su id.
    public Optional<user> getUserById(Long id) {
        return userRepository.findById(id);
    }

    //Método para poder listar un usuario por su username.
    public Optional<user> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
