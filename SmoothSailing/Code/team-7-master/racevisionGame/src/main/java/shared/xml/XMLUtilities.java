package shared.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;

/**
 * Contains utility functions to convert between xml files and xml class objects.
 */
public class XMLUtilities {


    /**
     * Converts an XML class object to an XML string.
     * @param o The XML class object to convert.
     * @return String containing the serialised XML data.
     * @throws JAXBException Thrown if the object is cannot be serialised to XML.
     */
    public static String classToXML(Object o) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(o.getClass());
        Marshaller jaxbMarshaller = context.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter sw = new StringWriter();

        jaxbMarshaller.marshal(o, sw);
        return sw.toString();
    }

    public static Object xmlToClass(File file, URL schemaURL, Class c) throws ParserConfigurationException, IOException, SAXException, JAXBException {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(file);

        return xmlToClass(document, schemaURL, c);
    }

    public static <T> T xmlToClass(String xml, URL schemaURL, Class<T> c) throws ParserConfigurationException, IOException, SAXException, JAXBException {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

        return xmlToClass(document, schemaURL, c);
    }

    /**
     * Converts an XML file to an XML class (e.g., {@link shared.xml.Race.XMLRace}).
     * @param i The input stream for the XML file.
     * @param schemaURL URL for the XML schema.
     * @param c The XML class to convert to.
     * @param <T> The XML class to convert to.
     * @return The XML class object.
     * @throws ParserConfigurationException Thrown if input cannot be converted to class.
     * @throws IOException Thrown if input cannot be converted to class.
     * @throws SAXException Thrown if input cannot be converted to class.
     * @throws JAXBException Thrown if input cannot be converted to class.
     */
    public static <T> T xmlToClass(InputStream i, URL schemaURL, Class<T> c) throws ParserConfigurationException, IOException, SAXException, JAXBException {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(i);

        return xmlToClass(document, schemaURL, c);
    }

    public static <T> T xmlToClass(Document document, URL schemaURL, Class<T> c) throws ParserConfigurationException, IOException, SAXException, JAXBException {
        JAXBContext jc = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(schemaURL);
        unmarshaller.setSchema(schema);

        return (T) unmarshaller.unmarshal(new DOMSource(document));
    }

    public static boolean validateXML(String file, URL schemaURL){
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = parser.parse(new File(file));

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(schemaURL);
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(document));
        } catch (ParserConfigurationException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (SAXException e) {
            return false;
        }
        return true;
    }


}
