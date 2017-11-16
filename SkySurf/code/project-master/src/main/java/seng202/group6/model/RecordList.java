package seng202.group6.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/**
 * Generic superclass for lists of records.
 * <p>
 * Only pass a record as an argument to notifyObservers when the record has been added. For all
 * other situations where notifyObservers is called do not pass any arguments.
 */
public class RecordList<T extends Record> extends Observable implements Observer {

    /**
     * The name of the RecordList.
     */
    private String name;
    /**
     * The ArrayList of Records the RecordList holds.
     */
    protected final ArrayList<T> records;


    /**
     * Creates a RecordList with the specified name and no records.
     *
     * @param name the name of the RecordList
     */
    public RecordList(String name) {
        this.name = name;
        this.records = new ArrayList<>();
    }


    /**
     * Creates a RecordList with the specified name that holds the given records.
     *
     * @param name    the name of the RecordList
     * @param records the records to store in the record list
     */
    public RecordList(String name, Collection<T> records) {
        this.name = name;
        this.records = new ArrayList<>();
        addAll(records);
    }


    /**
     * Gets the size of the internal data list.
     *
     * @return the size of the internal records
     */
    public int size() {
        return records.size();
    }


    /**
     * Applies the given filter to the internally stored records.
     *
     * @param filter the filter to compare records against
     * @return a List containing the matching records
     */
    public List<T> searchByCriteria(Filter<T> filter) {
        return records.stream()
                //.filter(record -> filter.matches(record))
                .filter(filter::matches)
                .collect(Collectors.toList());
    }


    /**
     * Adds the record to the internal list.
     * For efficiency regarding notifying observers, only use where a single record is added.
     *
     * @param record the record to be added
     * @return true if the record was added, else false
     */
    public boolean add(T record) {
        if (!this.records.contains(record)) {
            record.addObserver(this);
            this.records.add(record);
            setChanged();
            notifyObservers(record);
            return true;
        }
        return false;
    }


    /**
     * Adds all the given records to the internal list.
     * Use whenever more than one record is added as it is only notifies observers once.
     *
     * @param records a collection containing the records to add
     */
    public void addAll(Collection<T> records) {
        HashSet<T> recordsAlreadyPresent = new HashSet<>(this.records);
        for (T record : records) {
            if (!recordsAlreadyPresent.contains(record)) {
                record.addObserver(this);
                this.records.add(record);
                recordsAlreadyPresent.add(record);
            }
        }
        setChanged();
        notifyObservers();
    }


    /**
     * Removes the given record from the internal list.
     *
     * @param record the record to be removed
     * @return true if the record was removed, else false
     */
    public boolean remove(T record) {
        record.deleteObserver(this);
        Boolean removed = records.remove(record);
        if (removed) {
            setChanged();
            notifyObservers();
        }
        return removed;
    }


    /**
     * Removes the record at index i in the internal list.
     *
     * @param i the index of the item to be removed
     * @return the removed record
     * @throws IndexOutOfBoundsException if the index is not valid
     */
    public T remove(int i) throws IndexOutOfBoundsException {
        T record = records.get(i);
        T removed = records.remove(i);
        record.deleteObserver(this);
        setChanged();
        notifyObservers();
        return removed;
    }


    /**
     * Get the internal record list.
     *
     * @return an unmodifiable version of the internal record list
     */
    public List<T> getRecords() {
        return Collections.unmodifiableList(this.records);
    }


    /**
     * Get the record at index i of the internal list.
     *
     * @param i the given index
     * @return the airline at index i of the list
     */
    public T get(int i) {
        return records.get(i);
    }


    /**
     * Sets the name.
     *
     * @param name the name to use
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * Used to update observers.
     */
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }


    /**
     * Cleans up observers by removing them.
     */
    public void cleanUp() {
        records.forEach(Record::cleanUp);
        this.deleteObservers();
    }
}
