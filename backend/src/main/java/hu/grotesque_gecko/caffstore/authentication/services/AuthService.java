package hu.grotesque_gecko.caffstore.authentication.services;

import hu.grotesque_gecko.caffstore.authentication.token.JwtTokenProvider;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @Transactional
    public User authWithUsernameAndPassword(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        return (User) authentication.getPrincipal();
    }

    @Transactional
    public User authWithEmailAndPassword(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        return (User) authentication.getPrincipal();
    }

    public String generateToken(User user) {
        return jwtTokenProvider.createToken(user);
    }

    @Transactional
    public User register(String username, String email, String password) {
        return userService.internalCreateOne(username, email, password);
    }

    @Transactional
    public void resetPassword(String username, String email) {
        Optional<User> userByName = userService.internalFindOneByUsername(username);
        Optional<User> userByEmail = userService.internalFindOneByUsername(email);

        if(userByName.isPresent()) {
            userService.internalCreateTemporaryPassword(userByName.get());
        }
        else if(userByEmail.isPresent()) {
            userService.internalCreateTemporaryPassword(userByEmail.get());
        }
    }

    @Transactional
    public void logout(User currentUser) {
        userService.internalResetCredentialValidity(currentUser);
    }
}
