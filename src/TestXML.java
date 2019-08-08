import java.util.ArrayList;


public class TestXML {
	
	public static void main(String[] args) {
		SetParser parser = new SetParser();
		
		ArrayList<Card> fullList = new ArrayList<Card>(0);
		/*for (int i=0; i<fullList.size();i++) {
			System.out.println(fullList.get(i).getName());
		}*/
		
		AllCards allCards = new AllCards("src/Downloads/fullset.xml");
		
		/*parser.parseOCTGNSet("src/OCTGNSets/opus1/set.xml");
		fullList.addAll(parser.getCardList());
		
		parser.parseOCTGNSet("src/OCTGNSets/opus2/set.xml");
		fullList.addAll(parser.getCardList());
		
		parser.parseOCTGNSet("src/OCTGNSets/opus3/set.xml");
		fullList.addAll(parser.getCardList());
		
		parser.parseOCTGNSet("src/OCTGNSets/opus4/set.xml");
		fullList.addAll(parser.getCardList());
		
		parser.parseOCTGNSet("src/OCTGNSets/opus5/set.xml");
		fullList.addAll(parser.getCardList());
		
		parser.parseOCTGNSet("src/OCTGNSets/opus6/set.xml");
		fullList.addAll(parser.getCardList());
		
		parser.parseOCTGNSet("src/OCTGNSets/opus7/set.xml");
		fullList.addAll(parser.getCardList());
		
		parser.parseOCTGNSet("src/OCTGNSets/opus8/set.xml");
		fullList.addAll(parser.getCardList());
		
		parser.parseOCTGNSet("src/OCTGNSets/opus9/set.xml");
		fullList.addAll(parser.getCardList());*/
		
		parser.writeXML("save/fullset.xml", allCards.getFullList());
	}
	
}
