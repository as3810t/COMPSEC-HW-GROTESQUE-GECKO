package hu.grotesque_gecko.caffstore.controllers;

import hu.grotesque_gecko.caffstore.dto.UserDTO;
import hu.grotesque_gecko.caffstore.dto.UserListResponse;
import hu.grotesque_gecko.caffstore.models.User;
import hu.grotesque_gecko.caffstore.repositories.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

import static hu.grotesque_gecko.caffstore.utils.Preconditions.checkPermission;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody UserListResponse getAll(
        @AuthenticationPrincipal User currentUser,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "100") int pageSize
    ) {
        checkPermission(currentUser.isAdmin());

        Page<User> users = userRepository.findAll(PageRequest.of(offset, pageSize));

        return UserListResponse.builder()
            .users(users.getContent().stream().map(this::userToDTO).collect(Collectors.toList()))
            .totalCount((int) users.getTotalElements())
            .offset(offset)
            .pageSize(pageSize)
            .build();
    }

    @GetMapping("/me")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody UserDTO getMe(
        @AuthenticationPrincipal User currentUser
    ) {
        return userToDTO(currentUser);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public @ResponseBody UserDTO getOne(
        @AuthenticationPrincipal User currentUser,
        @RequestParam String id
    ) {
        checkPermission(currentUser.isAdmin(), currentUser.getId().equals(id));

        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new RuntimeException();
        }

        return userToDTO(user.get());
    }

    private UserDTO userToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
}
