package daw2026.Model;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column
    private String description;

    @Column(nullable=false)
    private String category;

    @Column(nullable=false)
    private Date startDate;

    @Column(nullable=false)
    private Date endDate;

    @Column(nullable=false)
    private int capacity;

    @Column(nullable=false)
    private int rooms;

    @Column(name = "created_at", nullable=false)
    private Date createdAt;

    // Quien crea el evento (ignoramos events y locals del User para evitar recursión infinita).
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"events", "locals"})
    private User user;

    // Donde se realiza el evento (ignoramos events y user del Local para evitar recursión infinita).
    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    @JsonIgnoreProperties({"events", "user"})
    private Local local;
}