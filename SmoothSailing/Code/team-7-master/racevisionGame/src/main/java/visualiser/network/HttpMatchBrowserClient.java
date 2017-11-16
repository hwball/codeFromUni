package visualiser.network;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;
import shared.utils.JsonReader;
import visualiser.model.RaceConnection;

import java.io.IOException;

/**
 * Created by Gondr on 19/09/2017.
 */
public class HttpMatchBrowserClient extends Thread {
    public ObservableList<RaceConnection> connections = FXCollections.observableArrayList();

    /**
     * Get all the matches that have been running in the past 5 seconds.
     */
    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                JSONArray cons = JsonReader.readJsonFromUrlArray("http://api.umbrasheep.com/seng/get_matches/");
                connections.clear();

                for (int i = 0; i < cons.length(); i++) {
                    JSONObject con = (JSONObject) cons.get(i);
                    //using "ip_address" will give their public ip
                    connections.add(new RaceConnection((String) con.get("local_ip"), con.getInt("port"), "Boat Game"));
                }

                Thread.sleep(5000);
            } catch (IOException e) {
                //e.printStackTrace();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }
}
