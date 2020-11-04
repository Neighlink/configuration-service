package pe.edu.upc.configurationservice.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "paymentCategories")
@Data
public class PaymentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    @Column(nullable = false)
    private Long condominiumId;
    @Column(nullable = false)
    private boolean isDelete;
}
