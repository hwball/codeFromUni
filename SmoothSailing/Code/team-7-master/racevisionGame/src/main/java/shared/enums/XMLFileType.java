package shared.enums;

/**
 * Represents the various ways in which we want to open/read an xml file - e.g., a string may contain the file contents, or the file path.
 */
public enum XMLFileType {

    /**
     * This means that a provided string contains the contents of an XML file.
     */
    Contents,

    /**
     * This means that a provided string contains the path to an XML file.
     */
    FilePath,

    /**
     * This means that a provided string contains the path, to be loaded as a resource, of an XML file.
     */
    ResourcePath;


}
