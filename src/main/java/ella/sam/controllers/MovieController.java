package ella.sam.controllers;


import ella.sam.models.Movie;
import ella.sam.models.QueryBody;
import ella.sam.models.ResponseBean;
import ella.sam.models.User;
import ella.sam.models.WatchedRecord;
import ella.sam.query.MovieQuerier;
import ella.sam.services.MovieService;
import ella.sam.services.UserService;
import ella.sam.shiro.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieQuerier movieQuerier;

    @Autowired
    private UserService userService;

    @DeleteMapping("/{id}")
    public ResponseBean deleteById(@PathVariable Long id) {
        movieService.deleteById(id);
        return new ResponseBean(200, "Success", null);
    }

    @GetMapping()
    public ResponseBean getWatchedMovie() {
        User user = decodeToken();
        return new ResponseBean(200, "message", movieService.findMovieByUserId(user.getId()));
    }


    @PostMapping("/search")
    public ResponseBean list(@RequestParam(value = "from", required = false) Integer from, @RequestParam(value = "size", required = false) Integer size, @RequestBody QueryBody queryBody) {
        if (queryBody.getAggregation() != null) {
            return new ResponseBean(200, "Success",movieQuerier.getFieldSet(from, size, queryBody));
        }
        return new ResponseBean(200, "Success", movieQuerier.browseMovie(from, size, queryBody));
    }

    @PostMapping
    public ResponseBean create(@RequestBody WatchedRecord watchedRecord) {
        User user = decodeToken();
//        if (user.getId() != watchedRecord.getUserId()) {
//            throw new RuntimeException("You have no permission to read watching records");
//        }
        Movie movie = new Movie();
        movie.setMovieId(watchedRecord.getMovieId());
        movie.setUser(userService.findUserById(user.getId()));
        movie.setWatchedDate(watchedRecord.getWatchedDate());
        return new ResponseBean(201, "Success",movieService.createMovie(movie));

    }

    private User decodeToken() {
        String token = request.getHeader("x-auth-token");
        String openid = JwtUtil.getOpenid(token);
        if (openid!=null) {
            return userService.findByOpenid(openid);
        }
        String username = JwtUtil.getUsername(token);
        if (username != null) {
            return userService.findUserByUsername(username);
        }
        throw new RuntimeException("Token is invalid");
    }
}
