package ella.sam.services;

import ella.sam.models.Book;
import ella.sam.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    public List<Book> getBookList() {
        return repository.findAll();
    }

    public Book createBook(Book book) {
        repository.save(book);
        return book;
    }

}
