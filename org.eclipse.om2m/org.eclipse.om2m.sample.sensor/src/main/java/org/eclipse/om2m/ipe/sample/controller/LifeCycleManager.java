package org.eclipse.om2m.ipe.sample.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.eclipse.om2m.ipe.sample.gui.GUI;
import org.eclipse.om2m.ipe.sample.model.Sensor;
import org.eclipse.om2m.ipe.sample.util.ObixUtil;

import si.fri.mag.gasperin.cep.CepHttpServlet;

public class LifeCycleManager {

	private static Log LOGGER = LogFactory.getLog(LifeCycleManager.class); 
	public static CepHttpServlet cepServer;
	
	public static boolean systemEvalvationEnabled = false; 
	
	/**
	 * Handle the start of the plugin with the resource representation and the GUI
	 */
	public static void start(CseService cseService){
		
		cepServer = new CepHttpServlet(cseService, Sensor.class);
		cepServer.setDebug(true);
		
		if(!systemEvalvationEnabled){
		
			createSensorResources();	
			
			//http://www.lek.si/si/skrb-za-zdravje/bolezni-in-simptomi/srce-ozilje/zvisan-krvni-tlak-hipertenzija/#02
			//optimalni krvni tlak	pod 120	pod 80
			//normalni krvni tlak	120-129	80-84
			//visoko normalen krvni tlak	130 -139	85 -89
			
			//arterijska hipertenzija	>140	>90
	
			/*
			cepServer.addCepRule("SENSOR", 
					 			 "CEP_DATA", 
					 			 "select avg(systolic) AS systolic, "
					 			 + "avg(diastolic) AS diastolic, "
					 			 + "avg(x) AS x, "
					 			 + "avg(y) AS y, "
					 			 + "avg(z) AS z "
					 			 + "from Sensor.win:length(4) "
					 			 + "having (avg(systolic) > 140 AND avg(diastolic) > 90) "
					 			 + "AND (avg(x) < 0.5 AND avg(y) < 0.5 AND avg(z) < 0.5 ) ");
			*/
		}
		
		cepServer.start();
		
		// Start the GUI
		if(SampleConstants.GUI){
			GUI.init();
		}
		
	}

	/**
	 * Stop the GUI if it is present
	 */
	public static void stop(){
		if(SampleConstants.GUI){
			GUI.stop();
		}
		
		cepServer.stopThread();
	}

	private static void createSensorResources(){
		String targetId, content;
		
		targetId = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME;
		AE ae = new AE();
		ae.setRequestReachability(true);
		ae.setAppID("SENSOR");
		ae.getPointOfAccess().add(SampleConstants.POA);
		ResponsePrimitive response = RequestSender.createAE(ae, "SENSOR");
		
		if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
			
			targetId = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + "SENSOR";
			Container cnt = new Container();
			cnt.setMaxNrOfInstances(BigInteger.valueOf(10));
			// Create the DESCRIPTOR container
			RequestSender.createContainer(targetId, "DESCRIPTOR", cnt);
			
			// Create the DATA container
			RequestSender.createContainer(targetId, "DATA", cnt);
			
			// Create the description contentInstance
			content = ObixUtil.getSensorDescriptorRep("SENSOR", SampleConstants.POA);
			targetId = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + "SENSOR" + "/" + "DESCRIPTOR";
			ContentInstance cin = new ContentInstance();
			cin.setContent(content);
			cin.setContentInfo(MimeMediaType.OBIX);
			RequestSender.createContentInstance(targetId, cin);
			
			cepServer.insertDevice("SENSOR");
			
		}
	}

}
