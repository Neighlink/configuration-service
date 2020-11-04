package pe.edu.upc.configurationservice.services;

import org.springframework.data.repository.query.Param;
import pe.edu.upc.configurationservice.entities.Building;

import java.util.List;
import java.util.Optional;

public interface BuildingService extends CrudService<Building, Long> {
    Optional<List<Building>> findAllByCondominiumId(Long condominiumId);
}
