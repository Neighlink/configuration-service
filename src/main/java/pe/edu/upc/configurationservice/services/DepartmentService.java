package pe.edu.upc.configurationservice.services;

import org.springframework.data.repository.query.Param;
import pe.edu.upc.configurationservice.entities.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService extends CrudService<Department, Long> {
    Optional<List<Department>> findAllByBuildingId(Long buildingId);
}
