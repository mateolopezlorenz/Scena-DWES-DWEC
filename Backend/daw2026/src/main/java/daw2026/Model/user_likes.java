package daw2026.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
public class user_likes {

    //Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long user_id;

    @Column
    private Long event_id;

    @Column
    private Timestamp create_at;

}