package hu.grotesque_gecko.caffstore.api;

import hu.grotesque_gecko.caffstore.api.dto.LoginDTO;
import hu.grotesque_gecko.caffstore.authentication.exceptions.AuthInvalidCredentialsException;
import hu.grotesque_gecko.caffstore.authentication.services.AuthService;
import hu.grotesque_gecko.caffstore.user.models.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody LoginDTO login(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String email,
        @RequestParam String password
    ) {
        User user;
        if(username != null) {
            user = authService.authWithUsernameAndPassword(username, password);
        }
        else if(email != null) {
            user = authService.authWithEmailAndPassword(email, password);
        }
        else {
            throw new AuthInvalidCredentialsException();
        }

        String token = authService.generateToken(user);

        return LoginDTO.builder()
            .username(user.getUsername())
            .token(token)
            .roles(user.getRoles())
            .build();
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody LoginDTO register(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password
    ) {
        User user = authService.register(username, email, password);
        String token = authService.generateToken(user);

        return LoginDTO.builder()
            .username(user.getUsername())
            .token(token)
            .roles(user.getRoles())
            .build();
    }

    @PostMapping(value = "/passwordReset", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void register(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String email
    ) {
        authService.resetPassword(username, email);
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public void logout(
        @AuthenticationPrincipal User currentUser
    ) {
        authService.logout(currentUser);
    }
}