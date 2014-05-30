package de.i3mainz.springframework.swe.n52.sos.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author sos_sn
 * try and execute Http Connections
 *
 */
public abstract class Http_connect {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(Http_connect.class);
    
	/**
	 * Erstellung einer Verbindung zu einem Webservice,
	 * Anfrage übergabe,
	 * Rückgabe einer Serviceantwort(Webservice) 
	 * @param targetURL
	 * @param urlParameters
	 * @return Http-response
	 */
	public static String excutePost(String targetURL, String urlParameters){
	URL url;
    HttpURLConnection connection = null;     
    
    try {
    	
    	//Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");				
	      
	      //connection.setRequestProperty("Content-Length", "" String.valueOf(urlParameters.length));
	      connection.setRequestProperty("Content-Language", "en-US");  			
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);
	
	      //Send request
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();
	
	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();
	
	    } catch (Exception e) {
	
	      LOG.error("Error in HTTP-Connection to SOS service!",e);
	      return null;
	
	    } finally {
	
	      if(connection != null) {connection.disconnect();}
	    }
	}
}
