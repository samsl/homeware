package ella.sam.repositories;

import ella.sam.models.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CelebrityRepository extends JpaRepository<Celebrity, Long> {
    Celebrity findByName(String name);
}
