package daw2026.Dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    
    private String name;
    private String description;
    private String category;
    private Date startDate;
    private Date endDate;
    private int capacity;
    private int rooms;
}
