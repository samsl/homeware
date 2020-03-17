package ella.sam.services;

import ella.sam.models.ProductCategory;
import ella.sam.repositories.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository repository;

    public ProductCategory createProductCategory(ProductCategory productCategory){
        repository.save(productCategory);
        return productCategory;
    }

    public List<ProductCategory> getProductCategoryList() {
        return repository.findAll();
    }
}
