package hu.grotesque_gecko.caffstore.user.services;

import hu.grotesque_gecko.caffstore.authorization.services.AuthorizeService;
import hu.grotesque_gecko.caffstore.utils.ValidationException;
import hu.grotesque_gecko.caffstore.user.exceptions.UserAlreadyExistsException;
import hu.grotesque_gecko.caffstore.user.exceptions.UserDoesNotExistsException;
import hu.grotesque_gecko.caffstore.user.models.User;
import hu.grotesque_gecko.caffstore.user.repositories.UserRepository;
import hu.grotesque_gecko.caffstore.utils.Paginated;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static hu.grotesque_gecko.caffstore.utils.Preconditions.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorizeService authorizeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Paginated<User> getAll(User currentUser, int offset, int pageSize) {
        authorizeService.canListAllUsers(currentUser);
        checkPagination(offset, pageSize);

        Page<User> users = userRepository.findAll(PageRequest.of(offset, pageSize));

        return new Paginated<>(users.getContent(), users.getTotalElements());
    }

    public User getOneById(User currentUser, String id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new UserDoesNotExistsException();
        }

        authorizeService.canViewUserProfile(currentUser, user.get());

        return user.get();
    }

    public User internalFetOneById(String id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new UserDoesNotExistsException();
        }

        return user.get();
    }

    @Transactional
    public User createOne(
        User currentUser,
        String username,
        String email,
        String password
    ) {
        authorizeService.canCreateUser(currentUser);

        checkParameter(!username.isEmpty(), ValidationException.class);
        checkParameter(!email.isEmpty(), EmailValidator.getInstance().isValid(email), ValidationException.class);
        checkParameter(password.length() >= 8, ValidationException.class);

        Optional<User> userByUsername = userRepository.findByUsername(username);
        Optional<User> userByEmail = userRepository.findByEmail(email);

        if(userByUsername.isPresent() || userByEmail.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User newUser = User.builder()
            .username(username)
            .email(email)
            .isAdmin(false)
            .password(encodePassword(password))
            .build();

        userRepository.save(newUser);

        return newUser;
    }

    @Transactional
    public User editOne(
        User currentUser,
        String id,
        String username,
        String email,
        String password
    ) {
        User user = getOneById(currentUser, id);
        authorizeService.canEditUser(currentUser, user);

        if(!username.isEmpty()) {
            authorizeService.canEditUsername(currentUser, user);
            user.setUsername(username);
        }
        else if(!email.isEmpty()) {
            authorizeService.canEditEmail(currentUser, user);
            checkParameter(EmailValidator.getInstance().isValid(email), ValidationException.class);
            user.setEmail(email);
        }
        else if(!password.isEmpty()) {
            if(currentUser.getId().equals(id)) {
                authorizeService.canEditPassword(currentUser, user);
                checkParameter(password.length() >= 8, ValidationException.class);
                user.setPassword(encodePassword(password));
            }
            else {
                authorizeService.canResetPassword(currentUser, user);
                user.setPassword(null);
            }
        }

        userRepository.save(user);

        return user;
    }

    @Transactional
    public void deleteOne(
        User currentUser,
        String id
    ) {
        User user = getOneById(currentUser, id);
        authorizeService.canDeleteUser(currentUser, user);
        userRepository.delete(user);
    }

    private String encodePassword(String password) {
        checkParameter(password.length() >= 8, ValidationException.class);
        return passwordEncoder.encode(password);
    }
}
