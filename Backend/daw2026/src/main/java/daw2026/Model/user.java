package daw2026.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable=false)
    private String username;

    @Column (unique = true, nullable=false)
    private String email;

    // La contraseña se acepta en peticiones pero no se devuelve en respuestas JSON.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column (nullable=false)
    private String password;

    @Column
    private Timestamp created_at;

    // El campo enabled no se incluye en las respuestas JSON.
    @JsonIgnore
    @Column(nullable = false)
    private Boolean enabled = true;

    // Un usuario crea muchos eventos (ignoramos el campo 'user' dentro de Event para evitar recursión infinita).
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"user", "local"})
    private List<Event> events = new ArrayList<>();

    // Un usuario crea muchos locales (ignoramos el campo 'user' dentro de Local para evitar recursión infinita).
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"user", "events"})
    private List<Local> locals = new ArrayList<>();
}
