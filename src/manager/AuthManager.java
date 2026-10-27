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
            Logger.getLogger().error("Registration failed: Username cannot be empty");
            throw new AuthException("Username cannot be empty");
        }
        if (rawPassword == null || rawPassword.isEmpty()) {
            Logger.getLogger().error("Registration failed: Password cannot be empty");
            throw new AuthException("Password cannot be empty");
        }

        String trimmedUser = username.trim();
        Logger.getLogger().log("Attempting registration for user: " + trimmedUser);
        if (userExists(trimmedUser)) {
            Logger.getLogger().error("Registration failed: Username already exists: " + trimmedUser);
            throw new AuthException("Username already exists", trimmedUser);
        }

        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hashPassword(rawPassword, salt);

        UserProfile profile = new UserProfile(trimmedUser, hash, salt, profilePicturePath, aboutMe);
        try {
            persistence.saveProfile(profile);
            this.currentUser = profile;
            Logger.getLogger().log("User registered successfully: " + trimmedUser);
            return profile;
        } catch (PersistenceException e) {
            Logger.getLogger().error("Registration failed to persist profile for " + trimmedUser + ": " + e.getMessage());
            throw new AuthException("Failed to save profile: " + e.getMessage(), trimmedUser, e);
        }
    }

    public synchronized UserProfile login(String username, String rawPassword) throws AuthException {
        if (username == null || rawPassword == null) {
            Logger.getLogger().error("Login failed: Invalid credentials provided");
            throw new AuthException("Invalid credentials");
        }

        String trimmedUser = username.trim();
        Logger.getLogger().log("Attempting login for user: " + trimmedUser);
        UserProfile profile;
        try {
            profile = persistence.loadProfile(trimmedUser);
        } catch (PersistenceException e) {
            Logger.getLogger().error("Login error accessing storage for " + trimmedUser + ": " + e.getMessage());
            throw new AuthException("Error accessing profile storage", trimmedUser, e);
        }

        if (profile == null) {
            Logger.getLogger().error("Login failed: User not found: " + trimmedUser);
            throw new AuthException("User not found", trimmedUser);
        }

        boolean valid = PasswordHasher.verifyPassword(rawPassword, profile.getSalt(), profile.getPasswordHash());
        if (!valid) {
            Logger.getLogger().error("Login failed: Incorrect password for: " + trimmedUser);
            throw new AuthException("Incorrect password", trimmedUser);
        }

        profile.updateLastLogin();
        try {
            persistence.saveProfile(profile);
        } catch (PersistenceException ignored) {
        }

        this.currentUser = profile;
        Logger.getLogger().log("User logged in successfully: " + trimmedUser);
        return profile;
    }

    public synchronized void logout() {
        if (currentUser != null) {
            Logger.getLogger().log("User logged out: " + currentUser.getUsername());
        }
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
