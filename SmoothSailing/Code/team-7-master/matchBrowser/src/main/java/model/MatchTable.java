package model;


import network.Messages.HostGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Holds a table object that stores current games
 */
public class MatchTable {
    private HashMap<ClientAddress, HostGame> matchTable;

    public MatchTable() {
        this.matchTable = new HashMap<>();
    }

    public void addEntry(ClientAddress address, HostGame newEntry) {
        this.matchTable.put(address, newEntry);
    }

    public HashMap<ClientAddress, HostGame> getMatchTable() {
        return matchTable;
    }

    @Override
    public String toString() {
        return  "MatchTable=" + matchTable;
    }
}
