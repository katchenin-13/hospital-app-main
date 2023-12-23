package ma.enset.hospitalapp.security.services;

import lombok.AllArgsConstructor;
import ma.enset.hospitalapp.security.entities.AppRole;
import ma.enset.hospitalapp.security.entities.AppUser;
import ma.enset.hospitalapp.security.repos.AppRoleRepository;
import ma.enset.hospitalapp.security.repos.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
       AppUser appUser = appUserRepository.findByUsername(username);
       if ( appUser != null) throw new RuntimeException("this user alrealy exist");
       if (!password.equals(confirmPassword)) throw new RuntimeException("Password not match");
       appUser = AppUser.builder()
               .userId(UUID.randomUUID().toString())
               .username(username)
               .password(passwordEncoder.encode(password))
               .email(email)
               .build();
        AppUser saveAppUser = appUserRepository.save(appUser);
        return saveAppUser;
    }

    @Override
    public AppRole addNewRole(String role) {
       AppRole appRole = appRoleRepository.findById(role).orElse(null);
        if (appRole!= null)throw new RuntimeException("this role already exist");
        appRole = AppRole.builder()
                .role(role)
                .build();
         AppRole saveAppRole = appRoleRepository.save(appRole);
        return saveAppRole;
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().add(appRole);
    }

    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().remove(appRole);

    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
