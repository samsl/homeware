package ella.sam.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1603552780244047129L;
    @Id
    @GeneratedValue
    private long id;
    private String role;

    @ManyToMany
    @JoinTable(name="role_permission", joinColumns = {@JoinColumn(name="rid")}, inverseJoinColumns = {@JoinColumn(name="pid")})
    private List<Permission> permissions;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
