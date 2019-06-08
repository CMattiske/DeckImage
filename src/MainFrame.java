import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame extends JFrame {

	public final static int DEFAULT_WIDTH = 1150;
	public final static int DEFAULT_HEIGHT = 900;
	
	private ImagePanel deckImage = new ImagePanel();
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem file_load = new JMenuItem("Load deck");
	private JMenuItem file_save = new JMenuItem("Save image");
	
	private JFileChooser fc = new JFileChooser();
	File defaultDirectory = new File("save/");
	
	private FileNameExtensionFilter filter_decks = new FileNameExtensionFilter("FFDecks export","txt");
	private FileNameExtensionFilter filter_jpeg = new FileNameExtensionFilter("JPEG image","jpeg");
	
	public MainFrame(String title) {
		super(title);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MenuListener ml = new MenuListener();
		
	    fc.setCurrentDirectory(defaultDirectory);
		file_save.addActionListener(ml);
		file_load.addActionListener(ml);
		
		fileMenu.add(file_load);
		fileMenu.add(file_save);
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		
		this.add(deckImage);
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		/*ArrayList<Card> testDeck = new ArrayList<Card>(0);
		for (int i = 0; i<16; i++) {
			testDeck.add(new Card("Auron","1-01"+i%10,3,Card.Type.FORWARD));
		}*/

		deckImage.setDeck(new DeckReader(new File("src/Downloads/FFCC Fire Wind.txt")).getDeck());
	}
	
	private class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			Object source = e.getSource();
			if (source == file_save) {
				fc.setFileFilter(filter_jpeg);
				int returnVal = fc.showSaveDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					saveImage2(file);
				}	
			} else if (source == file_load) {
				fc.setFileFilter(filter_decks);
				int returnVal = fc.showOpenDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					deckImage.setDeck(new DeckReader(file).getDeck());
				}	
			}
		}
	}
	
	private class ImagePanel extends JPanel {
		
		private Color color_bg = Color.BLACK;
		
		private int width = DEFAULT_WIDTH;
		private int height = DEFAULT_HEIGHT;
		
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
			setBackground(color_bg);
			int x = border;
			int y = border;
			for (int i=0; i<deck.size(); i++) {
				Card c = deck.get(i);
				if (x+(c.getQuantity()-1)*copyGap>width-border-card_width) {
					x = border;
					y += card_height + cardGap;
				}
				for (int j=0;j<c.getQuantity();j++) {
					g.drawImage(c.getImage(), x, y, card_width, card_height, null);
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
	        imagebuf = new Robot().createScreenCapture(panel.getBounds());
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
	
	private void saveImage2(File saveAs) {
		Container c = getContentPane();
		BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
		c.paint(im.getGraphics());
		try {
			ImageIO.write(im, "PNG", saveAs);
		} catch (IOException e) {
	        error("Error writing file "+saveAs.getAbsolutePath());
	    }
	}
	
	private void error(String m) {
		System.err.println(m);
	}
}
