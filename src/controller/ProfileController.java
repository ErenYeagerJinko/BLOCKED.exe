package controller;

import exceptions.*;
import manager.*;
import model.*;
import util.*;
import view.*;
import java.awt.*;

public class ProfileController {
    private Frame frame;
    private ProfileManager profileManager;
    private LeaderboardManager leaderboardManager;

    public ProfileController() {
        this(null, null, null);
    }

    public ProfileController(GameFrame frame, ProfileManager profileManager, LeaderboardManager leaderboardManager) {
        this((Frame) frame, profileManager, leaderboardManager);
    }

    public ProfileController(Frame frame, ProfileManager profileManager, LeaderboardManager leaderboardManager) {
        this.frame = frame;
        this.profileManager = profileManager != null ? profileManager : new ProfileManager();
        this.leaderboardManager = leaderboardManager != null ? leaderboardManager : new LeaderboardManager();
        Logger.getLogger().log("ProfileController initialized");
    }

    public void showUserProfileDialog(String username) {
        Logger.getLogger().log("Opening user profile dialog for username: " + username);
        UserProfile profile = null;
        if (profileManager != null && username != null) {
            profile = profileManager.getProfile(username);
        }
        if (profile == null && username != null) {
            profile = new UserProfile(username, "", "", null, "Leaderboard player profile");
        }
        UserProfileDialog dialog = new UserProfileDialog(frame, profile);
        dialog.setVisible(true);
    }

    public boolean handleUpdateAboutMe(String newAboutMe) {
        if (profileManager == null) {
            Logger.getLogger().error("ProfileManager not available to update about me");
            return false;
        }
        try {
            Logger.getLogger().log("Updating user about me bio");
            profileManager.updateAboutMe(newAboutMe);
            Logger.getLogger().log("User about me bio updated successfully");
            return true;
        } catch (AuthException e) {
            Logger.getLogger().error("Failed to update about me bio: " + e.getMessage());
            return false;
        }
    }

    public boolean handleUpdateProfilePicture(String picturePath) {
        if (profileManager == null) {
            Logger.getLogger().error("ProfileManager not available to update profile picture");
            return false;
        }
        try {
            Logger.getLogger().log("Updating user profile picture with path: " + picturePath);
            profileManager.updateProfilePicture(picturePath);
            Logger.getLogger().log("User profile picture updated successfully");
            return true;
        } catch (AuthException e) {
            Logger.getLogger().error("Failed to update profile picture: " + e.getMessage());
            return false;
        }
    }

    public void navigateToLeaderboard() {
        Logger.getLogger().log("Navigation to leaderboard requested");
    }

    public void navigateToProfile() {
        Logger.getLogger().log("Navigation to profile requested");
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public void setLeaderboardManager(LeaderboardManager leaderboardManager) {
        this.leaderboardManager = leaderboardManager;
    }
}
