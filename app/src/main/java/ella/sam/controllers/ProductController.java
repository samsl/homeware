package ella.sam.controllers;

import ella.sam.models.ParsedProduct;
import ella.sam.models.Product;
import ella.sam.models.ResponseBean;
import ella.sam.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping("parse")
    public ResponseBean parseUrl(@RequestParam String url) {
        ParsedProduct parsedProduct = new ParsedProduct(url);
        return new ResponseBean(200, "Success", parsedProduct);
    }

    @Autowired
    private ProductService service;
    @GetMapping
    public ResponseBean list(){
        return new ResponseBean(200, "Success", service.getProductList());
    }

    @PostMapping
    public ResponseBean create(@RequestBody Product product) {
        return new ResponseBean(HttpStatus.CREATED.value(), "Success", service.createProduct(product));
    }
}
