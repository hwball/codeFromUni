package shared.dataInput;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import shared.enums.XMLFileType;
import shared.exceptions.XMLReaderException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Base Reader for XML Files
 */
public abstract class XMLReader {

    protected Document doc;


    /**
     * Reads an XML file.
     * @param file The file to read.
     * @param type How to read the file - e.g., load as resource.
     * @throws XMLReaderException Throw if the file cannot be parsed.
     */
    public XMLReader(String file, XMLFileType type) throws XMLReaderException {


        InputStream xmlInputStream = null;

        //Create an input stream. Method depends on type parameter.

        if (type == XMLFileType.Contents) {
            //Wrap file contents in input stream.
            xmlInputStream = new ByteArrayInputStream(file.getBytes(StandardCharsets.UTF_8));

        } else if (type == XMLFileType.ResourcePath) {
            xmlInputStream = XMLReader.class.getClassLoader().getResourceAsStream(file);

        } else if (type == XMLFileType.FilePath) {
            try {
                xmlInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new XMLReaderException("Could not open file " + file, e);
            }

        }



        this.doc = parseInputStream(xmlInputStream);

    }


    /**
     * Reads an XML file from an input stream.
     * @param xmlInputStream The input stream to parse.
     * @throws XMLReaderException Thrown if the input stream cannot be parsed.
     */
    public XMLReader(InputStream xmlInputStream) throws XMLReaderException {

        this.doc = parseInputStream(xmlInputStream);
    }


    /**
     * Parses an input stream into a document.
     * @param inputStream The xml input stream to parse.
     * @return The parsed document.
     * @throws XMLReaderException Thrown when a document builder cannot be constructed, or the stream cannot be parsed.
     */
    private static Document parseInputStream(InputStream inputStream) throws XMLReaderException {

        //Create document builder.
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XMLReaderException("Could not create a DocumentBuilder.", e);
        }

        //Parse document.
        Document document = null;
        try {
            document = dBuilder.parse(inputStream);
        } catch (SAXException | IOException e) {
            throw new XMLReaderException("Could not parse the xml input stream.", e);
        }
        document.getDocumentElement().normalize();

        return document;
    }


    /**
     * Return Document data of the read-in XML
     * @return XML document
     */
    public Document getDocument() {
        return doc;
    }

    /**
     * Get content of a tag in an element
     * @param n Element to read tags from
     * @param tagName Name of the tag
     * @return Content of the tag
     */
    public String getTextValueOfNode(Element n, String tagName) {
        return n.getElementsByTagName(tagName).item(0).getTextContent();
    }

    /**
     * Get attributes for an element
     * @param n Element to read attributes from
     * @param attr Attributes of element
     * @return Attributes of element
     */
    public String getAttribute(Element n, String attr) {
        return n.getAttribute(attr);
    }

    protected boolean exists(Node node, String attribute) {
        return node.getAttributes().getNamedItem(attribute) != null;
    }

    /**
     * Get the contents of the XML FILe.
     * @param document holds all xml information
     * @return String representation of document
     * @throws TransformerException when document is malformed, and cannot be turned into a string
     */
    public static String getContents(Document document) throws TransformerException {
        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);
        transformer.transform(source, result);

        return stringWriter.toString();
    }


    /**
     * Reads the an XML file, as a resource, into a string.
     * @param path path of the XML
     * @param encoding encoding of the xml
     * @return A string containing the contents of the specified file.
     * @throws XMLReaderException Thrown if file cannot be read for some reason.
     */
    public static String readXMLFileToString(String path, Charset encoding) throws XMLReaderException {

        InputStream fileStream = XMLReader.class.getClassLoader().getResourceAsStream(path);

        //Resource can't be found.
        if (fileStream == null) {
            throw new XMLReaderException("Could not open resource: " + path, new IOException());
        }

        Document doc = XMLReader.parseInputStream(fileStream);

        doc.getDocumentElement().normalize();

        try {
            return XMLReader.getContents(doc);
        } catch (TransformerException e) {
            throw new XMLReaderException("Could not get XML file contents.", e);
        }

    }

}
