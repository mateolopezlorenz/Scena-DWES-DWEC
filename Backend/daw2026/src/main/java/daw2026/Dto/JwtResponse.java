package daw2026.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//DTO el cual devuelve el token JWT, después de loguear el usuario correctamente.
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;

    //Constructor el cual recibe el token, username y email, para devolverlo al frontend después de que el usuario se loguee correctamente.
    public JwtResponse(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }
}