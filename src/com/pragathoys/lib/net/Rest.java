package com.pragathoys.lib.net;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author marjohn
 */
public class Rest {
    public final static String PROTOCOL_HTTP = "http";
    public final static String PROTOCOL_HTTPS = "https";

    private String server_ip;
    private String server_port;
    private String protocol;
    
    public Rest(String protocol,String server_ip,String server_port) {
        this.protocol = protocol;
        this.server_ip = server_ip;        
        this.server_port = server_port.trim();
    }
    
    public Map<String, String> authenticate(String api_key){
        Map<String, String> reply;
        String cmd = "/rest/authenticate/" + api_key;
        String response = send_cmd(cmd);
        reply = parse_response( response);
        return reply;
    }
    
    private String send_cmd(String cmd){
        String response = "";

        URL url = null;
        try {            
            if(!server_port.equals("")){
                int port = Integer.parseInt(server_port);
                url = new URL(protocol,server_ip,port,cmd);
            }else{
                url = new URL(protocol,server_ip,cmd);
            }            
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);            
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String content = "";
            while( (line = reader.readLine())!=null){
                content += line;
            }
            response = content;
            
            Log.d("REST", "content : " + content);
        } catch (MalformedURLException ex) {
            response = fail_xml_msg("ERROR: Wrong URL for the server");
            Log.d("REST", "MalformedURLException");
            Logger.getLogger(Rest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketTimeoutException ex) {
            Log.d("REST", "SocketTimeoutException");
            response = fail_xml_msg("ERROR: Connection to the server timeout");
            Logger.getLogger(Rest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Log.d("REST", "IOException");
            response = fail_xml_msg("ERROR: Could not connect to the server");
            Logger.getLogger(Rest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    private Map<String, String> parse_response(String response){
        Map<String, String> reply = new HashMap<String, String>();
        // Clear the XML document
//        String parsed_reponse = response.replaceAll("<xml? version=\"1.0\" encoding=\"utf-8\" ?>", "");
//        parsed_reponse = parsed_reponse.replaceAll("<response>", "");
//        parsed_reponse = parsed_reponse.replaceAll("</response>", "");
        Matcher matcher;
        Pattern pattern_status = Pattern.compile("<status>(.*)</status>");
        Pattern pattern_content = Pattern.compile("<content>(.*)</content>");
        
        try {
            matcher = pattern_status.matcher(response);
            while (matcher.find()) {
                Log.d("REST", "Start index: " + matcher.start());
                Log.d("REST", "End index " + matcher.end());
                Log.d("REST", "group " + matcher.group());
                String item = matcher.group();
                item = item.replace("<status>", "");
                item = item.replace("</status>", "");
                reply.put("status", item);
              }            
            
            matcher = pattern_content.matcher(response);
            
            while (matcher.find()) {
                Log.d("REST", "Start index: " + matcher.start());
                Log.d("REST", "End index " + matcher.end());
                Log.d("REST", "group " + matcher.group());
                String item = matcher.group();
                item = item.replace("<content>", "");
                item = item.replace("</content>", "");                
                reply.put("content", item);
              }              
        } catch (Exception e) {
            reply.put("status", "fail");
            reply.put("content", "Could not parse response");
        }
        
        return reply;
    }
    
    private String fail_xml_msg(String content){
        String msg = "<xml? version=\"1.0\" encoding=\"utf-8\" ?>";
        msg += "<response>";
        msg += "<status>fail</status>";
        msg += "<content>" +content +"</content>";
        msg += "</response>";
        return msg;
    }   
}