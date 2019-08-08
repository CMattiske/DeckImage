import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllCards {
	
	public enum Attribute{
		NAME("Card Name"), JOB("Job"), CATEGORY("Category");
		
		String inText; 
		
		Attribute(String inText) {
			this.inText = inText;
		}
	}
	
	public final static int[] OPUS_SIZE = {216, 148, 154, 148, 166, 130, 138, 148, 124};
	
	private ArrayList<Card> fullList = new ArrayList<Card>(0);
	private ArrayList<ArrayList<Card>> lists = new ArrayList<ArrayList<Card>>(); //4x8 lists, 0-3 is fire, 4-7 is ice etc
	
	private Random rand;
	
	public AllCards(String filename) {
		fullList = (ArrayList<Card>) new SetParser(filename).getCardList().clone();
		Collections.sort(fullList);
		//Populate specific lists
		for (int i=0; i<Card.Element.values().length*Card.Type.values().length; i++) {
			lists.add(new ArrayList<Card>(0));
		}
		for (int i=0; i<fullList.size(); i++) {
			Card c = fullList.get(i);
			getList(c.getElement(), c.getType()).add(c);
		}
		
		rand = new Random();
		
	}
	private ArrayList<Card> getList(Card.Element element, Card.Type type) {
		return lists.get(element.ordinal()*Card.Type.values().length+type.ordinal());
	}
	
	public ArrayList<Card> getFullList() {
		return fullList;
	}
	
	public Card getCardById(String id) {
		String[] idSplit = id.split("-");
		int set = Integer.parseInt(idSplit[0]);
		int num = Integer.parseInt(idSplit[1]);
		int offset = 0;
		for (int i=0; i<set-1; i++) {
			offset+=OPUS_SIZE[i];
		}
		return fullList.get(offset+num-1);
	}
	
	public Card getRandomCard(Card.Element element, Card.Type type) {
		ArrayList<Card> list = getList(element, type);
		int r = rand.nextInt(list.size());
		return list.get(r);
	}
	
	public Card getRandomCardByName(String name, Card.Element... elements) {
		ArrayList<Card> nameList = new ArrayList<Card>(0);
		for (int i=0; i<fullList.size(); i++) {
			if (fullList.get(i).getName().equals(name)) {
				boolean correctElement = false;
				for (Card.Element e : elements) {
					if (fullList.get(i).getElement()==e) {
						correctElement = true;
					}
				}
				if (correctElement) nameList.add(fullList.get(i));
			}
		}
		if (nameList.isEmpty()) {
			return null;
		}
		int r = rand.nextInt(nameList.size());
		return nameList.get(r);
	}
	public Card getRandomCardByJob(String job, Card.Element... elements) {
		ArrayList<Card> jobList = new ArrayList<Card>(0);
		for (int i=0; i<fullList.size(); i++) {
			if (fullList.get(i).getJob().equals(job)) {
				boolean correctElement = false;
				for (Card.Element e : elements) {
					if (fullList.get(i).getElement()==e) {
						correctElement = true;
					}
				}
				if (correctElement) jobList.add(fullList.get(i));
			}
		}
		if (jobList.isEmpty()) {
			return null;
		}
		int r = rand.nextInt(jobList.size());
		return jobList.get(r);
	}
	public Card getRandomCardByCategory(String category, Card.Element... elements) {
		ArrayList<Card> categoryList = new ArrayList<Card>(0);
		for (int i=0; i<fullList.size(); i++) {
			String[] categories = fullList.get(i).getCategories();
			for (String categoryToCheck : categories) {
				if (categoryToCheck.equals(category)) {
					boolean correctElement = false;
					for (Card.Element e : elements) {
						if (fullList.get(i).getElement()==e) {
							correctElement = true;
						}
					}
					if (correctElement) categoryList.add(fullList.get(i));
				}
			}
		}
		if (categoryList.isEmpty()) {
			return null;
		}
		int r = rand.nextInt(categoryList.size());
		return categoryList.get(r);
	}
	
	//Returns a random card which satisfies each mention of the attribute in the given text
	public ArrayList<Card> getRandomCardsByAttribute(String text, Attribute att, Card.Element... elements) {
		ArrayList<Card> list = new ArrayList<Card>(0);
		Card.Element[] elementsPlusLightDark = new Card.Element[elements.length+2];
		for (int i=0; i<elements.length; i++) {
			elementsPlusLightDark[i]=elements[i];
		}
		elementsPlusLightDark[elements.length] = Card.Element.LIGHT;
		elementsPlusLightDark[elements.length+1] = Card.Element.DARK;
		Pattern pattern = Pattern.compile("\\["+att.inText+" \\((.*?)\\)\\]");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			//System.out.print(att.inText+" "+matcher.group(1)+": ");
			Card match;
			switch (att) {
			case NAME:
				match = getRandomCardByName(matcher.group(1), elementsPlusLightDark);
				//If can't find one of correct element, search all elements
				if (match==null) {
					match = getRandomCardByName(matcher.group(1), Card.Element.values());
				}
				break;
			case JOB:
				match = getRandomCardByJob(matcher.group(1), elementsPlusLightDark);
				if (match==null) {
					match = getRandomCardByJob(matcher.group(1), Card.Element.values());
				}
				break;
			case CATEGORY:
				match = getRandomCardByCategory(matcher.group(1), elementsPlusLightDark);
				if (match==null) {
					match = getRandomCardByCategory(matcher.group(1), Card.Element.values());
				}
				break;
			default:
				match = null;
			}
			//System.out.println(match==null?"":match.getName());
			if (match!=null) list.add(match);
		}
		return list;
	}
	
	public ArrayList<Card> getRandomDeck(Card.Element... elements) {
		Deck randomDeck = new Deck();
		randomDeck.generateRandom(elements);
		return randomDeck.getDeckList();
	}
	
	private void printDeck(ArrayList<Card> deck) {
		for (int i=0; i<deck.size(); i++) {
			System.out.println(deck.get(i));
		}
	}
	
	private class Deck {
		
		private final static int DECK_SIZE = 50;
		
		private final static int BACKUPS_MIN = 15;
		private final static int SUMMONS_MIN = 3;
		private final static int MONSTERS_MIN = 0;
		
		private final double[] R_BACKUPS = {0.05, 0.2, 0.5, 0.2, 0.05};
		private final double[] R_SUMMONS = {0.05, 0.05, 0.1, 0.2, 0.2, 0.2, 0.1, 0.05, 0.05};
		private final double[] R_MONSTERS = {0.3, 0.2, 0.1, 0.1, 0.1, 0.1, 0.1};
		
		private ArrayList<Card> deckList = new ArrayList<Card>(0);
		
		private int[] totalOfElement;

		private int totalForwards;
		private int totalBackups;
		private int totalSummons;
		private int totalMonsters;
		
		private int currentForwards = 0;
		private int currentBackups = 0; 
		private int currentSummons = 0;
		private int currentMonsters = 0;
		
		public Deck() {
			totalOfElement = new int[Card.Element.values().length];
			
			totalForwards = 0;
			totalBackups = 0;
			totalSummons = 0;
			totalMonsters = 0;
		}
		
		public ArrayList<Card> getDeckList() {
			return deckList;
		}
		
		public void generateRandom(Card.Element... elements) {
			
			//15-19 backups
			totalBackups = BACKUPS_MIN;
			double r = rand.nextDouble();
			double inc = 0;
			int i = 0;
			while (inc<r) {
				inc+=R_BACKUPS[i];
				if (inc>=r) {
					totalBackups = BACKUPS_MIN+i;
				} else {
					i++;
				}
			}
			
			//3-11 Summons
			totalSummons = SUMMONS_MIN;
			r = rand.nextDouble();
			inc = 0;
			i = 0;
			//Weighted range
			while (inc<r) {
				inc+=R_SUMMONS[i];
				if (inc>=r) {
					totalSummons = SUMMONS_MIN+i;
				} else {
					i++;
				}
			}
			
			//0-6 monsters
			totalMonsters = MONSTERS_MIN;
			r = rand.nextDouble();
			inc = 0;
			i = 0;
			//Weighted range
			while (inc<r) {
				inc+=R_MONSTERS[i];
				if (inc>=r) {
					totalMonsters = MONSTERS_MIN+i;
				} else {
					i++;
				}

			}

			totalForwards = DECK_SIZE-totalBackups-totalSummons-totalMonsters;
			
			//Add random card (& associates) from each card type until deck is full
			while (currentForwards<totalForwards ||
					currentBackups<totalBackups ||
					currentSummons<totalSummons ||
					currentMonsters<totalMonsters) {
				if (currentForwards<totalForwards) {
					Card c = getRandomCard(currentLeast(elements), Card.Type.FORWARD);
					addCardWithAssociates(c, elements);
				}
				if (currentBackups<totalBackups) {			
					Card c = getRandomCard(currentLeast(elements), Card.Type.BACKUP);
					addCardWithAssociates(c, elements);
				}
				if (currentSummons<totalSummons) {
					Card c = getRandomCard(currentLeast(elements), Card.Type.SUMMON);
					addCardWithAssociates(c, elements);
				}
				if (currentMonsters<totalMonsters) {
					Card c = getRandomCard(currentLeast(elements), Card.Type.MONSTER);
					addCardWithAssociates(c, elements);
				}
			}
		}
		//Returns the index of the minimum value in the array
		private int indexOfMin(int[] arr) {
			int index = 0;
			int min = Integer.MIN_VALUE;
			for (int i=0; i<arr.length; i++) {
				if (arr[i]<min) {
					index = i;
				}
			}
			return index;
		}
		private Card.Element currentLeast(Card.Element... elements) {
			Card.Element least = elements[0];
			int min = DECK_SIZE;
			for (int i=0; i<elements.length; i++) {
				if (totalOfElement[elements[i].ordinal()]<min) {
					min = totalOfElement[elements[i].ordinal()];
					least = elements[i];
				}
			}
			return least;
		}
		//Finds a random card of the correct element for each attribute mentioned in the card's text to add with it
		//eg. Ba'gamnan mentions job Headhunter so add a random Headhunter with him
		private void addCardWithAssociates(Card c, Card.Element... elements) {
			ArrayList<Card> fullList = new ArrayList<Card>(1);
			fullList.add(c);
			//System.out.println("Checking: "+c.getName()+" ("+c.getSetID()+")");
			//Check  how many cards it refers to
			String text = c.getText();
			//https://stackoverflow.com/questions/4662215/how-to-extract-a-substring-using-regex/4662265#4662265
			fullList.addAll(getRandomCardsByAttribute(text, Attribute.NAME, elements));
			fullList.addAll(getRandomCardsByAttribute(text, Attribute.JOB, elements));
			fullList.addAll(getRandomCardsByAttribute(text, Attribute.CATEGORY, elements));
			//Also add additional copy for S? TODO
			int forwardsToBeAdded = 0;
			int backupsToBeAdded = 0;
			int summonsToBeAdded = 0;
			int monstersToBeAdded = 0;
			int[] elementsToBeAdded = new int[Card.Element.values().length];
			for (int i=0; i<fullList.size(); i++) {
				switch (fullList.get(i).getType()) {
				case FORWARD:
					forwardsToBeAdded++;
					break;
				case BACKUP:
					backupsToBeAdded++;
					break;
				case SUMMON:
					summonsToBeAdded++;
					break;
				case MONSTER:
					monstersToBeAdded++;
					break;
				}
				elementsToBeAdded[fullList.get(i).getElement().ordinal()]++;
			}
			if (currentForwards+forwardsToBeAdded<=totalForwards &&
					currentBackups+backupsToBeAdded<=totalBackups &&
					currentSummons+summonsToBeAdded<=totalSummons &&
					currentMonsters+monstersToBeAdded<=totalMonsters) {
				
				deckList.addAll(fullList);
				
				currentForwards+=forwardsToBeAdded;
				currentBackups+=backupsToBeAdded;
				currentSummons+=summonsToBeAdded;
				currentMonsters+=monstersToBeAdded;
				
				for (int i=0; i<totalOfElement.length; i++) {
					totalOfElement[i]+=elementsToBeAdded[i];
				}
				
				/*System.out.println("Forwards: "+currentForwards+"/"+totalForwards);
				System.out.println("Backups: "+currentBackups+"/"+totalBackups);
				System.out.println("Summons: "+currentSummons+"/"+totalSummons);
				System.out.println("Monsters: "+currentMonsters+"/"+totalMonsters);
				for (int i=0; i<totalOfElement.length; i++) {
					System.out.print(totalOfElement[i]+", ");
				}
				System.out.println("");*/
			}
		}
		
	}
}
