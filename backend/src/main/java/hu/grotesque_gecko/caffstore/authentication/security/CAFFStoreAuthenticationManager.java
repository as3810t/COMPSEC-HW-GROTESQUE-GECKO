package hu.grotesque_gecko.caffstore.authentication.security;

import hu.grotesque_gecko.caffstore.authentication.exceptions.AuthInvalidCredentialsException;
import hu.grotesque_gecko.caffstore.authorization.exceptions.AuthorizationException;
import hu.grotesque_gecko.caffstore.user.models.TemporaryPassword;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.user.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CAFFStoreAuthenticationManager implements AuthenticationManager {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public CAFFStoreAuthenticationManager(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();

        User user;
        if(principal instanceof String && credentials instanceof String) {
            String username = (String) principal;
            String password = (String) credentials;

            user = getUser(username);

            List<String> potentialPasswords = new ArrayList<>();
            if(user.getPassword() != null) {
                potentialPasswords.add(user.getPassword());
            }
            potentialPasswords.addAll(user.getTemporaryPasswords().stream()
                .filter(p -> p.getValidUntil().after(new Date()))
                .map(TemporaryPassword::getPassword)
                .collect(Collectors.toList())
            );

            if(potentialPasswords.stream().noneMatch(p -> passwordEncoder.matches(password, p))) {
                throw new AuthInvalidCredentialsException();
            }
        }
        else if(principal instanceof User && credentials == null) {
            user = (User) principal;
        }
        else {
            throw new AuthorizationException("invalid authentication");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    private User getUser(String usernameOrEmail) {
        Optional<User> userByName = userService.internalFindOneByUsername(usernameOrEmail);

        if(userByName.isPresent()) {
            return userByName.get();
        }
        else {
            Optional<User> userByEmail = userService.internalFindOneByEmail(usernameOrEmail);
            if(userByEmail.isPresent()) {
                return userByEmail.get();
            }
            else {
                throw new AuthInvalidCredentialsException();
            }
        }
    }
}
