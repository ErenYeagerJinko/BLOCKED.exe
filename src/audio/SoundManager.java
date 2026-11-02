package audio;

import enums.*;
import exceptions.*;
import util.*;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

/**
 * Manages audio loading, playback, and mute state across the game.
 * Uses Java Sound API (Clip) with cached audio streams for low-latency playback.
 */
public class SoundManager {

    private static final String SOUND_DIR = "resources/Sound/";

    private static SoundManager instance;

    private final Map<SoundEffectType, Clip> clipCache;
    private boolean muted;

    /**
     * Constructs the SoundManager and pre-loads all sound effects.
     */
    public SoundManager() {
        this.clipCache = new HashMap<SoundEffectType, Clip>();
        this.muted = false;
        loadAllSounds();
    }

    /**
     * Singleton accessor for global sound management.
     *
     * @return the singleton SoundManager instance
     */
    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Pre-loads all required sound clips into memory cache.
     */
    private void loadAllSounds() {
        loadClip(SoundEffectType.SELECTION, SOUND_DIR + "select.wav");
        loadClip(SoundEffectType.PLACEMENT, SOUND_DIR + "place.wav");
        loadClip(SoundEffectType.LINE_CLEAR, SOUND_DIR + "clear.wav");
        loadClip(SoundEffectType.INVALID_MOVE, SOUND_DIR + "invalid.wav");
        loadClip(SoundEffectType.GAME_OVER, SOUND_DIR + "invalid.wav");
    }

    /**
     * Loads a single audio file and stores the open Clip in the cache.
     *
     * @param type the sound effect type
     * @param filePath relative path to the WAV file
     */
    private void loadClip(SoundEffectType type, String filePath) {
        File audioFile = new File(filePath);
        if (!audioFile.exists()) {
            Logger.getLogger().error("Audio file not found: " + filePath);
            return;
        }

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clipCache.put(type, clip);
            Logger.getLogger().log("Loaded audio effect: " + type.name() + " from " + filePath);
        } catch (UnsupportedAudioFileException e) {
            Logger.getLogger().error("Unsupported audio format for file: " + filePath + " - " + e.getMessage());
        } catch (IOException e) {
            Logger.getLogger().error("I/O error loading audio file: " + filePath + " - " + e.getMessage());
        } catch (LineUnavailableException e) {
            Logger.getLogger().error("Audio line unavailable for file: " + filePath + " - " + e.getMessage());
        }
    }

    /**
     * Plays the specified sound effect.
     * If the manager is muted, playback is skipped silently.
     *
     * @param type the sound effect to play
     * @throws AudioException if the sound effect is null or playback encounters an error
     */
    public void playSound(SoundEffectType type) throws AudioException {
        if (muted) {
            return;
        }

        if (type == null) {
            throw new AudioException("Cannot play null sound effect type.");
        }

        Clip clip = clipCache.get(type);
        if (clip == null) {
            Logger.getLogger().error("Sound effect clip not loaded: " + type.name());
            throw new AudioException("Sound clip not loaded for type: " + type.name());
        }

        try {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            String errorMsg = "Failed to play sound effect: " + type.name() + " - " + e.getMessage();
            Logger.getLogger().error(errorMsg);
            throw new AudioException(errorMsg, e);
        }
    }

    /**
     * Plays the selection sound effect.
     */
    public void playSelect() {
        try {
            playSound(SoundEffectType.SELECTION);
        } catch (AudioException e) {
            Logger.getLogger().error("Error playing select sound: " + e.getMessage());
        }
    }

    /**
     * Plays the block placement sound effect.
     */
    public void playPlace() {
        try {
            playSound(SoundEffectType.PLACEMENT);
        } catch (AudioException e) {
            Logger.getLogger().error("Error playing place sound: " + e.getMessage());
        }
    }

    /**
     * Plays the line clear sound effect.
     */
    public void playClear() {
        try {
            playSound(SoundEffectType.LINE_CLEAR);
        } catch (AudioException e) {
            Logger.getLogger().error("Error playing clear sound: " + e.getMessage());
        }
    }

    /**
     * Plays the invalid move sound effect.
     */
    public void playInvalid() {
        try {
            playSound(SoundEffectType.INVALID_MOVE);
        } catch (AudioException e) {
            Logger.getLogger().error("Error playing invalid move sound: " + e.getMessage());
        }
    }

    /**
     * Plays the game over sound effect.
     */
    public void playGameOver() {
        try {
            playSound(SoundEffectType.GAME_OVER);
        } catch (AudioException e) {
            Logger.getLogger().error("Error playing game over sound: " + e.getMessage());
        }
    }

    /**
     * Returns true if audio playback is currently muted.
     *
     * @return true if muted, false otherwise
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Sets the muted state of the audio system.
     *
     * @param muted true to mute, false to unmute
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) {
            stopAllClips();
            Logger.getLogger().log("Audio system muted.");
        } else {
            Logger.getLogger().log("Audio system unmuted.");
        }
    }

    /**
     * Toggles between muted and unmuted audio states.
     */
    public void toggleMute() {
        if (muted) {
            setMuted(false);
        } else {
            setMuted(true);
        }
    }

    /**
     * Stops all currently playing audio clips.
     */
    public void stopAllClips() {
        for (Clip clip : clipCache.values()) {
            if (clip != null) {
                if (clip.isRunning()) {
                    clip.stop();
                }
            }
        }
    }

    /**
     * Releases audio line resources held by cached clips.
     */
    public void close() {
        stopAllClips();
        for (Clip clip : clipCache.values()) {
            if (clip != null) {
                clip.close();
            }
        }
        clipCache.clear();
        Logger.getLogger().log("SoundManager audio resources released.");
    }
}
