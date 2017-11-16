package visualiser.network;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Creates an Http connection that hosts a game
 */
public class HttpMatchBrowserHost extends Thread {
    private HttpClient httpClient;
    private List<NameValuePair> params;

    public static HttpMatchBrowserHost httpMatchBrowserHost = null;

    /**
     * Constructor, this sends out the creation message of the race.
     * the thread should be run as soon as possible as the race is only valid for 5 seconds
     * until it requires a heartbeat from the start function.
     * @throws IOException if the hosting url is unreachable.
     */
    public HttpMatchBrowserHost() throws IOException {
        httpMatchBrowserHost = this;
        httpClient = HttpClients.createDefault();

        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        boolean matches = false;
        String ip = "";
        Pattern localIpPattern = Pattern.compile("192\\.168\\.1\\..*");
        Pattern ipPattern = Pattern.compile("192\\.168\\..{0,3}\\..*");
        while(e.hasMoreElements())
        {
            if (matches){
                break;
            }
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = ee.nextElement();
                matches = ipPattern.matcher(i.getHostAddress()).matches();
                System.out.println(i.getHostAddress());
                if (matches){
                    if (!localIpPattern.matcher(i.getHostAddress()).matches()) {
                        ip = i.getHostAddress();
                        //break;
                    }
                }
            }
        }

        // Request parameters and other properties.
        params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("ip", ip));
        params.add(new BasicNameValuePair("port", "4942"));
        params.add(new BasicNameValuePair("magic", "Thomas and Seng"));

        sendHttp("http://api.umbrasheep.com/seng/registermatch/");
    }

    /**
     * Sends a post to a server.
     * @param domain url of to send to
     * @throws IOException if the url is unreachable.
     */
    public void sendHttp(String domain) throws IOException {
        HttpPost httppost = new HttpPost(domain);
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        //Execute and get the response.
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                // do something useful
            } finally {
                instream.close();
            }
        } else {
            throw new IOException("No Response from Host");
        }
    }

    public void sendStarted() throws IOException {
        sendHttp("http://api.umbrasheep.com/seng/match_started/");
    }

    /**
     * THe host starts sending out heartbeat messages every 2 seconds.
     */
    @Override
    public void run() {
        while(!Thread.interrupted()){
            try {
                sendHttp("http://api.umbrasheep.com/seng/keep_match_alive/");
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
