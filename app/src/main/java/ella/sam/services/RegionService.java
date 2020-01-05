package ella.sam.services;

import ella.sam.models.Region;
import ella.sam.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {
    @Autowired
    private RegionRepository repository;

    public List<Region> getAllRegions() {
        return repository.findAll();
    }

    public Region findByName(String name) {
        return repository.findByName(name);
    }

    public Region createRegion(Region region) {
        repository.save(region);
        return region;
    }
}
