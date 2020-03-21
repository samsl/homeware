package ella.sam.services;

import ella.sam.models.Movie;
import ella.sam.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository repository;

    public List<Movie> getMovieList() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Movie createMovie(Movie movie) {
        repository.save(movie);
        return movie;
    }

    public List<Movie> findMovieByUserId(Long id) {
        return repository.findByUserId(id);
    }

}
