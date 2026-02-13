package daw2026.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private String token;
    private String username;
    private String email;
    private Long id;
}
