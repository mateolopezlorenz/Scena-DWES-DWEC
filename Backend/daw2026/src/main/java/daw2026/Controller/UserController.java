package daw2026.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Dto.UpdateUserRequest;
import daw2026.Dto.UserResponse;
import daw2026.Model.Event;
import daw2026.Model.User;
import daw2026.Service.UserEventService;
import daw2026.Service.UserService;
import daw2026.exception.ResourceNotFoundException;
import daw2026.exception.UserAlreadyExistsException;

// Controlador de Usuarios — todas las rutas requieren autenticación JWT.
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEventService userEventService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Obtener eventos favoritos del usuario autenticado
    @GetMapping("/me/likes")
    public ResponseEntity<?> getMyLikes(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            Optional<User> userOpt = userService.findByEmail(userDetails.getUsername());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
            List<Event> likedEvents = userEventService.findLikedEventsByUser(userOpt.get().getId());
            return ResponseEntity.ok(likedEvents);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener favoritos");
        }
    }

    // Devuelve el perfil del usuario logueado.
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            Optional<User> userOpt = userService.findByEmail(userDetails.getUsername());

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                UserResponse response = new UserResponse();
                response.setId(user.getId());
                response.setName(user.getName());
                response.setEmail(user.getEmail());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado" + userDetails.getUsername());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el perfil" + e.getMessage());
        }
    }

    // Busca un usuario por su ID para ver quien creó un evento etc.
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userService.findById(id);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                UserResponse response = new UserResponse();
                response.setId(user.getId());
                response.setName(user.getName());
                response.setEmail(user.getEmail());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el usuario" + e.getMessage());
        }
    }

    // Busca un usuario por su nombre.
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name) {
        try {
            Optional<User> userOpt = userService.findByName(name);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                UserResponse response = new UserResponse();
                response.setId(user.getId());
                response.setName(user.getName());
                response.setEmail(user.getEmail());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con nombre: " + name);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el usuario" + e.getMessage());
        }
    }

    // Busca un usuario por su email.
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> userOpt = userService.findByEmail(email);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                UserResponse response = new UserResponse();
                response.setId(user.getId());
                response.setName(user.getName());
                response.setEmail(user.getEmail());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con email: " + email);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el usuario" + e.getMessage());
        }
    }

    // Actualiza un usuario.
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        try {
            // Validación de campos
            if (request.getName() != null && request.getName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre no puede estar vacío");
            }
            if (request.getEmail() != null && request.getEmail().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email no puede estar vacío");
            }
            
            User user = new User();
            user.setId(id);
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            
            // Si se proporciona contraseña, encriptarla; si no, mantener la existente
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            } else {
                Optional<User> existingUser = userService.findById(id);
                if (existingUser.isPresent()) {
                    user.setPassword(existingUser.get().getPassword());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
                }
            }
            
            user.setEnabled(true);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el usuario");
        }
    }
}
