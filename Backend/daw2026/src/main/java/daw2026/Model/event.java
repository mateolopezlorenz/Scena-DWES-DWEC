package daw2026.Model;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table
public class event {

    //Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    private Date start_date;

    @Column
    private Date end_date;

    @Column
    private int latitude;

    @Column 
    private int longitude;

    @Column
    private String addrres;

    @Column
    private Long user_id;

    @Column
    private Date create_at;
    
}