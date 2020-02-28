import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;


public class Main extends JFrame {

	private static final int width = 1200;
	private static final int height = 650;
	
	private class Surface extends JPanel implements ActionListener {

		private static final long serialVersionUID = 1L;
		private Image image;
	    private int iw;
	    private int ih;
		private int posX; //image position
		private int posY;
		private int mouseX;
		private int mouseY;
		private boolean mouseInFrame;
		private Area[] areas = new Area[6];
		private int ellipsePosX;
		private int ellipsePosY;
		private Timer timer;
		private final int DELAY = 700;
		private final int INITIAL_DELAY = 1000;
		private AudioPlayer audioPlayer = new AudioPlayer();
	    private Color color = Color.GRAY;
	    private float alpha = 0.0f;
	    private boolean working = true;
	    private Character character;
	    private int points = -1;
	    
			private boolean developmentMode = true;
		
		Surface(Character character) {
			this.character = character;
			initUI();
			randPos();
			initTimer();
			
		}
		
		private void randPos() {
			Random random = new Random();
			posX = random.nextInt(width - iw) + iw/2; 
			posY = random.nextInt(height - 2*ih) + ih/2;	
		}
		
		private void initUI() {
			loadImage();
			deserializeData();
			
	        iw = image.getWidth(null);
	        ih = image.getHeight(null);
	        
	        addMouseMotionListener(new MyMouseAdapter());
	        addMouseListener(new MyMouseAdapter());
		}
		
		private void initTimer() {
	        timer = new Timer(DELAY, this);
	        timer.setInitialDelay(INITIAL_DELAY);
	        timer.start();
		}
		
		private void loadImage() {
			image = new ImageIcon(character.getImagePath()).getImage();
		}
		
		private void deserializeData() {
			try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("points.bin"))) {
	            this.points = (int) inputStream.readObject();
			} catch (FileNotFoundException e ) {
				e.printStackTrace();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		private void doDrawing(Graphics g) {
			
			Graphics2D g2d = (Graphics2D) g.create();
			
			setBackground(color);
			
	        ellipsePosX = posX-iw/2;
			ellipsePosY = posY-ih/2;
	        
			BufferedImage buffImg = new BufferedImage(getWidth(), getHeight(),
	                BufferedImage.TYPE_INT_ARGB);
	        Graphics2D gbi = buffImg.createGraphics();
	        
	        AffineTransform tx1 = new AffineTransform();
	        tx1.translate(-iw/2,-ih/2);
	        gbi.setTransform(tx1);
	        
	        gbi.setComposite(AlphaComposite.SrcOver.derive(alpha));
	        gbi.drawImage(image, posX, posY, null);
	        
	        areas[0] = new Area(new Ellipse2D.Double(ellipsePosX, ellipsePosY, iw, 1.2*iw));
	        areas[1] = new Area(new Ellipse2D.Double(ellipsePosX - 1.2*iw , ellipsePosY - 0.9*ih, 3.5*iw, 3.5*iw));
	        areas[2] = new Area(new Ellipse2D.Double(ellipsePosX - 2.5*iw, ellipsePosY - 2*ih, 6*iw, 6*iw));
	        areas[3] = new Area(new Ellipse2D.Double(ellipsePosX - 4*iw, ellipsePosY - 3.2*ih, 9*iw, 9*iw));
	        areas[4] = new Area(new Ellipse2D.Double(ellipsePosX - 7.5*iw, ellipsePosY - 6*ih, 16*iw, 16*iw));
	        areas[5] = new Area(new Rectangle2D.Double(0,0,getWidth(),getHeight()));

	        for(int i=5; i>0; i--) {
	        	areas[i].subtract(areas[i-1]);
	        }
	        
	        if(developmentMode)
	        {
	        	Color[] colours = {Color.green, Color.yellow, Color.red, Color.blue, Color.magenta, Color.orange};
		        g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
		        
		        for(int i=0; i<=5; i++) {
		        	g2d.setColor(colours[i]);
		        	g2d.fill(areas[i]);
		        }
	        }
	        
	        g2d.drawImage(buffImg, 0, 0, getWidth(), getHeight(), null);
	        
	        JLabel label = new JLabel("Punkty: " + points);
	        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
	        label.setFont(new Font("Serif", Font.BOLD, 20));
	        label.setBorder(border);
	        add(label);
	        
	        gbi.dispose();
	        g2d.dispose();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			doDrawing(g);
		}
		
		private class MyMouseAdapter extends MouseAdapter {
			
	        @Override
	        public void mouseExited(MouseEvent e) {
	            mouseInFrame = false;
	        }

	        @Override
	        public void mouseEntered(MouseEvent e) {
	            mouseInFrame = true;
	        }
	        
	        @Override
	        public void mouseMoved(MouseEvent e) {
	            mouseX = e.getX();
	            mouseY = e.getY();
	        }
	        
	        @Override
	        public void mousePressed(MouseEvent e) {
	        	if(working) {
		            if(areas[0].contains(mouseX,mouseY)) {
		            	points++;
		            	timer.stop();
		            	try {
							Thread.sleep(700);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
		            	
		            	alpha = 1.0f;
		            	color = Color.orange;
		            	repaint();
		            	
		            	try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
		            	audioPlayer.playSound(character.getSoundOfIndex(6));
		            	working = false;
		            	
		            	serializeData();
		            	askQuestion();
		            }
	        	}
	        }
		}
		
		private void serializeData() {
			try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("points.bin"))) {
				outputStream.writeObject(points);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		private void askQuestion() {
        	byte n = (byte) JOptionPane.showConfirmDialog(Main.this, "Would you like to play again?", "Quesion", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				Main.this.dispose();
				new Menu();
			}
			else if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
				audioPlayer.playSound("//res//sounds//test.wav");
				Main.this.dispose();
			}
        }

		@Override
		public void actionPerformed(ActionEvent e) {
			if(mouseInFrame) {	
				for(int i=0; i<=5; i++) {
					if(areas[i].contains(mouseX,mouseY)) {
						audioPlayer.playSound(character.getSoundOfIndex(i));
						break;
					}
				}	
			}
		}		
	}
	
	Main(Character character) {
        add(new Surface(character));

        setTitle("Find the invisible guy");
        setSize(width, height);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				Main main = new Main(Character.PASZKOWSKI);
//				main.setVisible(true);
//			}
//        });
		new Menu();
	}
}