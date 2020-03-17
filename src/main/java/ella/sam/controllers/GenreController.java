package ella.sam.controllers;

import ella.sam.models.Genre;
import ella.sam.models.Movie;
import ella.sam.models.ResponseBean;
import ella.sam.services.GenreService;
import ella.sam.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private GenreService service;

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseBean list() {
        return new ResponseBean(200, "Success", service.getGenreList());
    }

    @PostMapping
    public ResponseBean create(@RequestBody Genre genre) {
        return new ResponseBean(201, "Success", service.createGenre(genre));
    }

    @DeleteMapping("/{id}")
    public ResponseBean deleteById(@PathVariable Integer id) {
        List<Movie> movies = movieService.getMovieListByGenreId(id);
        for (Movie movie : movies) {
            Set<Genre> genres = new HashSet<>();
            movie.getGenres().forEach(g->{
                if (g.getId() != id) {
                    genres.add(g);
                }
            });
            movie.setGenres(genres);

        }
        service.deleteById(id);
        return new ResponseBean(200, "Success", null);
    }
}
