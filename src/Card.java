import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Card {
	public static String dir_images = "AllCards/";
	public static String default_image = "AllCards/cardback.jpg";
	
	private String name;
	private String id;
	private Image img;
	private int qty;
	
	public Card(String name, String id, int qty) {
		String image_filepath = Card.dir_images+id+".jpg";
		try {
			img = ImageIO.read(new File(image_filepath));
		} catch (IOException e) {
			try {
				error("Couldn't read image file "+image_filepath);
				img = ImageIO.read(new File(default_image));
			} catch (IOException e2) {
				error("Couldn't read default image file "+default_image);
			}
		}
	}
	
	private void error(String m) {
		System.err.println(m);
	}
}
