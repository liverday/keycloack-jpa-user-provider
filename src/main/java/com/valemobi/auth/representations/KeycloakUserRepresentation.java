package com.valemobi.auth.representations;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;
import com.valemobi.auth.dao.IUserDAO;
import com.valemobi.auth.model.User;

import java.util.List;

public class KeycloakUserRepresentation extends AbstractUserAdapterFederatedStorage {
    private User user;
    private final IUserDAO userDAO;

    public KeycloakUserRepresentation(
            KeycloakSession session,
            RealmModel realm,
            ComponentModel storageProviderModel,
            User user,
            IUserDAO userDAO
    ) {
        super(session, realm, storageProviderModel);

        this.user = user;
        this.userDAO = userDAO;
    }

    @Override
    public String getId() {
        return StorageId.keycloakId(storageProviderModel, user.getId().toString());
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public void setUsername(String username) {
        user.setUserName(username);
        user = userDAO.update(user);
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
        user = userDAO.update(user);
    }

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        user.setLastName(lastName);
        user = userDAO.update(user);
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public void setEmail(String email) {
        user.setEmail(email);
        user.setEmailVerified(false);
        user = userDAO.update(user);
    }

    @Override
    public boolean isEmailVerified() {
        if (user == null)
            return false;

        Boolean emailVerified = user.getEmailVerified();
        return emailVerified != null && emailVerified;
    }

    @Override
    public void setEmailVerified(boolean verified) {
        user.setEmailVerified(verified);
        user = userDAO.update(user);
    }

    @Override
    public void removeAttribute(String name) {
        super.removeAttribute(name);
        user = userDAO.update(user);
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        super.setAttribute(name, values);
        user = userDAO.update(user);
    }

    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
        user = userDAO.update(user);
    }
}
