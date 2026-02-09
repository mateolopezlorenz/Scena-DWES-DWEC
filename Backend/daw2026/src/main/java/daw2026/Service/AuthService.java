package daw2026.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import daw2026.Model.User;
import daw2026.Repository.UserRepository;

// Asegúrate de importar tus DTOs (Request/Response)
import daw2026.Dto.*; 

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public JwtResponse login(LoginRequest request) {
        // 1. Autenticar: Spring Security comprueba user y password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Obtener detalles del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // 3. Generar el Token JWT
        String token = jwtTokenUtil.generateToken(userDetails);

        // 4. Buscar datos extra en la DB (como el email) para devolver al front
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Retornamos la respuesta sin el campo de rol
        return new JwtResponse(token, user.getUsername(), user.getEmail());
    }

    public MessageResponse register(RegisterRequest request) {
        // Validaciones básicas
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear el nuevo usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // ENCRIPTAR la contraseña siempre
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    
        user.setEnabled(true);
        userRepository.save(user);
        return new MessageResponse("Usuario registrado correctamente");
    }
}