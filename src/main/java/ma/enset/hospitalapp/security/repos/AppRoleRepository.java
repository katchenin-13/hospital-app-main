package ma.enset.hospitalapp.security.repos;

import ma.enset.hospitalapp.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,String > {
}
