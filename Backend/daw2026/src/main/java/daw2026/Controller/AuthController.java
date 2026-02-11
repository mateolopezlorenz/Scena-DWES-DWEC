package daw2026.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Model.User;
import daw2026.Service.AuthService;
import daw2026.exception.UserAlreadyExistsException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Registro de un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User usuarioNuevo) {
        try {
            if (usuarioNuevo.getUsername() == null || usuarioNuevo.getUsername().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: El username es obligatorio");
            }
            if (usuarioNuevo.getEmail() == null || usuarioNuevo.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: El email es obligatorio");
            }
            if (usuarioNuevo.getPassword() == null || usuarioNuevo.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: La contraseña es obligatoria");
            }

            Map<String, Object> response = authService.register(usuarioNuevo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el usuario: " + e.getMessage());
        }
    }

    // Login de un usuario existente
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User datosLogin) {
        try {
            Map<String, Object> response = authService.login(datosLogin.getUsername(), datosLogin.getPassword());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al iniciar sesión: " + e.getMessage());
        }
    }
}