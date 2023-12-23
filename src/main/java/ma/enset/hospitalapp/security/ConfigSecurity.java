package ma.enset.hospitalapp.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor

public class ConfigSecurity {


    private PasswordEncoder passwordEncoder;
  //@Autowired
    private UserDetailsService userDetailsService;

    //authentification avec InMemoryUserDetailsManager non connecter a une base de donnees
    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager (){
        return  new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode( "1234")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()

        );
    }

    //Authentification avec JDBC (jdbcUserDetailsManager) connection avec une base sql relation
    //@Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    //gestion ou filtrage de requete http des la solution (gestion des role ou autorisation des acces a la base de donnne ou au different resources)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll();
        httpSecurity.rememberMe();
        httpSecurity.authorizeHttpRequests().requestMatchers("/webjars/**","/h2-console/**").permitAll();
        //httpSecurity.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER");
       // httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("USER,ADMIN");
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        httpSecurity.exceptionHandling().accessDeniedPage("/notAuthorise");
        httpSecurity.userDetailsService(userDetailsService);
        return httpSecurity.build();
    }
}
