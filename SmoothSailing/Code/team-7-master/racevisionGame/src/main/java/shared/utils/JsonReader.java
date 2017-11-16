package shared.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * A helper class that has functions to read information from a url to json object.
 */
public class JsonReader {

    /**
     * Reads all data from a Reader
     * @param rd reader to read from
     * @return string that the reader has currently read
     * @throws IOException if the reader is invalid
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Reads a Json Object from a URL
     * @param url url to read from
     * @return JSONObject that has been read
     * @throws IOException if the reader cannot obtain information
     * @throws JSONException if the read information is not json parsable.
     */
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (JSONException e) {
            return null;
        } finally {
            is.close();
        }
    }

    /**
     * Reads a Json Array from a URL
     * @param url url to read from
     * @return JSONArray that has been read
     * @throws IOException if the reader cannot obtain information
     * @throws JSONException if the read information is not json parsable.
     */
    public static JSONArray readJsonFromUrlArray(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
