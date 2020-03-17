package ella.sam.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "celebrity")
public class Celebrity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "directors", fetch = FetchType.LAZY)
    private Set<Movie> directedMovies;
    @JsonIgnore
    @ManyToMany(mappedBy = "playwrights", fetch = FetchType.LAZY)
    private Set<Movie> writedMovies;
    @JsonIgnore
    @ManyToMany(mappedBy = "cast", fetch = FetchType.LAZY)
    private Set<Movie> playedMovies;

    public Celebrity() {
    }

    public Celebrity(String name) {
        this.name = name;
    }

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

    public Set<Movie> getDirectedMovies() {
        return directedMovies;
    }

    public void setDirectedMovies(Set<Movie> directedMovies) {
        this.directedMovies = directedMovies;
    }

    public Set<Movie> getWritedMovies() {
        return writedMovies;
    }

    public void setWritedMovies(Set<Movie> writedMovies) {
        this.writedMovies = writedMovies;
    }

    public Set<Movie> getPlayedMovies() {
        return playedMovies;
    }

    public void setPlayedMovies(Set<Movie> playedMovies) {
        this.playedMovies = playedMovies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Celebrity celebrity = (Celebrity) o;
        return name.equals(celebrity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
