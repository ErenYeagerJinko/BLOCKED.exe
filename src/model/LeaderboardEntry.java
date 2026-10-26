package model;

import java.time.*;
import java.util.*;

public class LeaderboardEntry {
    private int rank;
    private String username;
    private int highestScore;
    private long totalScore;
    private String formattedPlayTime;
    private LocalDateTime achievedAt;

    public LeaderboardEntry(int rank, String username, int highestScore, long totalScore, String formattedPlayTime) {
        this(rank, username, highestScore, totalScore, formattedPlayTime, LocalDateTime.now());
    }

    public LeaderboardEntry(int rank, String username, int highestScore, long totalScore, String formattedPlayTime, LocalDateTime achievedAt) {
        this.rank = rank;
        this.username = username;
        this.highestScore = highestScore;
        this.totalScore = totalScore;
        this.formattedPlayTime = formattedPlayTime;
        this.achievedAt = achievedAt != null ? achievedAt : LocalDateTime.now();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public String getFormattedPlayTime() {
        return formattedPlayTime;
    }

    public void setFormattedPlayTime(String formattedPlayTime) {
        this.formattedPlayTime = formattedPlayTime;
    }

    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(LocalDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardEntry that = (LeaderboardEntry) o;
        return rank == that.rank &&
                highestScore == that.highestScore &&
                totalScore == that.totalScore &&
                Objects.equals(username, that.username) &&
                Objects.equals(formattedPlayTime, that.formattedPlayTime) &&
                Objects.equals(achievedAt, that.achievedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, username, highestScore, totalScore, formattedPlayTime, achievedAt);
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "rank=" + rank +
                ", username='" + username + '\'' +
                ", highestScore=" + highestScore +
                ", totalScore=" + totalScore +
                ", formattedPlayTime='" + formattedPlayTime + '\'' +
                ", achievedAt=" + achievedAt +
                '}';
    }
}
