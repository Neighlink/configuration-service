package pe.edu.upc.configurationservice.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.configurationservice.entities.Department;
import pe.edu.upc.configurationservice.repositories.DepartmentRepository;
import pe.edu.upc.configurationservice.services.DepartmentService;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department save(Department entity) throws Exception {
        return departmentRepository.save(entity);
    }

    @Override
    public List<Department> findAll() throws Exception {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> findById(Long aLong) throws Exception {
        return departmentRepository.findById(aLong);
    }

    @Override
    public Department update(Department entity) throws Exception {
        return departmentRepository.save(entity);
    }

    @Override
    public void deleteById(Long aLong) throws Exception {
        departmentRepository.deleteById(aLong);
    }

    @Override
    public Optional<List<Department>> findAllByBuildingId(Long buildingId) {
        return departmentRepository.findAllByBuildingId(buildingId);
    }
}
