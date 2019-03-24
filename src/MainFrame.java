import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainFrame extends JFrame {

	public MainFrame(String title) {
		super(title);
		
		
		
		this.setVisible(true);
	}
	
	private class ImagePanel extends JPanel {
		
		
		@Override
		public void paintComponent(Graphics g) {
			
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
