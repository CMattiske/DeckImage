import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainFrame extends JFrame {

	public final static int DEFAULT_WIDTH = 1150;
	public final static int DEFAULT_HEIGHT = 900;
	
	private ImagePanel deckImage = new ImagePanel();
	
	public MainFrame(String title) {
		super(title);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(deckImage);
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		ArrayList<Card> testDeck = new ArrayList<Card>(0);
		for (int i = 0; i<16; i++) {
			testDeck.add(new Card("Auron","1-01"+i%10,3,Card.Type.FORWARD));
		}

		deckImage.setDeck(testDeck);
	}
	
	private class ImagePanel extends JPanel {
		
		private int width = 1150;
		private int height = 725;
		
		private int border = 10;
		
		private int card_width = 120;
		private int card_height = 168;
		private int copyGap = 20;
		private int cardGap = 20;
		
		private ArrayList<Card> deck = new ArrayList<Card>(0);
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
		
		public void setDeck(ArrayList<Card> cards) {
			deck = cards;
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int x = border;
			int y = border;
			for (int i=0; i<deck.size(); i++) {
				Card c = deck.get(i);
				if (x+(c.getQuantity()-1)*copyGap>width-border-card_width) {
					x = border;
					y += card_height + cardGap;
				}
				for (int j=0;j<c.getQuantity();j++) {
					g.drawImage(c.getImage(), x, y, null);
					x+=copyGap;	
				}
				x+=card_width;
			}
		}

	}
	
	//Credit: Top voted answer at https://stackoverflow.com/questions/19621105/save-image-from-jpanel-after-draw
	//Can't use name as it contains characters that can't be saved
	private void saveImage(JPanel panel, File saveAs){
	    BufferedImage imagebuf=null;
	    try {
	        imagebuf = new Robot().createScreenCapture(panel.bounds());
	    } catch (AWTException e1) {
	        error("AWT error capturing screen");
	    }  
	    Graphics2D graphics2D = imagebuf.createGraphics();
	    panel.paint(graphics2D);
	    try {
	        ImageIO.write(imagebuf,"jpeg", saveAs);
	    } catch (IOException e) {
	        error("Error writing file "+saveAs.getAbsolutePath());
	    }
	}
	
	private void error(String m) {
		System.err.println(m);
	}
}
