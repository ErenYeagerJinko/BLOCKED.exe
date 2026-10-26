package model;

import java.util.*;

public class Leaderboard {
    public static final int DEFAULT_MAX_ENTRIES = 50;

    private List<LeaderboardEntry> entries;
    private int maxEntries;

    public Leaderboard() {
        this(DEFAULT_MAX_ENTRIES);
    }

    public Leaderboard(int maxEntries) {
        this.entries = new ArrayList<>();
        this.maxEntries = maxEntries > 0 ? maxEntries : DEFAULT_MAX_ENTRIES;
    }

    public List<LeaderboardEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void setEntries(List<LeaderboardEntry> newEntries) {
        this.entries = new ArrayList<>();
        if (newEntries != null) {
            for (LeaderboardEntry entry : newEntries) {
                if (entry != null) {
                    this.entries.add(entry);
                }
            }
        }
        trimToMaxEntries();
        updateRanks();
    }

    public void addEntry(LeaderboardEntry entry) {
        if (entry == null) {
            return;
        }
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getUsername().equalsIgnoreCase(entry.getUsername())) {
                entries.set(i, entry);
                return;
            }
        }
        entries.add(entry);
    }

    public void sortByHighestScore() {
        entries.sort(Comparator
                .comparingInt(LeaderboardEntry::getHighestScore)
                .reversed()
                .thenComparing(Comparator.comparingLong(LeaderboardEntry::getTotalScore).reversed()));
        trimToMaxEntries();
        updateRanks();
    }

    public void sortByCumulativeScore() {
        entries.sort(Comparator
                .comparingLong(LeaderboardEntry::getTotalScore)
                .reversed()
                .thenComparing(Comparator.comparingInt(LeaderboardEntry::getHighestScore).reversed()));
        trimToMaxEntries();
        updateRanks();
    }

    private void updateRanks() {
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }
    }

    private void trimToMaxEntries() {
        if (entries.size() > maxEntries) {
            entries = new ArrayList<>(entries.subList(0, maxEntries));
        }
    }

    public LeaderboardEntry getEntryByUsername(String username) {
        if (username == null) {
            return null;
        }
        for (LeaderboardEntry entry : entries) {
            if (username.equalsIgnoreCase(entry.getUsername())) {
                return entry;
            }
        }
        return null;
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public void clear() {
        entries.clear();
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries > 0 ? maxEntries : DEFAULT_MAX_ENTRIES;
        trimToMaxEntries();
        updateRanks();
    }
}
