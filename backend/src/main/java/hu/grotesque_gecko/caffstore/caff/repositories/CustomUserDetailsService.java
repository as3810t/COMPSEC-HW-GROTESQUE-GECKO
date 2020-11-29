package hu.grotesque_gecko.caffstore.caff.repositories;

import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.user.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userByUsername = this.users.findByUsername(username);
        if(userByUsername.isPresent()) {
            return userByUsername.get();
        }

        Optional<User> userByEmail = this.users.findByEmail(username);
        if(userByEmail.isPresent()) {
            return userByEmail.get();
        }

        throw new UsernameNotFoundException("Username: " + username + " not found");
    }
}