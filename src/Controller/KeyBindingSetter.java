package Controller;

import Model.MenuModel.KeyBindings;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class KeyBindingSetter implements EventHandler<KeyEvent> {

    private GameLoop gameLoop;
    private int selectedLeftRight;
    private int selectedUpDown;
    private KeyBindings keyBindings;

    public KeyBindingSetter(GameLoop gameLoop, int selectedLeftRight, int selectedUpDown) {
        this.gameLoop = gameLoop;
        this.selectedLeftRight = selectedLeftRight;
        this.selectedUpDown = selectedUpDown;
        this.keyBindings = new KeyBindings();
    }

    @Override
    public void handle(KeyEvent event) {

        String bindingToChange = keyBindings.getBinding(selectedLeftRight);

        String keyToChange = keyBindings.getKey(keyBindings.getBinding(selectedLeftRight), selectedUpDown).getKey();

        File entityKeyBindings = new File("resources/KeyBindings/"+bindingToChange);

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

                    if(eElement.getAttribute("type").equals(keyToChange)){
                        eElement.getElementsByTagName("key").item(0).setTextContent(event.getCode().getName());
                    }
                }
            }


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("resources/KeyBindings/"+bindingToChange));
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        gameLoop.setControls();
    }
}
