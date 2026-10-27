package model;

import util.*;
import java.time.*;
import java.util.*;

public class UserProfile {
    private String username;
    private String passwordHash;
    private String salt;
    private int highestScore;
    private long totalCumulativeScore;
    private long totalPlayTimeSeconds;
    private String profilePicturePath;
    private String aboutMe;
    private int gamesPlayed;
    private int linesCleared;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public UserProfile(String username, String passwordHash, String salt) {
        this(username, passwordHash, salt, null, "");
    }

    public UserProfile(String username, String passwordHash, String salt, String profilePicturePath, String aboutMe) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.profilePicturePath = profilePicturePath;
        this.aboutMe = aboutMe != null ? aboutMe : "";
        this.highestScore = 0;
        this.totalCumulativeScore = 0L;
        this.totalPlayTimeSeconds = 0L;
        this.gamesPlayed = 0;
        this.linesCleared = 0;
        this.createdAt = LocalDateTime.now();
        this.lastLoginAt = LocalDateTime.now();
        Logger.getLogger().log("UserProfile created for user: " + username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void updateHighestScore(int score) {
        if (score > this.highestScore) {
            this.highestScore = score;
            Logger.getLogger().log("Updated new highest score for " + username + ": " + score);
        }
    }

    public long getTotalCumulativeScore() {
        return totalCumulativeScore;
    }

    public void setTotalCumulativeScore(long totalCumulativeScore) {
        this.totalCumulativeScore = totalCumulativeScore;
    }

    public void addScore(int score) {
        if (score > 0) {
            this.totalCumulativeScore += score;
            Logger.getLogger().log("Added score " + score + " to " + username + ", cumulative score: " + this.totalCumulativeScore);
            updateHighestScore(score);
        }
    }

    public long getTotalPlayTimeSeconds() {
        return totalPlayTimeSeconds;
    }

    public void setTotalPlayTimeSeconds(long totalPlayTimeSeconds) {
        this.totalPlayTimeSeconds = totalPlayTimeSeconds;
    }

    public void addPlayTime(long seconds) {
        if (seconds > 0) {
            this.totalPlayTimeSeconds += seconds;
            Logger.getLogger().log("Added play time " + seconds + "s to " + username + " (total: " + this.totalPlayTimeSeconds + "s)");
        }
    }

    public int getHoursPlayed() {
        return (int) (totalPlayTimeSeconds / 3600);
    }

    public int getMinutesPlayed() {
        return (int) ((totalPlayTimeSeconds % 3600) / 60);
    }

    public String getFormattedPlayTime() {
        int hours = getHoursPlayed();
        int minutes = getMinutesPlayed();
        return hours + "h " + minutes + "m";
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe != null ? aboutMe : "";
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
        Logger.getLogger().log("Incremented games played for " + username + " to " + this.gamesPlayed);
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public void setLinesCleared(int linesCleared) {
        this.linesCleared = linesCleared;
    }

    public void addLinesCleared(int count) {
        if (count > 0) {
            this.linesCleared += count;
            Logger.getLogger().log("Added " + count + " lines cleared to " + username + " (total: " + this.linesCleared + ")");
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
        Logger.getLogger().log("Updated last login time for " + username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "username='" + username + '\'' +
                ", highestScore=" + highestScore +
                ", totalCumulativeScore=" + totalCumulativeScore +
                ", playTime=" + getFormattedPlayTime() +
                ", gamesPlayed=" + gamesPlayed +
                ", linesCleared=" + linesCleared +
                '}';
    }
}
