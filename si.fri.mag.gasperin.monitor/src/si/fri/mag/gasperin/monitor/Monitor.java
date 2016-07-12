package si.fri.mag.gasperin.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import si.fri.mag.gasperin.gcm.GCMContent;

public class Monitor
{
  private static int portCEP = 1400;
  private static String context = "/monitor";
  
  final static String GCM_API_KEY = "***";
  final static String GCM_DEVICE_ID="***";

  public static void main(String[] args) throws Exception
  {
    System.out.println("Starting server..");
    HttpServer serverCEP = HttpServer.create(new InetSocketAddress(portCEP), 0);
    
    serverCEP.createContext(context, new MyHandler());
    serverCEP.start();
    
    System.out.println("The server is now listening on\nPort: " + portCEP + "\nContext: " + context + "\n");
  }

 static class MyHandler implements HttpHandler
  {

    public void handle(HttpExchange t) throws IOException {
      String body = "";
      try
      {
	        InputStream is = t.getRequestBody();
	        int i;
	        while ((i = is.read()) != -1)
	        {
	          char c = (char)i;
	          body = body + c;
	        }
	        
	        body = StringEscapeUtils.unescapeHtml4(body);
		        
		    if(body.indexOf("<obj>") != -1){
		    	
		        String str = body.substring(body.indexOf("<obj>")+5, body.indexOf("</obj>")).trim();
		        //System.out.println(str);
		        
		        String value = str.substring(str.indexOf("val=\"")+5, str.indexOf("\" "));
		        		        
		        System.out.println(value);
		        
		        //postToGCM("Alert", value);
		      
    	 }
     
        t.sendResponseHeaders(204, -1L);
        
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      
    }

     public static void postToGCM(String title, String message){

     	try{
     		
     	   GCMContent content = new GCMContent();
     		
     	   content.addRegId(GCM_DEVICE_ID);       
     	   content.createData(title, message);
     	   
    		   URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
    		   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		   
    		   conn.setRequestMethod("POST");
    		   conn.setRequestProperty("Content-Type", "application/json");
    		   conn.setRequestProperty("Authorization", "key="+GCM_API_KEY);
    		   
    		   conn.setDoOutput(true);
    		   ObjectMapper mapper = new ObjectMapper();
    		   DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
    		   mapper.writeValue(wr, content);
    		   
    		   wr.flush();
    		   wr.close();
    		   
    		   int response = conn.getResponseCode();
//    		   System.out.println(response);
    		   
    		}catch(Exception e){
    			e.printStackTrace();
    		}
     }
  }
 
}