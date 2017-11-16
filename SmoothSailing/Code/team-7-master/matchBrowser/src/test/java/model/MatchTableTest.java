package model;

import network.Messages.Enums.RaceStatusEnum;
import network.Messages.HostGame;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MatchTableTest {
    private MatchTable testTable;

    @Before
    public void setUp() {
        testTable = new MatchTable();
    }

    @Test
    public void testTable() {
        HostGame entry = new HostGame("127.0.0.1", 4942, (byte)1, (byte)1, RaceStatusEnum.PRESTART, (byte)6, (byte)1);

        testTable.addEntry(new ClientAddress("127.0.0.1", 3779), entry);

        assertEquals(testTable.getMatchTable().get(new ClientAddress("127.0.0.1", 4942)), entry);
    }
}
