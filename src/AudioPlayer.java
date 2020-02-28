import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

class AudioPlayer {
	
	private AudioInputStream ais;
	private Clip c;
	
	protected void playSound(String fileName) {
        String path = new File("").getAbsolutePath() + fileName;
		File sound = new File(path);

        try {
            ais = AudioSystem.getAudioInputStream(sound);
            c = AudioSystem.getClip();
            c.open(ais); //Clip opens AudioInputStream
            c.start(); //Start playing audio
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	 }
	
	protected void stopSound() {
		c.stop();
	}
	
	protected long getSoundLenght (String fileName) {
		String path = new File("").getAbsolutePath() + fileName;
		File sound = new File(path);
        try {
            ais = AudioSystem.getAudioInputStream(sound);
            c = AudioSystem.getClip();
            c.open(ais); //Clip opens AudioInputStream
            return c.getMicrosecondLength()/1000;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}