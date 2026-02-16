package daw2026.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLocalRequest {
    
    private String name;
    private int latitude;
    private int longitude;
    private String ubication;
    private int capacity;
    private int rooms;
}
