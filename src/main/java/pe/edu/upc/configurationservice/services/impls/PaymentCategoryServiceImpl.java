package pe.edu.upc.configurationservice.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.configurationservice.entities.PaymentCategory;
import pe.edu.upc.configurationservice.repositories.PaymentCategoryRepository;
import pe.edu.upc.configurationservice.services.PaymentCategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentCategoryServiceImpl implements PaymentCategoryService {
    @Autowired
    private PaymentCategoryRepository paymentCategoryRepository;

    @Override
    public PaymentCategory save(PaymentCategory entity) throws Exception {
        return paymentCategoryRepository.save(entity);
    }

    @Override
    public List<PaymentCategory> findAll() throws Exception {
        return paymentCategoryRepository.findAll();
    }

    @Override
    public Optional<PaymentCategory> findById(Long aLong) throws Exception {
        return paymentCategoryRepository.findById(aLong);
    }

    @Override
    public PaymentCategory update(PaymentCategory entity) throws Exception {
        return paymentCategoryRepository.save(entity);
    }

    @Override
    public void deleteById(Long aLong) throws Exception {
        paymentCategoryRepository.deleteById(aLong);
    }

    @Override
    public Optional<List<PaymentCategory>> findAllByCondominiumId(Long condominiumId) {
        return paymentCategoryRepository.findAllByCondominiumId(condominiumId);
    }
}
