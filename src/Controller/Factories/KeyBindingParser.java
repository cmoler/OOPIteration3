package Controller.Factories;

import javafx.scene.input.KeyCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class KeyBindingParser {

    public KeyBindingParser(){

    }

    public KeyCode parsePlayerKey(String keyName) {
        try {
            return parseKey("player", keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyCode parseMenuKey(String keyName) {
        try {
            return parseKey("menu", keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyCode parseKey(String fileName, String keyName) throws Exception {
        File entityKeyBindings = new File("resources/KeyBindings/" + fileName+ ".xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(entityKeyBindings);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Bind");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("type").equals(keyName)){
                        return KeyCode.getKeyCode(eElement.
                                getElementsByTagName("key").
                                item(0).
                                getTextContent());
                    }
                }

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new Exception("Key not in menu keybinding: " + keyName);
    }
}
