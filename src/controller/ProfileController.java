package controller;

import exceptions.*;
import manager.*;
import model.*;
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
    }

    public void showUserProfileDialog(String username) {
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
            return false;
        }
        try {
            profileManager.updateAboutMe(newAboutMe);
            return true;
        } catch (AuthException e) {
            return false;
        }
    }

    public boolean handleUpdateProfilePicture(String picturePath) {
        if (profileManager == null) {
            return false;
        }
        try {
            profileManager.updateProfilePicture(picturePath);
            return true;
        } catch (AuthException e) {
            return false;
        }
    }

    public void navigateToLeaderboard() {
    }

    public void navigateToProfile() {
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
