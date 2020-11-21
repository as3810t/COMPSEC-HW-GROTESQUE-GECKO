package hu.grotesque_gecko.caffstore.user.services;

import hu.grotesque_gecko.caffstore.authorization.services.AuthorizeService;
import hu.grotesque_gecko.caffstore.user.models.TemporaryPassword;
import hu.grotesque_gecko.caffstore.user.repositories.TemporaryPasswordRepository;
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
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static hu.grotesque_gecko.caffstore.utils.Preconditions.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TemporaryPasswordRepository temporaryPasswordRepository;

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

    public User internalFindOneById(String id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new UserDoesNotExistsException();
        }

        return user.get();
    }

    public Optional<User> internalFindOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> internalFindOneByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createOne(
        User currentUser,
        String username,
        String email,
        String password
    ) {
        authorizeService.canCreateUser(currentUser);
        return internalCreateOne(username, email, password);
    }

    public User internalCreateOne(
        String username,
        String email,
        String password
    ) {
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
            .credentialValidityDate(new Date())
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
                internalCreateTemporaryPassword(user);
            }
            user.setCredentialValidityDate(new Date());
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

    @Transactional
    public void internalCreateTemporaryPassword(User user) {
        Calendar validUntil = Calendar.getInstance();
        validUntil.setTime(new Date());
        validUntil.add(Calendar.HOUR_OF_DAY, 1);

        String rawPassword = KeyGenerators.string().generateKey();
        System.out.println(rawPassword);

        TemporaryPassword password = TemporaryPassword.builder()
            .user(user)
            .password(encodePassword(rawPassword))
            .validUntil(validUntil.getTime())
            .build();

        temporaryPasswordRepository.save(password);
    }

    @Transactional
    public void internalResetCredentialValidity(User user) {
        user.setCredentialValidityDate(new Date());
        userRepository.save(user);
    }

    private String encodePassword(String password) {
        checkParameter(password.length() >= 8, ValidationException.class);
        return passwordEncoder.encode(password);
    }
}
