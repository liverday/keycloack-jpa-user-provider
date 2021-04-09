package valemobi.auth;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import valemobi.auth.dao.IUserDAO;
import valemobi.auth.model.User;
import valemobi.auth.representations.KeycloakUserRepresentation;

import java.util.*;

public class CustomUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        UserRegistrationProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final IUserDAO userDAO;

    Logger logger = LoggerFactory.getLogger(CustomUserStorageProvider.class);

    public CustomUserStorageProvider(KeycloakSession session, ComponentModel model, IUserDAO userDAO) {
        this.session = session;
        this.model = model;
        this.userDAO = userDAO;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        logger.info("supportCredentialsType({})", credentialType);
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        logger.info("isConfiguredFor({}, {}, {})", realm, user, credentialType);
        return supportsCredentialType(credentialType) && getPassword(user) != null;
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        logger.info("isValid({}, {}, {})", realm, user, credentialInput);
        boolean isValidCredentialModel = credentialInput instanceof UserCredentialModel;
        if (!isValidCredentialModel) return false;

        if (supportsCredentialType(credentialInput.getType())) {
            String password = getPassword(user);
            return password != null && password.equals(credentialInput.getChallengeResponse());
        }

        return false;
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel userModel, CredentialInput input) {
        logger.info("updateCredential({}, {}, {})", realm, userModel, input);
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel))
            return false;

        User user = new User().setUserName(userModel.getUsername());
        user.setPassword(input.getChallengeResponse());
        userDAO.update(user);
        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel userModel, String credentialType) {
        logger.info("disableCredentialType({}, {}, {})", realm, userModel, credentialType);
        if (!supportsCredentialType(credentialType))
            return;

        getUserRepresentation(userModel).setPassword(null);
;    }

    private KeycloakUserRepresentation getUserRepresentation(UserModel userModel) {
        KeycloakUserRepresentation userRepresentation = null;
        if (userModel instanceof CachedUserModel) {
            userRepresentation = (KeycloakUserRepresentation) ((CachedUserModel) userModel).getDelegateForUpdate();
        } else {
            userRepresentation = (KeycloakUserRepresentation) userModel;
        }
        return userRepresentation;
    }

    public KeycloakUserRepresentation getUserRepresentation(User user, RealmModel realm) {
        return new KeycloakUserRepresentation(session, realm, model, user, userDAO);
    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel userModel) {
        if (getUserRepresentation(userModel).getPassword() != null) {
            Set<String> set = new HashSet<>();
            set.add(PasswordCredentialModel.TYPE);
            return set;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void close() {
        userDAO.close();
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        return null;
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        return null;
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return null;
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        logger.info("getUsersCount({})", realm);
        return 0;
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        return null;
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        return null;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        return new ArrayList<>();
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        return new ArrayList<>();
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        return new ArrayList<>();
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        return null;
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        return false;
    }

    private String getPassword(UserModel user) {
        String password = null;
        if (user instanceof KeycloakUserRepresentation) {
            password = ((KeycloakUserRepresentation) user).getPassword();
        }

        return password;
    }
}
