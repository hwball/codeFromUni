package seng202.group6.model;

import java.util.Observable;

/**
 * Abstract class for record.
 */
public abstract class Record extends Observable {
    /**
     * Checks for for equality.
     *
     * @param object object to check
     * @return true if equal, false otherwise
     */
    @Override
    public abstract boolean equals(Object object);


    /**
     * Checks for typing.
     *
     * @param object object
     * @return true if object is instance of route, false otherwise
     */
    protected abstract boolean canEqual(Object object);


    @Override
    public abstract int hashCode();


    @Override
    public abstract String toString();


    /**
     * Cleans up observers by removing them.
     */
    public void cleanUp() {
        deleteObservers();
    }

}
