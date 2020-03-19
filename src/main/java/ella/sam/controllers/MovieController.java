package ella.sam.controllers;


import ella.sam.models.Celebrity;
import ella.sam.models.Genre;
import ella.sam.models.Movie;
import ella.sam.models.MovieDTO;
import ella.sam.models.QueryBody;
import ella.sam.models.Region;
import ella.sam.models.ResponseBean;
import ella.sam.query.MovieQuerier;
import ella.sam.services.CelebrityService;
import ella.sam.services.GenreService;
import ella.sam.services.MovieService;
import ella.sam.services.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private CelebrityService celebrityService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private MovieQuerier movieQuerier;

    @DeleteMapping("/{id}")
    public ResponseBean deleteById(@PathVariable Long id) {
        movieService.deleteById(id);
        return new ResponseBean(200, "Success", null);
    }

    @GetMapping
    public ResponseBean list(@RequestParam(value = "from", required = false) Integer from, @RequestParam(value = "size", required = false) Integer size, @RequestBody QueryBody queryBody) {
        if (queryBody.getAggregation() != null) {
            return new ResponseBean(200, "Success",movieQuerier.getFieldSet(from, size, queryBody));
        }
        return new ResponseBean(200, "Success", movieQuerier.browseMovie(from, size, queryBody));
    }

    @PostMapping
    public ResponseBean create(@RequestBody MovieDTO movieDTO) {

        Map<String, Celebrity> celebrityMap = new HashMap<>();
        Set<String> directorNames = movieDTO.getDirector();
        Set<Celebrity> directors = new HashSet<>();
        directorNames.forEach(d -> {
            Celebrity celebrity = celebrityService.findCelebrityByName(d);
            if (celebrity == null) {
                if (celebrityMap.containsKey(d)) {
                    celebrity = celebrityMap.get(d);
                } else {
                    celebrity = new Celebrity(d);
                    celebrityMap.put(d, celebrity);
                }
            }
            directors.add(celebrity);
        });

        Set<String> playwrightNames = movieDTO.getPlaywright();
        Set<Celebrity> playwrights = new HashSet<>();
        playwrightNames.forEach(d -> {
            Celebrity celebrity = celebrityService.findCelebrityByName(d);
            if (celebrity == null) {
                if (celebrityMap.containsKey(d)) {
                    celebrity = celebrityMap.get(d);
                } else {
                    celebrity = new Celebrity(d);
                    celebrityMap.put(d, celebrity);
                }
            }
            playwrights.add(celebrity);
        });

        Set<String> castNames = movieDTO.getCast();
        Set<Celebrity> cast = new HashSet<>();
        castNames.forEach(d -> {
            Celebrity celebrity = celebrityService.findCelebrityByName(d);
            if (celebrity == null) {
                if (celebrityMap.containsKey(d)) {
                    celebrity = celebrityMap.get(d);
                } else {
                    celebrity = new Celebrity(d);
                    celebrityMap.put(d, celebrity);
                }
            }
            cast.add(celebrity);
        });

        Set<String> genreNames = movieDTO.getGenres();
        Set<Genre> genres = new HashSet<>();
        genreNames.forEach(g -> {
            Genre genre = genreService.findByName(g);
            if (genre == null) {
                genre = new Genre(g);
            }
            genres.add(genre);
        });

        Set<String> regionNames = movieDTO.getRegions();
        Set<Region> regions = new HashSet<>();
        regionNames.forEach(d -> {
            Region region = regionService.findByName(d);
            if (region == null) {
                region = new Region(d);
            }
            regions.add(region);
        });

        Movie movie = new Movie();
        movie.setName(movieDTO.getName());
        movie.setPost(movieDTO.getPost());
        movie.setYear(movieDTO.getYear());
        movie.setGenres(genres);
        movie.setDirectors(directors);
        movie.setPlaywrights(playwrights);
        movie.setCast(cast);
        movie.setRegions(regions);
        return new ResponseBean(200, "Success", movieService.createMovie(movie));


    }
}
