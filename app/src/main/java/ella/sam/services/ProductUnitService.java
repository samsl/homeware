package ella.sam.services;

import ella.sam.models.ProductUnit;
import ella.sam.repositories.ProductUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitService {
    @Autowired
    private ProductUnitRepository repository;

    public ProductUnit createProductCapacity(ProductUnit productUnit) {
        repository.save(productUnit);
        return productUnit;
    }

    public List<ProductUnit> getProductCapacityList() {
        return repository.findAll();
    }
}
