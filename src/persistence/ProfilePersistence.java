package persistence;

import exceptions.*;
import model.*;
import util.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class ProfilePersistence {
    private static final String PROFILES_FILE = "data/profiles.txt";
    private static final String DELIMITER = ";;";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public ProfilePersistence() {
    }

    public synchronized Map<String, UserProfile> loadAllProfiles() throws PersistenceException {
        Map<String, UserProfile> profiles = new LinkedHashMap<>();
        File file = new File(PROFILES_FILE);
        if (!file.exists()) {
            Logger.getLogger().log("Profiles file not found, returning empty profile map");
            return profiles;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                UserProfile profile = deserializeProfile(line);
                if (profile != null) {
                    profiles.put(profile.getUsername().toLowerCase(), profile);
                }
            }
            Logger.getLogger().log("Loaded " + profiles.size() + " user profiles from storage");
        } catch (IOException e) {
            Logger.getLogger().error("Failed to load user profiles: " + e.getMessage());
            throw new PersistenceException("Failed to load user profiles", e);
        }

        return profiles;
    }

    public synchronized void saveAllProfiles(Collection<UserProfile> profiles) throws PersistenceException {
        File file = new File(PROFILES_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            if (profiles != null) {
                for (UserProfile profile : profiles) {
                    if (profile != null) {
                        writer.write(serializeProfile(profile));
                        writer.newLine();
                    }
                }
            }
            Logger.getLogger().log("Saved " + (profiles != null ? profiles.size() : 0) + " user profiles to storage");
        } catch (IOException e) {
            Logger.getLogger().error("Failed to save user profiles: " + e.getMessage());
            throw new PersistenceException("Failed to save user profiles", e);
        }
    }

    public synchronized void saveProfile(UserProfile profile) throws PersistenceException {
        if (profile == null || profile.getUsername() == null) {
            return;
        }
        Map<String, UserProfile> profiles = loadAllProfiles();
        profiles.put(profile.getUsername().toLowerCase(), profile);
        saveAllProfiles(profiles.values());
        Logger.getLogger().log("Saved profile for user: " + profile.getUsername());
    }

    public synchronized UserProfile loadProfile(String username) throws PersistenceException {
        if (username == null) {
            return null;
        }
        Map<String, UserProfile> profiles = loadAllProfiles();
        UserProfile profile = profiles.get(username.toLowerCase());
        Logger.getLogger().log("Loaded profile for user: " + username + " (found=" + (profile != null) + ")");
        return profile;
    }

    private String serializeProfile(UserProfile p) {
        String safeBio = p.getAboutMe() != null ? p.getAboutMe().replace("\n", "\\n").replace("\r", "") : "";
        String pfp = p.getProfilePicturePath() != null ? p.getProfilePicturePath() : "";
        String created = p.getCreatedAt() != null ? p.getCreatedAt().format(FORMATTER) : LocalDateTime.now().format(FORMATTER);
        String lastLogin = p.getLastLoginAt() != null ? p.getLastLoginAt().format(FORMATTER) : LocalDateTime.now().format(FORMATTER);

        return String.join(DELIMITER,
                p.getUsername(),
                p.getPasswordHash(),
                p.getSalt(),
                String.valueOf(p.getHighestScore()),
                String.valueOf(p.getTotalCumulativeScore()),
                String.valueOf(p.getTotalPlayTimeSeconds()),
                pfp,
                safeBio,
                String.valueOf(p.getGamesPlayed()),
                String.valueOf(p.getLinesCleared()),
                created,
                lastLogin
        );
    }

    private UserProfile deserializeProfile(String line) {
        String[] parts = line.split(DELIMITER, -1);
        if (parts.length < 12) {
            return null;
        }

        try {
            String username = parts[0];
            String passwordHash = parts[1];
            String salt = parts[2];
            int highestScore = Integer.parseInt(parts[3]);
            long totalCumulativeScore = Long.parseLong(parts[4]);
            long totalPlayTimeSeconds = Long.parseLong(parts[5]);
            String pfp = parts[6].isEmpty() ? null : parts[6];
            String aboutMe = parts[7].replace("\\n", "\n");
            int gamesPlayed = Integer.parseInt(parts[8]);
            int linesCleared = Integer.parseInt(parts[9]);
            LocalDateTime createdAt = parts[10].isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(parts[10], FORMATTER);
            LocalDateTime lastLoginAt = parts[11].isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(parts[11], FORMATTER);

            UserProfile profile = new UserProfile(username, passwordHash, salt, pfp, aboutMe);
            profile.setHighestScore(highestScore);
            profile.setTotalCumulativeScore(totalCumulativeScore);
            profile.setTotalPlayTimeSeconds(totalPlayTimeSeconds);
            profile.setGamesPlayed(gamesPlayed);
            profile.setLinesCleared(linesCleared);
            profile.setCreatedAt(createdAt);
            profile.setLastLoginAt(lastLoginAt);

            return profile;
        } catch (Exception e) {
            return null;
        }
    }
}
