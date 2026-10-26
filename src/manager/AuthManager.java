package manager;

import exceptions.*;
import model.*;
import persistence.*;
import util.*;
import java.util.*;

public class AuthManager {
    private ProfilePersistence persistence;
    private UserProfile currentUser;

    public AuthManager() {
        this(new ProfilePersistence());
    }

    public AuthManager(ProfilePersistence persistence) {
        this.persistence = persistence != null ? persistence : new ProfilePersistence();
    }

    public synchronized UserProfile register(String username, String rawPassword, String profilePicturePath, String aboutMe) throws AuthException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthException("Username cannot be empty");
        }
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new AuthException("Password cannot be empty");
        }

        String trimmedUser = username.trim();
        if (userExists(trimmedUser)) {
            throw new AuthException("Username already exists", trimmedUser);
        }

        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hashPassword(rawPassword, salt);

        UserProfile profile = new UserProfile(trimmedUser, hash, salt, profilePicturePath, aboutMe);
        try {
            persistence.saveProfile(profile);
            this.currentUser = profile;
            return profile;
        } catch (PersistenceException e) {
            throw new AuthException("Failed to save profile: " + e.getMessage(), trimmedUser, e);
        }
    }

    public synchronized UserProfile login(String username, String rawPassword) throws AuthException {
        if (username == null || rawPassword == null) {
            throw new AuthException("Invalid credentials");
        }

        String trimmedUser = username.trim();
        UserProfile profile;
        try {
            profile = persistence.loadProfile(trimmedUser);
        } catch (PersistenceException e) {
            throw new AuthException("Error accessing profile storage", trimmedUser, e);
        }

        if (profile == null) {
            throw new AuthException("User not found", trimmedUser);
        }

        boolean valid = PasswordHasher.verifyPassword(rawPassword, profile.getSalt(), profile.getPasswordHash());
        if (!valid) {
            throw new AuthException("Incorrect password", trimmedUser);
        }

        profile.updateLastLogin();
        try {
            persistence.saveProfile(profile);
        } catch (PersistenceException ignored) {
        }

        this.currentUser = profile;
        return profile;
    }

    public synchronized void logout() {
        this.currentUser = null;
    }

    public synchronized boolean isLoggedIn() {
        return currentUser != null;
    }

    public synchronized UserProfile getCurrentUser() {
        return currentUser;
    }

    public synchronized boolean userExists(String username) {
        if (username == null) {
            return false;
        }
        try {
            return persistence.loadProfile(username.trim()) != null;
        } catch (PersistenceException e) {
            return false;
        }
    }

    public ProfilePersistence getPersistence() {
        return persistence;
    }
}
