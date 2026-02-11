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
import daw2026.Dto.LoginRequest;
import daw2026.Dto.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Registro de un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El username es obligatorio");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El email es obligatorio");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: La contraseña es obligatoria");
        }

        User nuevoUsuario = new User();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(request.getPassword());

        Map<String, Object> response = authService.register(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login de un usuario existente
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("El username es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria");
        }
        Map<String, Object> response = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(response);

    } catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales incorrectas");
    }
}

}