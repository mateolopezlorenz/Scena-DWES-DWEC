package daw2026.Dto;

import java.time.LocalDateTime;

import daw2026.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {

    private String name;
    private String description;
    private Category category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double latitude;
    private Double longitude;
    private String address;
}
