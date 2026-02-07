package daw2026.Model;

import java.security.Timestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_event", uniqueConstraints = {
@UniqueConstraint(columnNames = {"user_id", "event_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    // Relación con Event
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, insertable = false, updatable = false)
    private Event event;

    @Column
    private Boolean liked = false;

    @Column(nullable = false)
    private Timestamp created_at;
}
