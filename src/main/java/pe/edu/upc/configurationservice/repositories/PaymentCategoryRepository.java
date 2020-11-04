package pe.edu.upc.configurationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.configurationservice.entities.PaymentCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCategoryRepository extends JpaRepository<PaymentCategory, Long> {
    @Query("SELECT p FROM PaymentCategory p WHERE p.condominiumId = :condominiumId AND p.isDelete = false")
    Optional<List<PaymentCategory>> findAllByCondominiumId(@Param("condominiumId") Long condominiumId);
}
