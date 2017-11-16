package seng202.group6.utils;


/**
 * Holds a representation of a condition set in a Filter to be manipulated in the
 * AdvFilterController
 */
public class AdvFilterCondition {

    private final RecordType dataType;
    private final String searchType;
    private final String searchTerm;

    /**
     * Constructs a AdvFilterCondition.
     *
     * @param dataType   the type of record to filter on
     * @param searchType the field of the record to filter on
     * @param searchTerm the value of the field of the object to filter on
     */
    public AdvFilterCondition(RecordType dataType, String searchType, String searchTerm) {
        this.dataType = dataType;
        this.searchType = searchType;
        this.searchTerm = searchTerm;
    }


    /**
     * Getter for the dataType property.
     *
     * @return the dataType
     */
    public RecordType getDataType() {
        return dataType;
    }


    /**
     * Getter for the searchType property.
     *
     * @return the searchType
     */
    public String getSearchType() {
        return searchType;
    }


    /**
     * Getter for the searchTerm property.
     *
     * @return the searchTerm
     */
    public String getSearchTerm() {
        return searchTerm;
    }
}
