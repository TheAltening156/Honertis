package fr.honertis.utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.honertis.Honertis;

public class WebUtils implements MC{
	public 	static String i = "";
	private static String agent1 = "User-Agent";
    private static String agent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";

	
	public static void browseWebsite(String url) {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String currentVersion = Honertis.INSTANCE.version;
	
	public static boolean update() {
	    BufferedReader in = null;
		try {
		    List<String> lines = new ArrayList<>();
		    String link = "https://raw.githubusercontent.com/TheAltening156/idk/main/hver.txt";
		    URL url = new URL(link);
		    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		    connection.addRequestProperty(agent1, agent2);
		    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    	for (String line = in.readLine(); line != null; line = in.readLine()) 
	    	    lines.add(line);
		    for (String s : lines)
		    	i += s; 
		    currentVersion = i;
		    if ( i.contains(Honertis.INSTANCE.version) )  {
		    	return false;
		    } else {
		    	return true;
		    }
		} catch (Exception e) {
	    	System.out.println(e);
	    } finally {
	    	if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	    }
		return false;
	}
	
	public static String visitSite(String urly) {
	    ArrayList<String> lines = new ArrayList<>();
	    String stuff = "";
	    try {
	      URL url = new URL(urly);
	      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	      connection.addRequestProperty(agent1, agent2);
	      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
	      String line;
	      while ((line = in.readLine()) != null)
	        lines.add(line); 
	    } catch (Exception line) {}
	    for (String s : lines)
	      stuff = String.valueOf(stuff) + s; 
	    return stuff;
		  
	}
	
}
