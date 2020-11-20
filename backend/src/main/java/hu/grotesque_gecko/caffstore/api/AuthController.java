package hu.grotesque_gecko.caffstore.api;

import hu.grotesque_gecko.caffstore.api.dto.LoginDTO;
import hu.grotesque_gecko.caffstore.api.dto.LoginResponse;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.user.repositories.UserRepository;
import hu.grotesque_gecko.caffstore.authentication.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @PostMapping("/login")
    public @ResponseBody LoginResponse login(@RequestBody LoginDTO data) {
        try {
            if(data.getUsername() != null) {
                String username = data.getUsername();

                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

                User user = this.users
                    .findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found"));
                String token = jwtTokenProvider.createToken(username, user.getRoles());

                return new LoginResponse(username, token, user.getRoles());
            }
            else if(data.getEmail() != null) {
                String email = data.getEmail();

                User user = this.users
                    .findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email " + email + "not found"));

                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), data.getPassword()));

                String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

                return new LoginResponse(user.getUsername(), token, user.getRoles());
            }
            else {
                throw new BadCredentialsException("Invalid username/password supplied");
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}