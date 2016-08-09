package si.fri.mag.gasperin.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
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
  private static int portCEP;
  private static String context;
  private static boolean autoSubscribe;
  
  private static String om2mUsername;
  private static String om2mPassword;
  private static String om2mIp;
  private static String om2mPathToProduct;
  private static String om2mDeviceName;
  private static String om2mContainerName;
  private static String om2mSubContainer;
  
  private static boolean postToGcmBool;
  private static String GCM_API_KEY;
  private static String GCM_DEVICE_ID;
  private static String GCM_TITLE;

  public static void main(String[] args) throws Exception
  {
    System.out.println("Starting server..");
    
    Properties prop = new Properties();
    InputStream input = null;
    
    try{
	    input = new FileInputStream("config.properties");
	   
	    prop.load(input);
	    portCEP = Integer.parseInt(prop.getProperty("MONITOR_PORT"));
	    GCM_API_KEY = prop.getProperty("GCM_API_KEY");
	    GCM_DEVICE_ID = prop.getProperty("GCM_DEVICE_ID");
	    context = prop.getProperty("MONITOR_CONTEXT");
	    om2mIp = prop.getProperty("OM2M_IP");
	    postToGcmBool = Boolean.parseBoolean(prop.getProperty("POST_TO_GCM"));
	    GCM_TITLE = prop.getProperty("GCM_TITLE");
	    om2mPathToProduct = prop.getProperty("OM2M_PATH_TO_PRODUCT");
	    om2mDeviceName = prop.getProperty("OM2M_DEVICE_NAME"); 
	    om2mContainerName = prop.getProperty("OM2M_CONTAINER_NAME"); 
	    autoSubscribe = Boolean.parseBoolean(prop.getProperty("MONITOR_AUTO_SUBSCRIBE"));
	    om2mUsername = prop.getProperty("OM2M_USERNAME");
	    om2mPassword = prop.getProperty("OM2M_PASSWORD");
	    om2mSubContainer = prop.getProperty("OM2M_SUB_CONTAINER");
	    
	    
	    HttpServer serverCEP = HttpServer.create(new InetSocketAddress(portCEP), 0);
	    
	    serverCEP.createContext(context, new MyHandler());
	    serverCEP.start();
	    
	    System.out.println("The server is now listening on port: " + portCEP + ", context: " + context + "\n");
	    
	    if(autoSubscribe){
	    	subscribeToOm2mContainer();	    
	    }
	    
    } catch (Exception ex) {
		ex.printStackTrace();
		
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
  }
  
  public static void subscribeToOm2mContainer(){
  	//SUBSCRIBE
	    String rawData = "<m2m:sub xmlns:m2m='http://www.onem2m.org/xml/protocols'>" +
						    "<nu>http://" + om2mIp + ":" + portCEP + context + "</nu>"+
						    "<nct>2</nct>" +
						 "</m2m:sub>";
	    
	    HttpURLConnection conn = null;
	    
	    try {
	    	URL url = new URL("http://" + om2mIp + ":8080/~"+om2mPathToProduct+"/"+om2mDeviceName+"/"+om2mContainerName);
		    conn = (HttpURLConnection) url.openConnection();
	    	
	        conn.setReadTimeout(10000);
	        conn.setConnectTimeout(15000);
	        conn.setRequestMethod("POST");
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setRequestProperty("X-M2M-Origin", om2mUsername + ":" + om2mPassword);
	        conn.setRequestProperty("X-M2M-NM", om2mSubContainer);
	        conn.setRequestProperty("Content-Type", "application/xml;ty=23");
	        String body = rawData;
	        OutputStream output = new BufferedOutputStream(conn.getOutputStream());
	        output.write(body.getBytes());
	        output.flush();
	        
	        int responseCode = conn.getResponseCode();
	        String responseContent = conn.getContent().toString();
	        	        
	        if(responseCode == 201){
	        	System.out.println("Monitor is subscribed to " + "http://" + om2mIp + ":8080/~"+om2mPathToProduct+"/"+om2mDeviceName+"/"+om2mContainerName);
	        }
	        
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	    } finally {
	    	if(conn != null){
	    		conn.disconnect();
	    	}
	    }
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
		        
		        if(postToGcmBool){
		        	postToGCM(GCM_TITLE, value);
		        }
		      
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