package daw2026.Controller;

import daw2026.Model.User;
import daw2026.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Herramienta de seguridad para encriptar contraseñas
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registro
    @PostMapping("/register")
    public String register(@RequestBody User usuarioNuevo) {

        // Validación rápida
        if (usuarioNuevo.getEmail() == null || usuarioNuevo.getPassword() == null) {
            return "Fallo: Email y contraseña son obligatorios";
        }

        // Comprobar si ya existe el email en la BBDD
        if (userRepository.findByEmail(usuarioNuevo.getEmail()).isPresent()) {
            return "Fallo: El email ya existe";
        }

        // Encriptar la contraseña
        String passwordCifrada = passwordEncoder.encode(usuarioNuevo.getPassword());
        usuarioNuevo.setPassword(passwordCifrada);

        // Guardar en la base de datos
        userRepository.save(usuarioNuevo);

        return "¡Usuario registrado con éxito!";
    }

    // Login (sin usar token)
    @PostMapping("/login")
    public String login(@RequestBody User datosLogin) {

        // Buscamos al usuario por su email
        Optional<User> usuarioEnBD = userRepository.findByEmail(datosLogin.getEmail());

        if (usuarioEnBD.isPresent()) {
                // Comparar la contraseña que se envía con la encriptada de la BBDD
            boolean coincide = passwordEncoder.matches(datosLogin.getPassword(), usuarioEnBD.get().getPassword());

            if (coincide) {
                // Aquí iría el Token
                return "Login Correcto (El usuario es válido, pero falta implementar el Token)";
            }
        }

        return "Fallo: Credenciales incorrectas";
    }
}