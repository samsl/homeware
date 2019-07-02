package ella.sam.services;

import ella.sam.models.Product;
import ella.sam.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public List<Product> getProductList() {
        return repository.findAll();
    }

    public Product createProduct(Product product) {
        repository.save(product);
        return product;
    }
}

