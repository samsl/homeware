package ella.sam.controllers;

import ella.sam.models.Book;
import ella.sam.models.ResponseBean;
import ella.sam.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService service;

    @GetMapping
    public ResponseBean list() {
        return new ResponseBean(200, "Success", service.getBookList());
    }

    @PostMapping
    public ResponseBean create(@RequestBody Book book) {
        return new ResponseBean(201, "Success", service.createBook(book));
    }
}
