package manager;

import exceptions.*;
import model.*;
import persistence.*;
import util.*;
import java.util.*;

public class LeaderboardManager {
    private ProfilePersistence persistence;
    private Leaderboard cachedLeaderboard;

    public LeaderboardManager() {
        this(new ProfilePersistence());
    }

    public LeaderboardManager(ProfilePersistence persistence) {
        this.persistence = persistence != null ? persistence : new ProfilePersistence();
        this.cachedLeaderboard = new Leaderboard();
        Logger.getLogger().log("LeaderboardManager initialized");
        refreshLeaderboard();
    }

    public synchronized void refreshLeaderboard() {
        try {
            Map<String, UserProfile> profiles = persistence.loadAllProfiles();
            List<LeaderboardEntry> entries = new ArrayList<>();
            for (UserProfile p : profiles.values()) {
                LeaderboardEntry entry = new LeaderboardEntry(
                        0,
                        p.getUsername(),
                        p.getHighestScore(),
                        p.getTotalCumulativeScore(),
                        p.getFormattedPlayTime(),
                        p.getLastLoginAt()
                );
                entries.add(entry);
            }
            cachedLeaderboard.setEntries(entries);
            cachedLeaderboard.sortByHighestScore();
            Logger.getLogger().log("Leaderboard refreshed successfully with " + entries.size() + " entries");
        } catch (PersistenceException e) {
            Logger.getLogger().error("Failed to refresh leaderboard: " + e.getMessage());
        }
    }

    public synchronized Leaderboard getTopScores(int limit) {
        Logger.getLogger().log("Retrieving top " + limit + " highest scores from leaderboard");
        refreshLeaderboard();
        cachedLeaderboard.sortByHighestScore();
        if (limit > 0 && cachedLeaderboard.size() > limit) {
            Leaderboard trimmed = new Leaderboard(limit);
            List<LeaderboardEntry> sub = cachedLeaderboard.getEntries().subList(0, limit);
            trimmed.setEntries(sub);
            return trimmed;
        }
        return cachedLeaderboard;
    }

    public synchronized Leaderboard getTopCumulativeScores(int limit) {
        Logger.getLogger().log("Retrieving top " + limit + " cumulative scores from leaderboard");
        refreshLeaderboard();
        cachedLeaderboard.sortByCumulativeScore();
        if (limit > 0 && cachedLeaderboard.size() > limit) {
            Leaderboard trimmed = new Leaderboard(limit);
            List<LeaderboardEntry> sub = cachedLeaderboard.getEntries().subList(0, limit);
            trimmed.setEntries(sub);
            return trimmed;
        }
        return cachedLeaderboard;
    }

    public synchronized int getPlayerRank(String username) {
        if (username == null) {
            Logger.getLogger().log("Cannot retrieve rank for null username");
            return -1;
        }
        LeaderboardEntry entry = cachedLeaderboard.getEntryByUsername(username);
        int rank = entry != null ? entry.getRank() : -1;
        Logger.getLogger().log("Retrieved rank for " + username + ": " + rank);
        return rank;
    }

    public Leaderboard getCachedLeaderboard() {
        return cachedLeaderboard;
    }

    public ProfilePersistence getPersistence() {
        return persistence;
    }
}
