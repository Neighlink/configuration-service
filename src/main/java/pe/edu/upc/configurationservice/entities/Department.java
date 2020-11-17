package pe.edu.upc.configurationservice.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long buildingId;
    @Column(nullable = false)
    private Long condominiumId;
    @Column(nullable = false)
    private String secretCode;
    @Column(nullable = true)
    private Integer limiteRegister;
    @Column(nullable = true)
    private boolean isDelete;

}
