package ella.sam.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private short year;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name="movie_genre", joinColumns=@JoinColumn(name="movie_id"), inverseJoinColumns = @JoinColumn(name="genre_id"))
    private Set<Genre> genres;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name="movie_director", joinColumns = @JoinColumn(name="movie_id"), inverseJoinColumns = @JoinColumn(name="director_id"))
    private Set<Celebrity> directors;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name="movie_playwright", joinColumns = @JoinColumn(name="movie_id"), inverseJoinColumns = @JoinColumn(name="playwright_id"))
    private Set<Celebrity> playwrights;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name="movie_cast", joinColumns = @JoinColumn(name="movie_id"), inverseJoinColumns = @JoinColumn(name="cast_id"))
    private Set<Celebrity> cast;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name="movie_region", joinColumns = @JoinColumn(name="movie_id"), inverseJoinColumns = @JoinColumn(name="region_id"))
    private Set<Region> regions;
    private String post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Celebrity> getDirectors() {
        return directors;
    }

    public void setDirectors(Set<Celebrity> directors) {
        this.directors = directors;
    }

    public Set<Celebrity> getPlaywrights() {
        return playwrights;
    }

    public void setPlaywrights(Set<Celebrity> playwrights) {
        this.playwrights = playwrights;
    }

    public Set<Celebrity> getCast() {
        return cast;
    }

    public void setCast(Set<Celebrity> cast) {
        this.cast = cast;
    }

    public Set<Region> getRegions() {
        return regions;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
