package hu.grotesque_gecko.caffstore.api;

import hu.grotesque_gecko.caffstore.api.dto.UserDTO;
import hu.grotesque_gecko.caffstore.api.dto.UserListDTO;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.user.services.UserService;
import hu.grotesque_gecko.caffstore.utils.Paginated;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody
    UserListDTO getAll(
        @AuthenticationPrincipal User currentUser,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "100") int pageSize
    ) {
        Paginated<User> users = userService.getAll(currentUser, offset, pageSize);

        return UserListDTO.builder()
            .users(users.getEntities().stream().map(this::userToDTO).collect(Collectors.toList()))
            .totalCount((int) users.getTotalCount())
            .offset(offset)
            .pageSize(pageSize)
            .build();
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody UserDTO createOne(
        @AuthenticationPrincipal User currentUser,
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password
    ) {
        User newUser = userService.createOne(
            currentUser,
            username,
            email,
            password
        );

        return userToDTO(newUser);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody UserDTO getMe(
        @AuthenticationPrincipal User currentUser
    ) {
        return userToDTO(currentUser);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody UserDTO getOne(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        return userToDTO(userService.getOneById(currentUser, id));
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody UserDTO editOne(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id,
        @RequestParam(required = false, defaultValue = "") String username,
        @RequestParam(required = false, defaultValue = "") String email,
        @RequestParam(required = false, defaultValue = "") String password
    ) {
        User newUser = userService.editOne(
            currentUser,
            id,
            username,
            email,
            password
        );

        return userToDTO(newUser);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody void deleteOne(
        @AuthenticationPrincipal User currentUser,
        @PathVariable String id
    ) {
        userService.deleteOne(currentUser, id);
    }

    private UserDTO userToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
}
