import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class CardInDeck implements Comparable<CardInDeck> {	
	public enum Type {
		FORWARD, SUMMON, MONSTER, BACKUP;
	}
	
	public static String dir_images = "AllCards/";
	public static String default_image = "AllCards/cardback.jpg";
	
	private String name;
	private String id;
	private Image img;
	private Type type;
	
	public CardInDeck(String name, String id, Type type) {
		this.name = name;
		this.id = id;
		this.type = type;
		String image_filepath = CardInDeck.dir_images+id+".jpg";
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
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public Image getImage() {
		return img;
	}
	
	public Type getType() {
		return type;
	}
	
	private void error(String m) {
		System.err.println(m);
	}

	@Override
	public int compareTo(CardInDeck c) {
		if (this.type!=c.type) {
			return this.type.ordinal()<c.getType().ordinal()?-1:1;
		} else {
			return this.id.compareTo(c.getId());
		}
		
	}
	
	
}
