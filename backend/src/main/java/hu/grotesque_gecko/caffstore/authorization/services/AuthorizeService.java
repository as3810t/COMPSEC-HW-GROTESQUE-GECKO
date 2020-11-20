package hu.grotesque_gecko.caffstore.authorization.services;

import hu.grotesque_gecko.caffstore.caff.models.CAFF;
import hu.grotesque_gecko.caffstore.user.models.User;
import org.springframework.stereotype.Service;

import static hu.grotesque_gecko.caffstore.utils.Preconditions.checkPermission;

@Service
public class AuthorizeService {
    public void canListAllUsers(User currentUser) {
        checkPermission(currentUser.isAdmin());
    }

    public void canViewUserProfile(User currentUser, User userToView) {
        checkPermission(currentUser.isAdmin(), currentUser.getId().equals(userToView.getId()));
    }

    public void canCreateUser(User currentUser) {
        checkPermission(currentUser.isAdmin());
    }

    public void canEditUser(User currentUser, User userToEdit) {
        checkPermission(currentUser.isAdmin(), currentUser.getId().equals(userToEdit.getId()));
    }

    public void canEditUsername(User currentUser, User userToEdit) {
        checkPermission(currentUser.isAdmin(), currentUser.getId().equals(userToEdit.getId()));
    }

    public void canEditEmail(User currentUser, User userToEdit) {
        checkPermission(currentUser.getId().equals(userToEdit.getId()));
    }

    public void canEditPassword(User currentUser, User userToEdit) {
        checkPermission(currentUser.getId().equals(userToEdit.getId()));
    }

    public void canResetPassword(User currentUser, User userToEdit) {
        checkPermission(currentUser.isAdmin(), currentUser.getId().equals(userToEdit.getId()));
    }

    public void canDeleteUser(User currentUser, User userToDelete) {
        checkPermission(currentUser.isAdmin(), currentUser.getId().equals(userToDelete.getId()));
    }

    public void canGetAllCAFF(User currentUser) {
    }

    public void canCreateCAFF(User currentUser) {
    }

    public void canViewCAFF(User currentUser, CAFF caff) {
    }

    public void canEditCAFF(User currentUser, CAFF caff) {
        checkPermission(currentUser.isAdmin(), currentUser.getId().equals(caff.getOwner().getId()));
    }

    public void canDeleteCAFF(User currentUser, CAFF caff) {
        checkPermission(currentUser.isAdmin(), currentUser.getId().equals(caff.getOwner().getId()));
    }
}
