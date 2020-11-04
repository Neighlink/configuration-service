package pe.edu.upc.configurationservice.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.configurationservice.entities.Building;
import pe.edu.upc.configurationservice.repositories.BuildingRepository;
import pe.edu.upc.configurationservice.services.BuildingService;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public Building save(Building entity) throws Exception {
        return buildingRepository.save(entity);
    }

    @Override
    public List<Building> findAll() throws Exception {
        return buildingRepository.findAll();
    }

    @Override
    public Optional<Building> findById(Long aLong) throws Exception {
        return buildingRepository.findById(aLong);
    }

    @Override
    public Building update(Building entity) throws Exception {
        return buildingRepository.save(entity);
    }

    @Override
    public void deleteById(Long aLong) throws Exception {
        buildingRepository.deleteById(aLong);
    }

    @Override
    public Optional<List<Building>> findAllByCondominiumId(Long condominiumId) {
        return buildingRepository.findAllByCondominiumId(condominiumId);
    }
}
