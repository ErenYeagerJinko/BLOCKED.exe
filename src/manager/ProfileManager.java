package manager;

import exceptions.*;
import model.*;
import persistence.*;
import java.util.*;

public class ProfileManager {
    private ProfilePersistence persistence;
    private AuthManager authManager;

    public ProfileManager() {
        this(new ProfilePersistence(), new AuthManager());
    }

    public ProfileManager(ProfilePersistence persistence, AuthManager authManager) {
        this.persistence = persistence != null ? persistence : new ProfilePersistence();
        this.authManager = authManager != null ? authManager : new AuthManager(this.persistence);
    }

    public synchronized void recordGameSession(int score, long sessionDurationSeconds, int linesCleared) {
        UserProfile current = authManager != null ? authManager.getCurrentUser() : null;
        if (current == null) {
            return;
        }

        current.addScore(score);
        current.addPlayTime(sessionDurationSeconds);
        current.addLinesCleared(linesCleared);
        current.incrementGamesPlayed();
        current.updateLastLogin();

        try {
            persistence.saveProfile(current);
        } catch (PersistenceException ignored) {
        }
    }

    public synchronized void updateAboutMe(String newAboutMe) throws AuthException {
        UserProfile current = authManager != null ? authManager.getCurrentUser() : null;
        if (current == null) {
            throw new AuthException("No active user session to update bio");
        }

        current.setAboutMe(newAboutMe != null ? newAboutMe : "");
        try {
            persistence.saveProfile(current);
        } catch (PersistenceException e) {
            throw new AuthException("Failed to persist updated bio: " + e.getMessage(), current.getUsername(), e);
        }
    }

    public synchronized void updateProfilePicture(String newPicturePath) throws AuthException {
        UserProfile current = authManager != null ? authManager.getCurrentUser() : null;
        if (current == null) {
            throw new AuthException("No active user session to update avatar");
        }

        current.setProfilePicturePath(newPicturePath);
        try {
            persistence.saveProfile(current);
        } catch (PersistenceException e) {
            throw new AuthException("Failed to persist updated profile picture: " + e.getMessage(), current.getUsername(), e);
        }
    }

    public synchronized UserProfile getProfile(String username) {
        if (username == null) {
            return null;
        }
        try {
            return persistence.loadProfile(username.trim());
        } catch (PersistenceException e) {
            return null;
        }
    }

    public synchronized List<UserProfile> getAllProfiles() {
        try {
            Map<String, UserProfile> map = persistence.loadAllProfiles();
            return new ArrayList<>(map.values());
        } catch (PersistenceException e) {
            return new ArrayList<>();
        }
    }

    public ProfilePersistence getPersistence() {
        return persistence;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}
