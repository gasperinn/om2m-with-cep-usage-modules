package org.eclipse.om2m.ipe.sample.controller;

import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.core.service.CseService;
import org.eclipse.om2m.ipe.sample.RequestSender;
import org.eclipse.om2m.ipe.sample.constants.SampleConstants;
import org.eclipse.om2m.ipe.sample.model.SampleModel;
import org.eclipse.om2m.ipe.sample.util.ObixUtil;

public class SampleController {
	
	public static CseService CSE;
	protected static String AE_ID;
	
	public static String getFormatedSensorState(){
		return ObixUtil.getSensorStateRep();
	}
	
	public static void sendSensorData(String systolic, String diastolic, String x, String y, String z){
		// Set the value in the "real world" model
		SampleModel.setSensorState(systolic, diastolic, x, y, z);
		// Send the information to the CSE
		String targetID = SampleConstants.CSE_PREFIX + "/" +  SampleConstants.SENSOR + "/" + SampleConstants.DATA;
		ContentInstance cin = new ContentInstance();
		cin.setContent(ObixUtil.getStateSensorRep(systolic, diastolic, x, y, z));
		cin.setContentInfo(MimeMediaType.OBIX + ":" + MimeMediaType.ENCOD_PLAIN);
		RequestSender.createContentInstance(targetID, null, cin);
	}

	public static void setCse(CseService cse){
		CSE = cse;
	}
	
}
