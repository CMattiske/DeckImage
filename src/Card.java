
public class Card {
	
	public enum Type{
		FORWARD, BACKUP, SUMMON, MONSTER;
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
	private String category;
	
	private String text;
	private int power;
	private Rarity rarity;
	
	public Card(String name, String setID, String guid, Element element,
			int cost, Type type, boolean multicard, boolean exburst, String job, String category, String text,
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
		this.category = category;
		this.text = text;
		this.power = power;
		this.rarity = rarity;
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

	public String getCategory() {
		return category;
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
	
	
}
