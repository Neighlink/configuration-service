package pe.edu.upc.configurationservice.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "buildings")
@Data
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer numberOfHomes;
    @Column(nullable = false)
    private Long condominiumId;
    @Column(nullable = false)
    private boolean isDelete;
}
