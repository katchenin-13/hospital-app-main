package ma.enset.hospitalapp;

import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repository.PatientRepository;
import ma.enset.hospitalapp.security.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.util.Date;

@SpringBootApplication
public class HospitalAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalAppApplication.class, args);
    }

    //@Bean
    CommandLineRunner start(PatientRepository patientRepository){
        return args -> {
            patientRepository.save(new Patient(null,"Mohamed",new Date(),false,42));
            patientRepository.save(new Patient(null,"Imane",new Date(),true,98));
            patientRepository.save(new Patient(null,"Yassine",new Date(),true,342));
            patientRepository.save(new Patient(null,"Laila",new Date(),false,123));
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //@Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager){
       PasswordEncoder passwordEncoder = passwordEncoder();
       return args -> {
           UserDetails u1 = jdbcUserDetailsManager.loadUserByUsername("user11");
         if (u1 == null)
             jdbcUserDetailsManager.createUser(
                     User.withUsername("user11").password(passwordEncoder.encode("1234")).roles("USER").build()
             );
         UserDetails u2 = jdbcUserDetailsManager.loadUserByUsername("admin1");
         if (u2 == null)
            jdbcUserDetailsManager.createUser(
                    User.withUsername("admin1").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()

            );
       };
    }
   //@Bean
    CommandLineRunner commandLineRunner(AccountService accountService){
        return args -> {
            accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");
            accountService.addNewUser("user1","1234","email@gmail.com","1234");
            accountService.addNewUser("admin","1234","email@gmail.com","1234");

            accountService.addRoleToUser("user1","USER");
            accountService.addRoleToUser("admin","ADMIN");
            accountService.addRoleToUser("admin","USER");
        };
    }

}
