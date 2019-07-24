import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
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
	private JMenu specialMenu = new JMenu("Special");
	private JMenuItem special_generate = new JMenuItem("Generate Opus IX deck");
	
	private JFileChooser fc = new JFileChooser();
	File defaultDirectory = new File("save/");
	
	private FileNameExtensionFilter filter_decks = new FileNameExtensionFilter("FFDecks export","txt");
	private FileNameExtensionFilter filter_jpeg = new FileNameExtensionFilter("JPEG image","jpg");
	
	private WWGenerator generator;
	
	public MainFrame(String title) {
		super(title);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MenuListener ml = new MenuListener();
		
	    fc.setCurrentDirectory(defaultDirectory);
		file_save.addActionListener(ml);
		file_load.addActionListener(ml);
		special_generate.addActionListener(ml);
		
		fileMenu.add(file_load);
		fileMenu.add(file_save);
		menuBar.add(fileMenu);
		specialMenu.add(special_generate);
		menuBar.add(specialMenu);
		this.setJMenuBar(menuBar);
		
		this.add(deckImage);
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		/*ArrayList<Card> testDeck = new ArrayList<Card>(0);
		for (int i = 0; i<16; i++) {
			testDeck.add(new Card("Auron","1-01"+i%10,3,Card.Type.FORWARD));
		}*/

		//deckImage.setDeck(new DeckReader(new File("src/Downloads/FFCC Fire Wind.txt")).getDeck());
		generator = new WWGenerator();
		//deckImage.setDeck(generator.getList());
	}
	
	private class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			Object source = e.getSource();
			if (source == file_save) {
				fc.setFileFilter(filter_jpeg);
				int returnVal = fc.showSaveDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String filename = file.getAbsolutePath();
					String[] filesplit = file.getAbsolutePath().split("\\.");
					if (filesplit.length>0) {
						filename = filesplit[0];
					}
					System.out.println(filename);
					File newFile = new File(filename+".jpg");
					saveImage2(newFile);
				}	
			} else if (source == file_load) {
				fc.setFileFilter(filter_decks);
				int returnVal = fc.showOpenDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					deckImage.setDeck(new DeckReader(file).getDeck());
				}	
			} else if (source == special_generate) {
				generator.generateList();
				deckImage.setDeck(generator.getList());
			}
		}
	}
	
	private class ImagePanel extends JPanel {
		
		private Color color_bg = Color.BLACK;
		
		private int panel_width = DEFAULT_WIDTH;
		private int panel_height = DEFAULT_HEIGHT;
		
		private int border = 10;
		
		private int card_width = 120;
		private int card_height = 168;
		private int copyGap = 20;
		private int cardGap = 20;
		
		private ArrayList<CardInDeck> deck = new ArrayList<CardInDeck>(0);
		private ArrayList<CardComponent> components = new ArrayList<CardComponent>(0);
		
		public ImagePanel() {
			super();
			setLayout(null);
		}
		
		public int getWidth() {
			return panel_width;
		}
		
		public int getHeight() {
			return panel_height;
		}
		
		public void setDeck(ArrayList<CardInDeck> cards) {
			deck = cards;
			Collections.sort(deck);
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(color_bg);
			int x = border;
			int y = border;
			String currentID = "";
			int qty = 1;
			for (int i=0; i<deck.size(); i+=qty) {
				CardInDeck c = deck.get(i);
				//Find how many copies
				currentID = c.getId();
				qty = 1;
				for (int j=i+1; j<deck.size(); j++) {
					if (currentID.equals(deck.get(j).getId())) {
						qty++;
					} else { //End loop
						j=deck.size();
					}
				}
				if (x+(qty-1)*copyGap>panel_width-border*2-card_width) {
					x = border;
					y += card_height + cardGap;
				}
				for (int j=0;j<qty;j++) {
					g.drawImage(c.getImage(), x, y, card_width, card_height, null);
					x+=copyGap;	
				}
				x+=card_width;
			}
		}
		
		private class CardComponent extends JComponent {
			
			private int x;
			private int y;
			private int width;
			private int height;
			
			private Image cardImage;
			private int ratio;
			
			public CardComponent(int x, int y, CardInDeck card, int n) {
				this.x = x;
				this.y = y;
				this.cardImage = card.getImage();
				this.ratio = n;
				
				this.width = card_width + copyGap*(n-1);
				this.height = card_height;
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
