import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Menu extends JFrame {

	private final static String musicPath = "\\res\\sounds\\music.wav";
	private static AtomicBoolean running = new AtomicBoolean();
	
	public Menu()
	{
		startThread();
		setLookAndFeel();
		
		//final Character[] possibilities = {Character.PAWELEK, Character.PASZKOWSKI, Character.JULA};
		Character character = (Character) JOptionPane.showInputDialog(
                null,
                "Choose your character:",
                "Find The Invisible Guy Menu",
                JOptionPane.PLAIN_MESSAGE,
                null,
                Character.values(),
                Character.values()[0]);

		if (character != null) {
			running.set(false);
        	Main main = new Main(character);
        	main.setVisible(true);
        }
		else
			running.set(false);
	}
	
	private void startThread() {
		running.set(true);
		Thread thread = new Thread(() -> {
			AudioPlayer ap = new AudioPlayer();
			long interval = ap.getSoundLenght(musicPath);
			while(running.get()) {
				ap.playSound(musicPath);
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
//	private void startThread() {
//		running.set(true);
//		Thread thread = new Thread(() -> {
//			AudioPlayer ap = new AudioPlayer();
//			long interval = ap.getSoundLenght(musicPath);
//			ap.playSound(musicPath);
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			ap.stopSound();
//		});
//		thread.start();
//	}
	
	private void setLookAndFeel() {		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
	}
	
		public static void main (String [] args) {
			new Menu();   
		}
}