package manager;

import exceptions.*;
import model.*;
import persistence.*;
import util.*;
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
            Logger.getLogger().log("Recorded game session for " + current.getUsername() + ": score=" + score + ", duration=" + sessionDurationSeconds + "s, lines=" + linesCleared);
        } catch (PersistenceException e) {
            Logger.getLogger().error("Failed to persist game session for " + current.getUsername() + ": " + e.getMessage());
        }
    }

    public synchronized void updateAboutMe(String newAboutMe) throws AuthException {
        UserProfile current = authManager != null ? authManager.getCurrentUser() : null;
        if (current == null) {
            Logger.getLogger().error("Failed to update bio: No active user session");
            throw new AuthException("No active user session to update bio");
        }

        current.setAboutMe(newAboutMe != null ? newAboutMe : "");
        try {
            persistence.saveProfile(current);
            Logger.getLogger().log("Updated bio for user: " + current.getUsername());
        } catch (PersistenceException e) {
            Logger.getLogger().error("Failed to persist updated bio for " + current.getUsername() + ": " + e.getMessage());
            throw new AuthException("Failed to persist updated bio: " + e.getMessage(), current.getUsername(), e);
        }
    }

    public synchronized void updateProfilePicture(String newPicturePath) throws AuthException {
        UserProfile current = authManager != null ? authManager.getCurrentUser() : null;
        if (current == null) {
            Logger.getLogger().error("Failed to update avatar: No active user session");
            throw new AuthException("No active user session to update avatar");
        }

        current.setProfilePicturePath(newPicturePath);
        try {
            persistence.saveProfile(current);
            Logger.getLogger().log("Updated profile picture for user: " + current.getUsername() + " -> " + newPicturePath);
        } catch (PersistenceException e) {
            Logger.getLogger().error("Failed to persist updated profile picture for " + current.getUsername() + ": " + e.getMessage());
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
