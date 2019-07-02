package ella.sam.controllers;

import ella.sam.models.ResponseBean;

import ella.sam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseBean list() {
        return new ResponseBean(HttpStatus.OK.value(), "Success", userService.getUserList());
    }
}
