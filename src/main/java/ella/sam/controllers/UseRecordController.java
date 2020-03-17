package ella.sam.controllers;

import ella.sam.models.ResponseBean;
import ella.sam.models.UseRecord;
import ella.sam.services.UseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/useRecords")
public class UseRecordController {
    @Autowired
    private UseRecordService service;

    @GetMapping
    public ResponseBean getUseRecordsByProductId(@RequestParam(value = "productId", required = false) Long productId, @RequestParam(value = "bookId", required = false) Long bookId) {
        if (productId != null) {
            return new ResponseBean(HttpStatus.OK.value(), "Success", service.findByProductId(productId));
        } else if (bookId != null) {
            return new ResponseBean(HttpStatus.OK.value(), "Success", service.findByBookId(bookId));
        } else {
            return new ResponseBean(HttpStatus.OK.value(), "Success", service.findAll());
        }
    }

    @PostMapping
    public ResponseBean createUseRecord(@RequestBody UseRecord useRecord) {
        return new ResponseBean(HttpStatus.CREATED.value(), "Success", service.createUseRecord(useRecord));

    }
    @PutMapping("/{useRecordId}")
    public ResponseBean updateUseRecord(@RequestBody UseRecord useRecord) {
        return new ResponseBean(HttpStatus.OK.value(), "Success", service.updateUseRecord(useRecord));
    }

}
