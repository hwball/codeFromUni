package seng202.group6.model;

/**
 * Class for identified records that contain IATA or ICAO values.
 */
public abstract class IdentifiedRecord extends Record {
    private String iata;
    private String icao;

    /**
     * Constructor for IdentifiedRecord.
     *
     * @param iata IATA string to set
     * @param icao ICAO string to set
     */
    IdentifiedRecord(String iata, String icao) {
        this.iata = iata;
        this.icao = icao;
    }

    /**
     * Gets the International Air Transport Association code for this airline.
     *
     * @return the iata code
     */
    public String getIata() {
        return iata;
    }


    /**
     * Sets the International Air Transport Association code for this airline.
     *
     * @param iata the iata code
     */
    public void setIata(String iata) {
        String old = getIata();
        this.iata = iata;
        setChanged();
        notifyObservers(new FieldChangeNotification(IdentifiedField.IATA, old));
    }


    /**
     * Gets the International Civil Aviation Organization code for this airline.
     *
     * @return the icao code
     */
    public String getIcao() {
        return icao;
    }


    /**
     * Sets the International Civil Aviation Organization code for this airline.
     *
     * @param icao the icao code
     */
    public void setIcao(String icao) {
        String old = getIcao();
        this.icao = icao;
        setChanged();
        notifyObservers(new FieldChangeNotification(IdentifiedField.ICAO, old));
    }

    /**
     * Enum for identified fields.
     */
    public enum IdentifiedField {
        IATA,
        ICAO
    }

    /**
     * Inner class FieldChangeNotification.
     */
    public class FieldChangeNotification {
        private final IdentifiedField field;
        private final String oldValue;

        /**
         * Constructor for FieldChangeNotification.
         *
         * @param field field to set
         * @param old   old value
         */
        FieldChangeNotification(IdentifiedField field, String old) {
            this.field = field;
            oldValue = old;
        }

        /**
         * Getter for the old value.
         *
         * @return old value
         */
        public String getOldValue() {
            return oldValue;
        }

        /**
         * Getter fo the field.
         *
         * @return the field
         */
        public IdentifiedField getField() {
            return field;
        }
    }
}
