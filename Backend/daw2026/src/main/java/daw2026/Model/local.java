package daw2026.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "local")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private int latitude;

    @Column(nullable=false)
    private int longitude;

    @Column(nullable=false)
    private String ubication;

    @Column(nullable=false)
    private int capacity;

    @Column(nullable=false)
    private int rooms;

    // Quien crea el local
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Un local tiene muchos eventos
    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();
}