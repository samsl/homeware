package ella.sam.controllers;

import ella.sam.models.ProductCategory;
import ella.sam.models.ResponseBean;
import ella.sam.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productCategories")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService service;

    @GetMapping
    public ResponseBean list() {
        return new ResponseBean(HttpStatus.OK.value(), "Success", service.getProductCategoryList());
    }

    @PostMapping
    public ResponseBean createProductCategory(@RequestBody ProductCategory productCategory) {
        return new ResponseBean(HttpStatus.OK.value(), "Success", service.createProductCategory(productCategory));
    }
}
