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
		    String agent1 = "User-Agent";
		    String agent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
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
	
}
