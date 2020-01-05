package ella.sam.services;

import ella.sam.models.Genre;
import ella.sam.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    @Autowired
    private GenreRepository repository;

    public List<Genre> getGenreList() {
        return repository.findAll();
    }

    public Genre createGenre(Genre genre) {
        repository.save(genre);
        return genre;
    }

    public Genre findByName(String name) {
        return repository.findByName(name);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}
