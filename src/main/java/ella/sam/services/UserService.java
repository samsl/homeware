package ella.sam.services;

import ella.sam.models.User;
import ella.sam.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

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

    public User createUser(User user) {
        String salt = generateSalt();
        user.setSalt(salt);
        String password = hashPassword(user.getPassword(), user.getSalt());
        user.setPassword(password);
        return repository.save(user);
    }

    public User findByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }

    private String generateSalt() {
        String CHARS = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++ ) {
            int index = random.nextInt(CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
    }

    private String hashPassword(String password, String salt) {
        if (password == null) {
            return "";
        }
        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
        MessageDigest md;
        try {
            // Select the message digest for the hash computation -> SHA-256
            md = MessageDigest.getInstance("SHA-256");


            // Passing the salt to the digest for the computation
            md.update(saltBytes);

            // Generate the salted hash
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword)
                sb.append(String.format("%02x", b));

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException("Hash password failed", e);
        }
    }
}
