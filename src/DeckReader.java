import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class DeckReader {
	ArrayList<Card> theDeck = new ArrayList<Card>(0);
	
	public DeckReader(File f) {
		theDeck = readFile(f);
	}
	
	public ArrayList<Card> getDeck() {
		return theDeck;
	}
	
	private ArrayList<Card> readFile(File f) {
		ArrayList<Card> list = new ArrayList<Card>(0);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			try {
				String line = br.readLine();
				
				int section = 0; //Not reading cards yet
				//1 is forwards
				//2 is summons
				//3 is backups
				//4 is monsters
				
				
				//MAIN LOOP
				while (line!=null) {
					
					if (line.contains("Forward(")) {
						section = 1;
					} else if (line.contains("Summon(")) {
						section = 2;
					} else if (line.contains("Backup(")) {
						section = 3;
					} else if (line.contains("Monster(")) {
						section = 4;
					} else if (section>0 && !line.isEmpty()) { //Only read if reading cards
						//Take ID off
						String name = line.substring(2,line.length()-8);
						String id = line.substring(line.length()-6, line.length()-1);
						int qty = Integer.parseInt(line.charAt(0)+"");

						Card.Type type = Card.Type.FORWARD;
						
						switch (section) {
						case 1:
							type = Card.Type.FORWARD;
							break;
						case 2:
							type = Card.Type.SUMMON;
							break;
						case 3:
							type = Card.Type.BACKUP;
							break;
						case 4:
							type = Card.Type.MONSTER;
							break;
						}
						
						list.add(new Card(name, id, qty, type));
					}


					line = br.readLine();
				}
				
			} catch (IOException e) {
				System.out.println("IO Exception reading from file "+f.getName());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find file "+f.getName());
		}
		
		return list;
	}
}
