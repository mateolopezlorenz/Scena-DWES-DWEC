package daw2026.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalResponse {
    
    private Long id;
    private String name;
    private int latitude;
    private int longitude;
    private String ubication;
    private int capacity;
    private int rooms;
    private UserResponse user;
    private List<EventBasicInfo> events;
}
