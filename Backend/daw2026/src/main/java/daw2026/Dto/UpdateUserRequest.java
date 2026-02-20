package daw2026.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para recibir los datos al actualizar un usuario
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    
    private String name;
    private String email;
    private String password;
}
