package ella.sam.services;

import ella.sam.models.User;
import ella.sam.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;
    public User findUserById(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.orElse(null);
    }
    public List<User> getUserList() {
        return repository.findAll();
    }
    public User findUserByUsername(String name) {
        return repository.findByUsername(name);
    }
}
