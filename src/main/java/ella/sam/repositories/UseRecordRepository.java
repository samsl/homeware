package ella.sam.repositories;

import ella.sam.models.UseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UseRecordRepository extends JpaRepository<UseRecord, Long> {
    List<UseRecord> findByProductId(Long productId);
    @Query("select u from UseRecord u where u.product.id = ?1 and  u.endDate > ?2 and u.endDate < ?3")
    List<UseRecord> findIntersecionRecords(Long id, Date startDate, Date endDate);
    List<UseRecord> findByBookId(Long bookId);
}
