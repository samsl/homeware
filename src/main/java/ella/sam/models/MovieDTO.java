package ella.sam.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    private String name;
    private Set<String> director;
    private Set<String> playwright;
    private Set<String> regions;
    private Set<String> genres;
    private Set<String> cast;
    private short year;
    private String post;
    private double rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getDirector() {
        return director;
    }

    public void setDirector(Set<String> director) {
        this.director = director;
    }

    public Set<String> getPlaywright() {
        return playwright;
    }

    public void setPlaywright(Set<String> playwright) {
        this.playwright = playwright;
    }

    public Set<String> getRegions() {
        return regions;
    }

    public void setRegions(Set<String> regions) {
        this.regions = regions;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public Set<String> getCast() {
        return cast;
    }

    public void setCast(Set<String> cast) {
        this.cast = cast;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
