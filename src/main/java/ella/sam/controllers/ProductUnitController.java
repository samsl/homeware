package ella.sam.controllers;

import ella.sam.models.ProductUnit;
import ella.sam.models.ResponseBean;
import ella.sam.services.ProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productUnits")
public class ProductUnitController {
    @Autowired
    private ProductUnitService service;

    @GetMapping
    public ResponseBean list() {
        return new ResponseBean(HttpStatus.OK.value(), "Success", service.getProductCapacityList());
    }
    @PostMapping
    public ResponseBean createProductCapacity(@RequestBody ProductUnit productUnit) {
        return new ResponseBean(HttpStatus.CREATED.value(), "Success", service.createProductCapacity(productUnit));
    }
}
