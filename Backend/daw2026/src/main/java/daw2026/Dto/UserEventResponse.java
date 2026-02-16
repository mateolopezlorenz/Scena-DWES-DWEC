package daw2026.Dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventResponse {
    
    private Long id;
    private Long userId;
    private Long eventId;
    private Boolean liked;
    private Timestamp createdAt;
}
