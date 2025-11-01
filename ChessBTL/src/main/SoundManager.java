package main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private Clip clip;

    // Phát nhạc nền (loop liên tục)
    public void playLoop(String filePath) {
        stop();
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Khong the phat nhac nen: " + e.getMessage());
        }
    }

    // Phát âm thanh ngắn (vd: move, capture)
    public static void playSound(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip sound = AudioSystem.getClip();
            sound.open(audioStream);
            sound.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Khong the phat am thanh: " + e.getMessage());
        }
    }

    // Dừng nhạc
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
