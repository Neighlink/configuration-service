package pe.edu.upc.configurationservice.services;

import org.springframework.data.repository.query.Param;
import pe.edu.upc.configurationservice.entities.PaymentCategory;

import java.util.List;
import java.util.Optional;

public interface PaymentCategoryService extends CrudService<PaymentCategory, Long> {
    Optional<List<PaymentCategory>> findAllByCondominiumId(Long condominiumId);
}
