import java.util.ArrayList;
import java.util.Random;


public class WWGenerator {
	
	public final static double MAGUS_CHANCE = 0.8;
	
	Random rand = new Random();
	ArrayList<CardInDeck> list = new ArrayList<CardInDeck>(0);
	
	public WWGenerator() {
		generateList();
	}
	
	private ArrayList<CardInDeck> chooseFrom(int n, ArrayList<CardInDeck> pool) {
		ArrayList<CardInDeck> taken = new ArrayList<CardInDeck>(n);
		for (int i=0; i<n; i++) {
			int r2 = rand.nextInt(pool.size());
			taken.add(pool.get(r2));
			pool.remove(r2);
		}
		return taken;
	}
	
	public void generateList() {
		list = new ArrayList<CardInDeck>(0);
		list.add(new CardInDeck("Porom","9-115",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Porom","9-115",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Porom","9-115",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Lenna","3-144",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Lenna","3-144",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Lenna","3-144",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Paine","1-199",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Paine","1-199",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Paine","1-199",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Fina","8-060",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Fina","8-060",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Fina","8-060",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Zidane","3-056",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Zidane","3-056",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Zidane","3-056",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Famfrit, the Darkening Cloud","3-123",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Famfrit, the Darkening Cloud","3-123",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Famfrit, the Darkening Cloud","3-123",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Diabolos","5-062",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Diabolos","5-062",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Diabolos","5-062",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Valefor","1-198",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Valefor","1-198",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Valefor","1-198",CardInDeck.Type.SUMMON));
		list.add(new CardInDeck("Yuna","1-177",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Yuna","1-177",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Yuna","1-177",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Rikku","1-089",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Rikku","1-089",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Rikku","1-089",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Shinra","6-048",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Shinra","6-048",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Shinra","6-048",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Paine","2-063",CardInDeck.Type.FORWARD));
		list.add(new CardInDeck("Miounne","5-067",CardInDeck.Type.BACKUP));
		list.add(new CardInDeck("Miounne","5-067",CardInDeck.Type.BACKUP));
		
		int total = 36;
		
		//Magus Sisters?
		double r1 = rand.nextDouble();
		if (r1<MAGUS_CHANCE) {
			list.add(new CardInDeck("The Magus Sisters","9-056",CardInDeck.Type.FORWARD));
			list.add(new CardInDeck("The Magus Sisters","9-056",CardInDeck.Type.FORWARD));
			list.add(new CardInDeck("The Magus Sisters","9-056",CardInDeck.Type.FORWARD));
			total+=3;
		}
		
		//Fill backups
		//Wind backups
		//Choose 3 from 2 Echo, 2 King Tycoon, 1 Miounne
		ArrayList<CardInDeck> pool1 = new ArrayList<CardInDeck>(5);
		pool1.add(new CardInDeck("Echo","5-053",CardInDeck.Type.BACKUP));
		pool1.add(new CardInDeck("Echo","5-053",CardInDeck.Type.BACKUP));
		pool1.add(new CardInDeck("King Tycoon","3-059",CardInDeck.Type.BACKUP));
		pool1.add(new CardInDeck("King Tycoon","3-059",CardInDeck.Type.BACKUP));
		pool1.add(new CardInDeck("Miounne","5-067",CardInDeck.Type.BACKUP));
		
		list.addAll(chooseFrom(3, pool1));

		//Water backups
		//Choose 3 from 3 Merlwyb, 2 Leonora
		ArrayList<CardInDeck> pool2 = new ArrayList<CardInDeck>(5);
		pool2.add(new CardInDeck("Merlwyb","2-137",CardInDeck.Type.BACKUP));
		pool2.add(new CardInDeck("Merlwyb","2-137",CardInDeck.Type.BACKUP));
		pool2.add(new CardInDeck("Merlwyb","2-137",CardInDeck.Type.BACKUP));
		pool2.add(new CardInDeck("Leonora","3-143",CardInDeck.Type.BACKUP));
		pool2.add(new CardInDeck("Leonora","3-143",CardInDeck.Type.BACKUP));
		
		list.addAll(chooseFrom(3, pool2));
		
		total += 6;
		
		//Fill Forwards
		//Choose 5 from 3 Veritas, 2 Yiazmat, 1 Y'shtola, 1 Aerith
		//Or choose 8 from 3 Veritas, 2 Yiazmat, 2 Y'shtola, 1 Aerith, 2 Cuchulainn
		ArrayList<CardInDeck> pool3 = new ArrayList<CardInDeck>(0);
		if (total<=42) {
			pool3.add(new CardInDeck("Veritas of the Dark","8-136",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Veritas of the Dark","8-136",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Veritas of the Dark","8-136",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Yiazmat","9-057",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Yiazmat","9-057",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Y'shtola","5-068",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Y'shtola","5-068",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Aerith","8-049",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Cuchulainn, the Impure","2-133",CardInDeck.Type.SUMMON));
			pool3.add(new CardInDeck("Famfrit","9-113",CardInDeck.Type.SUMMON));
			pool3.add(new CardInDeck("Cloud of Darkness","5-126",CardInDeck.Type.FORWARD));
					
			list.addAll(chooseFrom(7, pool3));
		} else if (total<=45) {
			pool3.add(new CardInDeck("Veritas of the Dark","8-136",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Veritas of the Dark","8-136",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Veritas of the Dark","8-136",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Yiazmat","9-057",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Yiazmat","9-057",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Y'shtola","5-068",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Y'shtola","5-068",CardInDeck.Type.FORWARD));
			pool3.add(new CardInDeck("Aerith","8-049",CardInDeck.Type.FORWARD));
					
			list.addAll(chooseFrom(4, pool3));
		}
		
		pool3.addAll(pool1);
		pool3.addAll(pool2);
		
		list.addAll(chooseFrom(1, pool3));
	}
	
	public ArrayList<CardInDeck> getList() {
		return list;
	}
}
