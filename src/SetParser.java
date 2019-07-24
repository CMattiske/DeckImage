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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

public class SetParser {

	ArrayList<Card> cardList = new ArrayList<Card>(0);

	public SetParser() {
		
	}
	
	public SetParser(String filename) {
		parseFullSet(filename);
	}
	
	public void parseFullSet(String filename) {
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
				String category = eCard.getElementsByTagName("Category").item(0).getTextContent();
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
						type_, multicard_, exburst_, job, category, text,
						power_, rarity_));
			}
		}
	}

	public ArrayList<Card> getCardList() {
		return cardList;
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
	    	
	    	Element category = doc.createElement("Category");
	    	category.appendChild(doc.createTextNode(theCard.getCategory()));
	    	card.appendChild(category);
	    	
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

	public void parseOCTGNSet(String filename) {

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
				String category = "";
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
							category = eProperty.getAttribute("value");
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
						type_, multicard_, exburst_, job, category, text,
						power_, rarity_));

			}
		}
	}
	
	private void error(String str) {
		System.err.println(str);
	}
	
	private void fatalError(String str) {
		System.err.println(str);
		System.exit(0);
	}
}
