import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

//https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

public class SetParser {

	public final static String OCTGN_GUID_FFTCG = "285a8cdb-1d02-4edd-a9de-1ade9d5a4f43";
	
	ArrayList<Card> cardList = new ArrayList<Card>(0);

	public SetParser() {
		
	}
	
	public SetParser(String filename) {
		parseFullSet(filename);
	}
	
	public void parseFullSet(String filename) {
		cardList = new ArrayList<Card>(0);
		File fXmlFile = new File(filename);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			fatalError("ParserConfigurationException when creating DocumentBuilder");
		} catch (IOException e) {
			fatalError("IOException when parsing " + filename);
		} catch (SAXException e) {
			fatalError("SAXException when parsing " + filename);
		}
		
		doc.getDocumentElement().normalize();
	
		NodeList cards = doc.getElementsByTagName("card");
		
		for (int i = 0; i < cards.getLength(); i++) {
	
			Node card = cards.item(i);
	
			if (card.getNodeType() == Node.ELEMENT_NODE) {
	
				Element eCard = (Element) card;
	
				// Get fields
				String name = eCard.getAttribute("Name");
				String setID = eCard.getAttribute("SetID");
				String guid = eCard.getAttribute("GUID");
				
				String element = eCard.getElementsByTagName("Element").item(0).getTextContent();
				String cost = eCard.getElementsByTagName("Cost").item(0).getTextContent();
				String type = eCard.getElementsByTagName("Type").item(0).getTextContent();
				String multicard = eCard.getElementsByTagName("Multicard").item(0).getTextContent();
				String exburst = eCard.getElementsByTagName("EXBurst").item(0).getTextContent();
				String job = eCard.getElementsByTagName("Job").item(0).getTextContent();
				NodeList catList = eCard.getElementsByTagName("Category");
				String[] categories = new String[catList.getLength()];
				for (int cat = 0; cat < categories.length; cat++) {
					categories[cat]=catList.item(cat).getTextContent();
				}
				String text = eCard.getElementsByTagName("Text").item(0).getTextContent();
				String power = eCard.getElementsByTagName("Power").item(0).getTextContent();
				String rarity = eCard.getElementsByTagName("Rarity").item(0).getTextContent();
				
				// Convert fields to the correct type
	
				int cost_ = 0;
				try {
					cost_ = Integer.parseInt(cost);
				} catch (NumberFormatException e) {
					error("Couldn't parse cost "+cost+" for "+name+" "+setID);
				}
				Card.Element element_ = Card.Element.valueOf(element.toUpperCase());
				Card.Type type_ = Card.Type.valueOf(type.toUpperCase());
				int power_ = 0;
				try {
					power_ = (type_ == Card.Type.FORWARD ? Integer.parseInt(power) : 0);
				} catch (NumberFormatException e) {
					error("Couldn't parse power "+power+" for "+name+" "+setID);
				}
				boolean exburst_ = exburst.toLowerCase().equals("true");
				Card.Rarity rarity_ = Card.Rarity.valueOf(rarity.toUpperCase());
				boolean multicard_ = type_ == Card.Type.SUMMON ? false : multicard.toLowerCase().equals("true");
	
				cardList.add(new Card(name, setID, guid, element_, cost_,
						type_, multicard_, exburst_, job, categories, text,
						power_, rarity_));
			}
		}
		Collections.sort(cardList); //Relies on Card.compareTo using set id only
	}

	public ArrayList<Card> getCardList() {
		return cardList;
	}

	public void parseOCTGNSet(String filename) {
		cardList = new ArrayList<Card>(0);

		File fXmlFile = new File(filename);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (ParserConfigurationException e) {
			fatalError("ParserConfigurationException when creating DocumentBuilder");
		} catch (IOException e) {
			fatalError("IOException when parsing " + filename);
		} catch (SAXException e) {
			fatalError("SAXException when parsing " + filename);
		}

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		NodeList cards = doc.getElementsByTagName("card");

		for (int i = 0; i < cards.getLength(); i++) {

			Node card = cards.item(i);

			if (card.getNodeType() == Node.ELEMENT_NODE) {

				Element eCard = (Element) card;

				// Get fields
				String name = eCard.getAttribute("name");
				String guid = eCard.getAttribute("id");

				String setID = "";
				String element = "";
				String cost = "";
				String type = "";
				String multicard = "";
				String exburst = "";
				String job = "";
				String[] categories = new String[1];
				String text = "";
				String power = "";
				String rarity = "";

				NodeList properties = eCard.getElementsByTagName("property");
				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					if (property.getNodeType() == Node.ELEMENT_NODE) {
						Element eProperty = (Element) property;
						String thisProperty = eProperty.getAttribute("name");
						switch (thisProperty) {
						case "CP":
							cost = eProperty.getAttribute("value");
							break;
						case "Element":
							element = eProperty.getAttribute("value");
							break;
						case "Type":
							type = eProperty.getAttribute("value");
							break;
						case "Job":
							job = eProperty.getAttribute("value");
							break;
						case "Category":
							String categoryString = eProperty.getAttribute("value");
							categories = categoryString.split(" \u25cf ");
							break;
						case "Power":
							power = eProperty.getAttribute("value");
							break;
						case "Abilities":
							text = eProperty.getAttribute("value");
							break;
						case "EXBurst":
							exburst = eProperty.getAttribute("value");
							break;
						case "SetNumber":
							setID = eProperty.getAttribute("value");
							break;
						case "Rarity":
							rarity = eProperty.getAttribute("value");
							break;
						case "Multicard":
							multicard = eProperty.getAttribute("value");
							break;
						default:
						}
					}
				}
				// Convert fields to the correct type

				int cost_ = 0;
				try {
					cost_ = Integer.parseInt(cost);
				} catch (NumberFormatException e) {
					error("Couldn't parse cost "+cost+" for "+name+" "+setID);
				}
				Card.Element element_ = Card.Element.valueOf(element.toUpperCase());
				Card.Type type_ = Card.Type.valueOf(type.toUpperCase());
				int power_ = 0;
				try {
					power_ = type_ == Card.Type.FORWARD ? Integer.parseInt(power) : 0;
				} catch (NumberFormatException e) {
					error("Couldn't parse power "+power+" for "+name+" "+setID);
				}
				boolean exburst_ = exburst.equals("Yes");
				Card.Rarity rarity_ = Card.Rarity.valueOf(rarity.toUpperCase());
				boolean multicard_ = type_ == Card.Type.SUMMON ? false : multicard.equals("True");

				cardList.add(new Card(name, setID, guid, element_, cost_,
						type_, multicard_, exburst_, job, categories, text,
						power_, rarity_));

			}
		}
	}
	
	//https://examples.javacodegeeks.com/core-java/xml/parsers/documentbuilderfactory/create-xml-file-in-java-using-dom-parser-example/
	public void writeXML(String filename, ArrayList<Card> list) {
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = null;
	    try {
	    	db = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			fatalError("ParserConfigurationException when creating DocumentBuilder");
		}
	    Document doc = db.newDocument();
	    
	    //Root element
	    Element root = doc.createElement("cards");
	    doc.appendChild(root);
	    
	    for (int i=0; i<list.size();i++) {
	    	
	    	//Card attributes
	    	Card theCard = list.get(i);
	    	Element card = doc.createElement("card");
	    	root.appendChild(card);
	    	
	    	Attr name = doc.createAttribute("Name");
	    	name.setValue(theCard.getName());
	    	Attr setID = doc.createAttribute("SetID");
	    	setID.setValue(theCard.getSetID());
	    	Attr guid = doc.createAttribute("GUID");
	    	guid.setValue(theCard.getGuid());
	    	
	    	card.setAttributeNode(name);
	    	card.setAttributeNode(setID);
	    	card.setAttributeNode(guid);
	    	
	    	//Card fields
	    	Element element = doc.createElement("Element");
	    	element.appendChild(doc.createTextNode(""+theCard.getElement()));
	    	card.appendChild(element);
	    	
	    	Element cost = doc.createElement("Cost");
	    	cost.appendChild(doc.createTextNode(""+theCard.getCost()));
	    	card.appendChild(cost);
	    	
	    	Element type = doc.createElement("Type");
	    	type.appendChild(doc.createTextNode(""+theCard.getType()));
	    	card.appendChild(type);
	    	
	    	Element multicard = doc.createElement("Multicard");
	    	multicard.appendChild(doc.createTextNode(""+theCard.isMulticard()));
	    	card.appendChild(multicard);
	    	
	    	Element exburst = doc.createElement("EXBurst");
	    	exburst.appendChild(doc.createTextNode(""+theCard.isExburst()));
	    	card.appendChild(exburst);
	    	
	    	Element job = doc.createElement("Job");
	    	job.appendChild(doc.createTextNode(theCard.getJob()));
	    	card.appendChild(job);
	    	
	    	for (int j=0; j<theCard.getCategories().length; j++) {
		    	Element category = doc.createElement("Category");
		    	category.appendChild(doc.createTextNode(theCard.getCategories()[j]));
		    	card.appendChild(category);
	    	}
	    	
	    	Element text = doc.createElement("Text");
	    	text.appendChild(doc.createTextNode(theCard.getText()));
	    	card.appendChild(text);
	    	
	    	Element power = doc.createElement("Power");
	    	power.appendChild(doc.createTextNode(""+theCard.getPower()));
	    	card.appendChild(power);
	    	
	    	Element rarity = doc.createElement("Rarity");
	    	rarity.appendChild(doc.createTextNode(""+theCard.getRarity()));
	    	card.appendChild(rarity);
	    }
	    
	    // create the xml file
	    //transform the DOM Object to an XML File
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = null;
	    try {
	    	transformer = transformerFactory.newTransformer();
	    } catch (TransformerConfigurationException e) {
	    	fatalError("TransformerConfigurationException when creating Transformer");
	    }
	    DOMSource domSource = new DOMSource(doc);
	    StreamResult streamResult = new StreamResult(new File(filename));
	
	    // If you use
	    // StreamResult result = new StreamResult(System.out);
	    // the output will be pushed to the standard output ...
	    // You can use that for debugging 
	
	    try {
	    	transformer.transform(domSource, streamResult);
	    } catch (TransformerException e) {
	    	fatalError("TransformerException when transforming into XML");
	    }
	
	    System.out.println("Created file: "+filename);
	    
	}

	public void writeOCTGNDeck(String filename, ArrayList<Card> list) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = null;
	    try {
	    	db = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			fatalError("ParserConfigurationException when creating DocumentBuilder");
		}
	    Document doc = db.newDocument();
	    
	    //Root element
	    Element deck = doc.createElement("deck");
	    doc.appendChild(deck);
	    
    	Attr game = doc.createAttribute("game");
    	game.setValue(OCTGN_GUID_FFTCG);
    	Attr sleeveid = doc.createAttribute("sleeveid");
    	sleeveid.setValue("0");
	    
    	deck.setAttributeNode(game);
    	deck.setAttributeNode(sleeveid);
    	
    	Element section_forwards = doc.createElement("section");
    	deck.appendChild(section_forwards);
    	Attr sname_forwards = doc.createAttribute("name");
    	sname_forwards.setValue("Forwards");
    	Attr shared_forwards = doc.createAttribute("shared");
    	shared_forwards.setValue("False");
    	section_forwards.setAttributeNode(sname_forwards);
    	section_forwards.setAttributeNode(shared_forwards);
    	
    	Element section_monsters = doc.createElement("section");
    	deck.appendChild(section_monsters);
    	Attr sname_monsters = doc.createAttribute("name");
    	sname_monsters.setValue("Monsters");
    	Attr shared_monsters = doc.createAttribute("shared");
    	shared_monsters.setValue("False");
    	section_monsters.setAttributeNode(sname_monsters);
    	section_monsters.setAttributeNode(shared_monsters);
    	
    	Element section_backups = doc.createElement("section");
    	deck.appendChild(section_backups);
    	Attr sname_backups = doc.createAttribute("name");
    	sname_backups.setValue("Backups");
    	Attr shared_backups = doc.createAttribute("shared");
    	shared_backups.setValue("False");
    	section_backups.setAttributeNode(sname_backups);
    	section_backups.setAttributeNode(shared_backups);
    	
    	Element section_summons = doc.createElement("section");
    	deck.appendChild(section_summons);
    	Attr sname_summons = doc.createAttribute("name");
    	sname_summons.setValue("Summons");
    	Attr shared_summons = doc.createAttribute("shared");
    	shared_summons.setValue("False");
    	section_summons.setAttributeNode(sname_summons);
    	section_summons.setAttributeNode(shared_summons);
    	
    	Element section_notes = doc.createElement("notes");
    	deck.appendChild(section_notes);
    	
    	CDATASection cdata = doc.createCDATASection("");
    	section_notes.appendChild(cdata);
    	
	    Collections.sort(list);
	    
	    int n = 1;
	    
	    for (int i=0; i<list.size(); i+=n) {
	    	Card c = list.get(i);
    		String currentID = c.getSetID();
    		n = 1;
	    	for (int j=i+1; j<list.size(); j++) {
				if (currentID.equals(list.get(j).getSetID())) {
					n++;
				} else { //End loop
					j=list.size();
				}
	    	}
	    	//Add card to deck
	    	Element card = doc.createElement("card");
	    	switch (c.getType()) {
	    	case FORWARD:
		    	section_forwards.appendChild(card);
	    		break;
	    	case MONSTER:
		    	section_monsters.appendChild(card);
	    		break;
	    	case BACKUP:
		    	section_backups.appendChild(card);
	    		break;
	    	case SUMMON:
		    	section_summons.appendChild(card);
	    		break;
	    	}
	    	Attr qty = doc.createAttribute("qty");
	    	qty.setValue(""+n);
	    	Attr id = doc.createAttribute("id");
	    	id.setValue(c.getGuid());
	    	card.setAttributeNode(qty);
	    	card.setAttributeNode(id);
	    }
	    
	    //Create the XML file
	    createXMLFile(doc, filename);
	}
	
	private void createXMLFile(Document doc, String filename) {
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = null;
	    try {
	    	transformer = transformerFactory.newTransformer();
	    } catch (TransformerConfigurationException e) {
	    	fatalError("TransformerConfigurationException when creating Transformer");
	    }
	    DOMSource domSource = new DOMSource(doc);
	    StreamResult streamResult = new StreamResult(new File(filename));
	
	    // If you use
	    // StreamResult result = new StreamResult(System.out);
	    // the output will be pushed to the standard output ...
	    // You can use that for debugging 
	
	    try {
	    	transformer.transform(domSource, streamResult);
	    } catch (TransformerException e) {
	    	fatalError("TransformerException when transforming into XML: "+e.getMessage());
	    }
	
	    System.out.println("Created file: "+filename);
	}
	
	private void error(String str) {
		System.err.println(str);
	}
	
	private void fatalError(String str) {
		System.err.println(str);
		System.exit(0);
	}
}
