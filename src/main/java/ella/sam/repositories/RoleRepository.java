package ella.sam.repositories;

import ella.sam.models.Permission;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Permission, Long> {
}
