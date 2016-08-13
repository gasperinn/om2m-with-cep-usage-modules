package org.eclipse.om2m.ipe.sample.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.ipe.sample.RequestSender;
import org.eclipse.om2m.ipe.sample.constants.SampleConstants;
import org.eclipse.om2m.ipe.sample.controller.LifeCycleManager;
import org.eclipse.om2m.ipe.sample.model.SampleModel;
import org.eclipse.om2m.ipe.sample.model.Sensor;
import org.eclipse.om2m.ipe.sample.monitor.SampleMonitor;
import org.eclipse.om2m.ipe.sample.util.ObixUtil;
import org.osgi.framework.FrameworkUtil;

import si.fri.mag.gasperin.cep.h2.CepRule;
import si.fri.mag.gasperin.cep.h2.Device;
import si.fri.mag.gasperin.cep.utils.CEP;

/**
 * The Graphical User Interface of the IPE sample.
 */
public class Web_GUI extends Thread {
	private static Server server;
	
	//use same credentials as for webpage
	private String userName = null;
	private String pass = null;
	
	private String sensorLogoImage = "data:image/png;base64,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";
	
	@Override
	public void run() {
		
		//setting credentials for login CEP
		try {
			Path iniPath = Paths.get("configuration", "config.ini");
		    Charset charset = Charset.forName("ISO-8859-1");
		    
			List<String> lines = Files.readAllLines(iniPath, charset);
			
			for (String line : lines) {
				if(line.contains("org.eclipse.om2m.adminRequestingEntity=")){
					String userPass = line.substring("org.eclipse.om2m.adminRequestingEntity=".length());
					userName = userPass.split("\\\\:")[0];
					pass = userPass.split("\\\\:")[1];
					System.out.println("[CEP INFO]: Username and password for Web sensor server has been set");
					//LOGGER.info("Username and password for CEP http server has been set");
				}
		      }
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		server = new Server(8082);
        ServletContextHandler context = new ServletContextHandler(server, "/sensor", ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
	
      //LOGIN PAGE
        context.addServlet(new ServletHolder(new DefaultServlet() {
          protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        	          	  
        	  response.getWriter().append(
            		"<html><div id='login'><center><br>" +
    	            "<img src='"+sensorLogoImage+"' height='90'><br><br>"+
    	            "<form method='POST' action='/sensor/j_security_check'>"+
    	            "<table><tbody>"+
    	            "<tr><td>username: </td><td><input type='text' name='j_username'></td></tr>"+
    	            "<tr><td>password</td><td><input type='password' name='j_password'></td></tr>"+
    	            "<tr><td></td><td><input style='float: right;' type='submit' value='Login'/></td></tr>"+
    	            "</tbody></table>"+
    	            "</form>"+
    	            "</center></div></html>");
            
            }
        }), "/login");
        
        //SHOW SENSOR FORM (HTTP GET)
        context.addServlet(new ServletHolder(new DefaultServlet() {
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            	
              String systolic = "105.0";
              String diastolic = "70.0";
              String x = "0.1";
              String y = "0.1";
              String z = "0.1";
            	
              if (request.getParameterMap().containsKey("systolic")) {
            	  systolic = request.getParameter("systolic");
              }
              if (request.getParameterMap().containsKey("diastolic")) {
            	  diastolic = request.getParameter("diastolic");
              }
              if (request.getParameterMap().containsKey("x")) {
            	  x = request.getParameter("x");
              }
              if (request.getParameterMap().containsKey("y")) {
            	  y = request.getParameter("y");
              }
              if (request.getParameterMap().containsKey("z")) {
            	  z = request.getParameter("z");
              }
            	
          	  response.getWriter().append("<html><div style='text-align: center;'><h2>SENSOR SIMULATOR</h2></div>");
          	  
  				response.getWriter().append("<form method='POST' action='/sensor/send' style='text-align: center;'>"+
  							"<fieldset style='width: 100px; margin: 0 auto; text-align: left; padding:10px; border: 1px solid;'>"+
  							"Systolic (top): <br> <input type='text' name='systolic' value='"+systolic+"'><br><br>"+
  							"Diastolic (bottom): <br> <input type='text' name='diastolic' value='"+diastolic+"'><br><br>"+
  							"X axis: <br> <input type='text' name='x' value='"+x+"'><br><br>"+
  							"Y axis: <br> <input type='text' name='y' value='"+y+"'><br><br>"+
  							"Z axis: <br> <input type='text' name='z' value='"+z+"'><br><br>"+
							"<input type='submit' name='action' value='Send'/>"+
							"</fieldset></form>");
          	  
          	  response.getWriter().append("</html>");
          	  
  	  			
            }
          }), "/*");
        
        //SEND SENSOR DATA (HTTP POST)
        context.addServlet(new ServletHolder(new DefaultServlet() {
            protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            	
            	// Send switch all request to create a content with the current State
            	SampleMonitor.sendSensorData(
            			request.getParameter("systolic"), 
            			request.getParameter("diastolic"),
            			request.getParameter("x"),
            			request.getParameter("y"),
            			request.getParameter("z")
            	);
            	                    	
            	Sensor sensor = new Sensor(
            			Double.parseDouble(request.getParameter("systolic")), 
    					Double.parseDouble(request.getParameter("diastolic")),
						Double.parseDouble(request.getParameter("x")),
						Double.parseDouble(request.getParameter("y")),
						Double.parseDouble(request.getParameter("z"))
            	);
            	                    	
            	LifeCycleManager.cepServer.sendEvent(sensor, "SENSOR");
            	            	    			
            	response.sendRedirect("/sensor?systolic="+request.getParameter("systolic")+
            								 "&diastolic="+request.getParameter("diastolic")+
            								 "&x="+request.getParameter("x")+
            								 "&y="+request.getParameter("y")+
            								 "&z="+request.getParameter("z"));
            	
            }
          }), "/send");
        


        
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__FORM_AUTH);
        constraint.setRoles(new String[]{"user"}); //String[]{"user","admin","moderator"}
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.addConstraintMapping(constraintMapping);
        HashLoginService loginService = new HashLoginService();
        loginService.putUser(userName, new Password(pass), new String[] {"user"});
        securityHandler.setLoginService(loginService);

        FormAuthenticator authenticator = new FormAuthenticator("/login", "/login", false);
        securityHandler.setAuthenticator(authenticator);

        context.setSecurityHandler(securityHandler);
        
        try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public void stopThread(){
		try {
			//H2DBTableCepRules table = new H2DBTableCepRules();
        	//table.removeAll();        	
        	//table.close();
        	
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
