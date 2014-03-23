package com.pragathoys.lib.net;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marjohn
 */
public class Rest {
    public final static String PROTOCOL_HTTP = "http";
    public final static String PROTOCOL_HTTPS = "https";

    private String server_ip;
    private String protocol;
    
    public Rest(String protocol,String server_ip) {
        this.protocol = protocol;
        this.server_ip = server_ip;
        
    }
    
    public boolean authenticate(String api_key){
        boolean result = false;
        String cmd = "/rest/authenticate/" + api_key;
        send_cmd(cmd);
        return result;
    }
    
    private void send_cmd(String cmd){
        String url_string = protocol + "://" + server_ip + cmd;
        URL url = null;
        try {
            url = new URL(url_string);
            URLConnection conn = url.openConnection();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String content = "";
            while( (line = reader.readLine())!=null){
                content += line;
            }
            
            Log.d("REST", "content : " + content);
        } catch (MalformedURLException ex) {
            Log.d("REST", "MalformedURLException");
            Logger.getLogger(Rest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Log.d("REST", "IOException");
            Logger.getLogger(Rest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
