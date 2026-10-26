package controller;

import manager.LeaderboardManager;
import manager.ProfileManager;
import model.UserProfile;
import view.GameFrame;
import view.UserProfileDialog;

import java.awt.Frame;

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
        this.profileManager = profileManager;
        this.leaderboardManager = leaderboardManager;
    }

    public void showUserProfileDialog(String username) {
        UserProfile profile = null;
        if (profile == null && username != null) {
            profile = new UserProfile(username, "", "", null, "Leaderboard player profile");
        }
        UserProfileDialog dialog = new UserProfileDialog(frame, profile);
        dialog.setVisible(true);
    }

    public void handleUpdateAboutMe(String newAboutMe) {
    }

    public void handleUpdateProfilePicture(String picturePath) {
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
