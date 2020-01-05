package ella.sam.repositories;

import ella.sam.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenresId(Integer id);
    List<Movie> findByGenresName(String id);
}
