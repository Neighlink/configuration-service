package pe.edu.upc.configurationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.configurationservice.entities.Building;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query("SELECT b FROM Building b WHERE b.condominiumId = :condominiumId AND b.isDelete = false")
    Optional<List<Building>> findAllByCondominiumId(@Param("condominiumId") Long condominiumId);
}
