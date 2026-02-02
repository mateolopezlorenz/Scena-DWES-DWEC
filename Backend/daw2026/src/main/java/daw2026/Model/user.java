package daw2026.Model;

import java.security.Timestamp;

import jakarta.persistence.*;

@Entity
@Table
public class user {

    //Atributos
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column 
    private String password;

    @Column
    private Timestamp created_at;
}
