package ella.sam.services;

import ella.sam.models.Celebrity;
import ella.sam.repositories.CelebrityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CelebrityService {
    @Autowired
    private CelebrityRepository repository;

    public List<Celebrity> getAllCelebrity() {
        return repository.findAll();
    }

    public Celebrity findCelebrityByName(String name) {
        return repository.findByName(name);
    }

    public Celebrity createCelebrity(Celebrity celebrity) {
        repository.save(celebrity);
        return celebrity;
    }
}
