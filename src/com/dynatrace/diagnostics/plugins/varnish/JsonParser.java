 /**
  * This template file was generated by dynaTrace client.
  * The dynaTrace community portal can be found here: http://community.dynatrace.com/
  * For information how to publish a plugin please visit http://community.dynatrace.com/plugins/contribute/
  **/ 
package com.dynatrace.diagnostics.plugins.varnish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JsonParser {
	
	private long Messages = 0;
	private long Messages_Ready = 0;
	private long Messages_Unack = 0;
	private double messRate = 0;
	private double redRate = 0;
	private double unRate = 0;
	
	public JsonParser(String server, String port, final String Username, final String Password) throws IOException, ParseException{
		
		Authenticator.setDefault (new Authenticator() {
    	    protected PasswordAuthentication getPasswordAuthentication() {
    	        return new PasswordAuthentication (Username, Password.toCharArray());
    	    }
    	});
		JSONParser parser = new JSONParser();
		String rabbitURL = "http://" + server + ":" + port + "/api/overview";
        URL rabbitMQ = new URL(rabbitURL);
        BufferedReader in = new BufferedReader(new InputStreamReader(rabbitMQ.openStream()));
        String finalline = "";
        String inputLine = "";
        while ((inputLine = in.readLine()) != null)
        {
                //System.out.println(inputLine);
                finalline += inputLine;
        }
        in.close();
        Object objover = parser.parse(finalline);
        JSONObject theover= (JSONObject) objover;
        Object temp = parser.parse(theover.get("queue_totals").toString());
        JSONObject queueover = (JSONObject) temp;
        temp = parser.parse(queueover.get("messages_details").toString());
        JSONObject messDetail = (JSONObject) temp;
        temp = parser.parse(queueover.get("messages_ready_details").toString());
        JSONObject redDetail = (JSONObject) temp;
        temp = parser.parse(queueover.get("messages_unacknowledged_details").toString());
        JSONObject unDetail = (JSONObject) temp;
        Messages = Long.parseLong(queueover.get("messages").toString());
        messRate = Double.parseDouble(messDetail.get("rate").toString());
        Messages_Ready = Long.parseLong(queueover.get("messages_ready").toString());
        redRate = Double.parseDouble(redDetail.get("rate").toString());
        Messages_Unack = Long.parseLong(queueover.get("messages_unacknowledged").toString());
        unRate = Double.parseDouble(unDetail.get("rate").toString());
	}
	public long getMessages()
	{
		return Messages;
	}
	public long getMessages_Ready()
	{
		return Messages_Ready;
	}
	public long getMessages_Unack()
	{
		return Messages_Unack;
	}
	public double get_messRate()
	{
		return messRate;
	}
	public double get_redRate()
	{
		return redRate;
	}
	public double get_unRate()
	{
		return unRate;
	}
}