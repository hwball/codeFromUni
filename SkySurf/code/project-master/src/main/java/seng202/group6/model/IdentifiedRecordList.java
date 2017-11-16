package seng202.group6.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

import seng202.group6.model.IdentifiedRecord.FieldChangeNotification;
import seng202.group6.model.IdentifiedRecord.IdentifiedField;


/**
 * Record list that allows record to be accessed by IATA and ICAO.
 */
public class IdentifiedRecordList<T extends IdentifiedRecord> extends RecordList<T> {
    /**
     * A mapping from IATA to a list of all records that have that IATA.
     */
    private final HashMap<String, List<T>> recordsByIATA = new HashMap<>();
    /**
     * A mapping from ICAO to a list of all records that have that ICAO.
     */
    private final HashMap<String, List<T>> recordsByICAO = new HashMap<>();


    /**
     * Create an instance with no records.
     *
     * @param name name of IdentifiedRecordList
     */
    public IdentifiedRecordList(String name) {
        super(name);
    }


    /**
     * Create an instance and add the given records.
     *
     * @param name    name of IdentifiedRecordList
     * @param records records to be contained in the list
     */
    public IdentifiedRecordList(String name, Collection<T> records) {
        super(name);
        addAll(records);
    }


    @Override
    public boolean add(T record) {
        addReference(record);
        boolean added = super.add(record);
        if (!added) {
            removeReference(record);
        }
        return added;
    }


    @Override
    public void addAll(Collection<T> records) {
        HashSet<T> recordsAlreadyPresent = new HashSet<>(this.records);
        records.stream().filter(record -> !recordsAlreadyPresent.contains(record)).forEach(record -> {
            record.addObserver(this);
            this.records.add(record);
            addReference(record);
            recordsAlreadyPresent.add(record);
        });
        setChanged();
        notifyObservers();
    }


    @Override
    public boolean remove(T record) {
        removeReference(record);
        return super.remove(record);
    }


    @Override
    public T remove(int i) throws IndexOutOfBoundsException {
        try {
            T removed = records.remove(i);
            removed.deleteObserver(this);
            removeReference(removed);
            setChanged();
            notifyObservers();
            return removed;
        } catch (NullPointerException e) {
            throw new IndexOutOfBoundsException();
        }
    }


    /**
     * Checks to see if the given code maps to exactly one record in the list.
     *
     * @param code IATA or ICAO to look up
     * @return true if the passed iata or icao are contained in collection and the record is the
     * only one referenced by that code
     */
    public boolean recordUniquelyPresent(String code) {
        return containsUniqueICAO(code) || containsUniqueIATA(code);
    }


    /**
     * Adds the record to the mapping from IATA and ICAO.
     *
     * @param record the record to add
     */
    private void addReference(T record) {
        addReferenceIATA(record);
        addReferenceICAO(record);
    }


    /**
     * Adds the record to the mapping from IATA.
     *
     * @param record the record to add
     */
    private void addReferenceIATA(T record) {
        if (!record.getIata().isEmpty()) {
            if (recordsByIATA.get(record.getIata()) != null) {
                recordsByIATA.get(record.getIata()).add(record);
            } else {
                ArrayList<T> list = new ArrayList<>();
                list.add(record);
                recordsByIATA.put(record.getIata(), list);
            }
        }
    }


    /**
     * Adds the record to the mapping from ICAO.
     *
     * @param record the record to add
     */
    private void addReferenceICAO(T record) {
        if (!record.getIcao().isEmpty()) {
            if (recordsByICAO.get(record.getIata()) != null) {
                recordsByICAO.get(record.getIata()).add(record);
            } else {
                ArrayList<T> list = new ArrayList<>();
                list.add(record);
                recordsByICAO.put(record.getIcao(), list);
            }
        }
    }


    /**
     * Removes the record from the mapping from IATA and ICAO.
     *
     * @param record the record to remove
     */
    private void removeReference(T record) {
        removeReferenceIATA(record, record.getIata());
        removeReferenceICAO(record, record.getIcao());
    }


    /**
     * Removes a reference to a record by IATA.
     *
     * @param record  record to remove
     * @param oldIATA IATA reference to remove
     */
    private void removeReferenceIATA(T record, String oldIATA) {
        if (recordsByIATA.containsKey(oldIATA)) {
            recordsByIATA.get(oldIATA).remove(record);
            if (recordsByIATA.get(oldIATA).size() == 0) {
                recordsByICAO.remove(oldIATA);
            }
        }
    }

    /**
     * Removes a reference to a record by ICAO.
     *
     * @param record  record to remove
     * @param oldICAO ICAO reference to remove
     */
    private void removeReferenceICAO(T record, String oldICAO) {
        if (recordsByICAO.containsKey(oldICAO)) {
            recordsByICAO.get(oldICAO).remove(record);
            if (recordsByICAO.get(oldICAO).size() == 0) {
                recordsByICAO.remove(oldICAO);
            }
        }
    }


    /**
     * Checks to see if the collection contains exactly one record with that IATA
     *
     * @param iata IATA to check
     * @return true if that IATA maps to only one record, false if the IATA is empty or does not map
     * to exactly one record.
     */
    boolean containsUniqueIATA(String iata) {
        return !iata.isEmpty() && recordsByIATA.containsKey(iata) && recordsByIATA.get(iata).size() == 1;
    }


    /**
     * Checks to see if the collection contains exactly one record with that ICAO
     *
     * @param icao ICAO to check
     * @return true if that ICAO maps to only one record, false if the ICAO is empty or does not map
     * to exactly one record.
     */
    boolean containsUniqueICAO(String icao) {
        return !icao.isEmpty() && recordsByICAO.containsKey(icao) && recordsByICAO.get(icao).size() == 1;
    }

    /**
     * Gets the record referenced by the given code, as long as the code references exacttly one record.
     * @param id //TODO
     * @return the record if the code references exactly one
     */
    public T getById(String id) {
        T record = null;
        if (!id.isEmpty()) {
            if (containsUniqueIATA(id)) {
                record = recordsByIATA.get(id).get(0);
            } else if (containsUniqueICAO(id)) {
                record = recordsByICAO.get(id).get(0);
            }
        }
        return record;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable obs, Object obj) {
        if (obs instanceof IdentifiedRecord && obj instanceof FieldChangeNotification) {
            FieldChangeNotification notification = (FieldChangeNotification) obj;
            IdentifiedField field = notification.getField();
            switch (field) {
                case IATA:
                    removeReferenceIATA((T) obs, notification.getOldValue());
                    addReferenceIATA((T) obs);
                    break;
                case ICAO:
                    removeReferenceICAO((T) obs, notification.getOldValue());
                    addReferenceICAO((T) obs);
                    break;
            }
        }
        super.update(obs, obj);
    }
}
