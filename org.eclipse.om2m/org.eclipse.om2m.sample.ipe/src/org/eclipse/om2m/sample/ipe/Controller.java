package org.eclipse.om2m.sample.ipe;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.interworking.service.InterworkingService;

public class Controller implements InterworkingService{

	@Override
	public ResponsePrimitive doExecute(RequestPrimitive request) {
		String[] parts = request.getTo().split("/");
		String appId = parts[3];
		ResponsePrimitive response = new ResponsePrimitive(request);
		
		if(request.getQueryStrings().containsKey("op")){
			String valueOp = request.getQueryStrings().get("op").get(0);
			
			switch(valueOp){
			case "get":
				if(appId.equals(Monitor.sensorId)){
					response.setContent(ObixUtil.getSensorDataRep(Monitor.sensorValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else if (appId.equals(Monitor.actuatorId)){
					response.setContent(ObixUtil.getActuatorDataRep(Monitor.actuatorValue));
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			case "true": case "false":
				if(appId.equals(Monitor.actuatorId)){
					Monitor.actuatorValue = Boolean.parseBoolean(valueOp);
					response.setResponseStatusCode(ResponseStatusCode.OK);
				} else {
					response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
				}
				return response;
			default:
				response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
			}
		} else {
			response.setResponseStatusCode(ResponseStatusCode.BAD_REQUEST);
		}
		return response;
	}

	@Override
	public String getAPOCPath() {
		return Monitor.ipeId;
	}
	
}
