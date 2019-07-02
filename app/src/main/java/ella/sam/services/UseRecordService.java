package ella.sam.services;

import ella.sam.models.Book;
import ella.sam.models.Product;
import ella.sam.models.UseRecord;
import ella.sam.models.UseRecordStatus;
import ella.sam.repositories.BookRepository;
import ella.sam.repositories.ProductRepository;
import ella.sam.repositories.UseRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UseRecordService {
    @Autowired
    private UseRecordRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<UseRecord> findAll() {
        return repository.findAll();
    }

    public List<UseRecord> findByProductId(Long productId) {
        return repository.findByProductId(productId);
    }

    public List<UseRecord> findByBookId(Long bookId) {
        return repository.findByBookId(bookId);
    }

    public UseRecord createUseRecord(UseRecord useRecord) {
        updateStatus(useRecord);
        repository.save(useRecord);
        return useRecord;
    }

    public UseRecord updateUseRecord(UseRecord useRecord) {
        updateStatus(useRecord);

        return useRecord;
    }

    private Date cutoffTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            throw new RuntimeException("date format not correct {0}", e);
        }
    }

    private void updateStatus(UseRecord useRecord) {
        if (useRecord.getEndDate() != null) {
            long span = TimeUnit.DAYS.convert(cutoffTime(useRecord.getEndDate()).getTime() - cutoffTime(useRecord.getStartDate()).getTime(), TimeUnit.MILLISECONDS) + 1;
            Book book = useRecord.getBook();
            Product product = useRecord.getProduct();
            double speed;
            if (product != null) {
                product = productRepository.findById(product.getId()).orElse(null);
                Objects.requireNonNull(product);
                speed = product.getCapacity() * 1.0 / span;
                useRecord.setSpeed(speed);
                useRecord.setStatus(UseRecordStatus.DONE);
                repository.save(useRecord);

                List<UseRecord> records = repository.findIntersecionRecords(product.getId(), useRecord.getStartDate(), useRecord.getEndDate());
                double productSpeed = 0;
                for (UseRecord record : records) {
                    productSpeed += record.getSpeed();
                }
                if (product.getSpeed() != 0) {
                    product.setSpeed((product.getSpeed() + productSpeed) * 1.0 / 2);
                } else {
                    product.setSpeed(speed);
                }
                product.setAmmount(product.getAmmount() - 1);
                productRepository.save(product);
            } else {
                book = bookRepository.findById(book.getId()).orElse(null);
                speed = book.getPages() * 1.0 / span;
                useRecord.setSpeed(speed);
                useRecord.setStatus(UseRecordStatus.DONE);
                repository.save(useRecord);
                book.setSpeed(speed);
            }



        } else {
            useRecord.setStatus(UseRecordStatus.NEW);
        }
    }
}
