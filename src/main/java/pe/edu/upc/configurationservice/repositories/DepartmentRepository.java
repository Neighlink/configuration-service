package pe.edu.upc.configurationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.configurationservice.entities.Building;
import pe.edu.upc.configurationservice.entities.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT d FROM Department d WHERE d.buildingId = :buildingId AND d.isDelete = false")
    Optional<List<Department>> findAllByBuildingId(@Param("buildingId") Long buildingId);

    @Query("SELECT d FROM Department d WHERE d.secretCode = :code AND d.isDelete = false")
    Optional<Department> findByCode(@Param("code") String code);
}
