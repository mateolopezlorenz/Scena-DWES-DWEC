package daw2026.Security;

import daw2026.Repository.UserRepository;
import daw2026.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;


//Servicio el cual se encarga de cargar los datos del usuario, desde la base de datos, para poder válidar el token JWT en cada petición. 
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    //Inyectamos el repositorio de usuarios para cargar los datos desde la base de datos.
    private final UserRepository userRepository;
    

    //Cargamos el usuario desde la base de datos, haciendo uso del repositorio, y lo convertimos en un objeto.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        //Devolvemos el usuario convertido en objeto, para poder ser utilizado a la hora de validar el token.
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .disabled(!user.isEnabled())
                .build();
    }
}

