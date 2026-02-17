package daw2026.Controller;

import daw2026.Dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Dto.LoginRequest;
import daw2026.Dto.MessageResponse;
import daw2026.Dto.RegisterRequest;
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
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El username es obligatorio"));
        }
        
        if (request.getUsername().length() < 3) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El username debe tener al menos 3 caracteres"));
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El email es obligatorio"));
        }
        
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El email no es válido"));
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: La contraseña es obligatoria"));
        }
        
        if (request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: La contraseña debe tener al menos 6 caracteres"));
        }

        //Creamos un nuevo usuario con los datos que se han recibido junto a la petición.
        User nuevoUsuario = new User();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(request.getPassword());

        //Llamamos al servicio de autenticación para registrar al nuevo usuario.
        try {
            authService.register(nuevoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Usuario registrado exitosamente"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(e.getMessage()));
        }
    }

    // Login de un usuario existente
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Validamos que los campos no estén vacíos, si lo están, devolvemos código 400.
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("El email es obligatorio"));
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("La contraseña es obligatoria"));
        }

        try {
            LoginResponse loginResponse = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Credenciales incorrectas."));
        }
    }
}