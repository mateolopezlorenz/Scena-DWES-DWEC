package daw2026.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import daw2026.Model.User;
import daw2026.Repository.UserRepository;
import daw2026.Security.CustomUserDetailsService;
import daw2026.Security.JwtTokenUtil;
import daw2026.exception.UserAlreadyExistsException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenUtil jwtTokenUtil,
                       CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    public Map<String, Object> register(User nuevoUsuario) {
        if (userRepository.findByUsername(nuevoUsuario.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario '" + nuevoUsuario.getUsername() + "' ya está en uso.");
        }
        if (userRepository.findByEmail(nuevoUsuario.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El email '" + nuevoUsuario.getEmail() + "' ya está registrado.");
        }
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        nuevoUsuario.setCreated_at(new Timestamp(System.currentTimeMillis()));
        User usuarioGuardado = userRepository.save(nuevoUsuario);
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuarioGuardado.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", usuarioGuardado.getUsername());
        response.put("email", usuarioGuardado.getEmail());
        response.put("id", usuarioGuardado.getId());

        return response;
    }
    public Map<String, Object> login(String username, String password) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String token = jwtTokenUtil.generateToken(userDetails);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("id", user.getId());

        return response;
    }
}