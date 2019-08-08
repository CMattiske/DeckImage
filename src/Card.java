import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;


public class Card implements Comparable<Card> {
	
	public final static String IMAGE_URL = "https://fftcg.cdn.sewest.net/images/cards/full/";
	public final static String IMAGE_SUFFIX = "_eg";
	
	public static String images_localDir = "AllCards/";
	public static String default_image = "AllCards/cardback.jpg";
	
	public final static String IMAGE_TYPE = ".jpg";
	
	public enum Type{
		FORWARD, SUMMON, MONSTER, BACKUP;
	}
	
	public enum Element{
		FIRE, ICE, WIND, EARTH, LIGHTNING, WATER, LIGHT, DARK;
	}
	
	public enum Rarity{
		C, R, H, L, S;
	}
	
	private String name;
	private String setID;
	private String guid;

	private Element element;
	private int cost;
	private Type type;
	private boolean multicard;
	private boolean exburst;
	private String job;
	private String[] categories;
	
	private String text;
	private int power;
	private Rarity rarity;
	
	public Card(String name, String setID, String guid, Element element,
			int cost, Type type, boolean multicard, boolean exburst, String job, String[] categories, String text,
			int power, Rarity rarity) {
		super();
		this.name = name;
		this.setID = setID;
		this.guid = guid;
		this.element = element;
		this.cost = cost;
		this.type = type;
		this.multicard = multicard;
		this.exburst = exburst;
		this.job = job;
		this.categories = categories;
		this.text = text;
		this.power = power;
		this.rarity = rarity;
	}
	
	public Image getImg() {
		Image image = null;
		String url_fullpath = IMAGE_URL+setID+rarity+IMAGE_SUFFIX+IMAGE_TYPE;
		try {
			URL url = new URL(url_fullpath);
			image = ImageIO.read(url);
		} catch (Exception e) {
			error("Couldn't read image from url: "+url_fullpath);
			String image_filepath = CardInDeck.dir_images+setID+IMAGE_TYPE;
			try {
				image = ImageIO.read(new File(image_filepath));
			} catch (IOException e2) {
				try {
					error("Couldn't read image file "+image_filepath);
					image = ImageIO.read(new File(default_image));
				} catch (IOException e3) {
					error("Couldn't read default image file "+default_image);
				}
			}
		}
		return image;
	}

	public String getName() {
		return name;
	}

	public String getSetID() {
		return setID;
	}

	public String getGuid() {
		return guid;
	}

	public Element getElement() {
		return element;
	}

	public int getCost() {
		return cost;
	}

	public Type getType() {
		return type;
	}
	
	public boolean isMulticard() {
		return multicard;
	}
	
	public boolean isExburst() {
		return exburst;
	}

	public String getJob() {
		return job;
	}

	public String[] getCategories() {
		return categories;
	}

	public String getText() {
		return text;
	}

	public int getPower() {
		return power;
	}

	public Rarity getRarity() {
		return rarity;
	}
	
	public int compareTo(Card other) {
		return this.setID.compareTo(other.getSetID());
	}
	
	@Override
	public String toString() {
		return name+" ("+setID+")";
	}
	
	private void error(String message) {
		System.err.println(message);
	}
}
