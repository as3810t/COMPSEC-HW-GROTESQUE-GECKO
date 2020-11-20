package hu.grotesque_gecko.caffstore.utils;

import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.user.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
	//...
    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //...

        this.users.deleteAll();

        this.users.save(User.builder()
            .username("user")
            .email("user@localhost")
            .password(this.passwordEncoder.encode("user"))
            .isAdmin(false)
            .build()
        );
        this.users.save(User.builder()
            .username("admin")
            .email("admin@localhost")
            .password(this.passwordEncoder.encode("admin"))
            .isAdmin(true)
            .build()
        );

        log.debug("printing all users...");
        this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}