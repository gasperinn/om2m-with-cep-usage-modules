package org.eclipse.om2m.ipe.sample.util;

import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.ShortName;
import org.eclipse.om2m.commons.obix.Bool;
import org.eclipse.om2m.commons.obix.Contract;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.Op;
import org.eclipse.om2m.commons.obix.Str;
import org.eclipse.om2m.commons.obix.Uri;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
import org.eclipse.om2m.ipe.sample.constants.Operations;
import org.eclipse.om2m.ipe.sample.constants.SampleConstants;
import org.eclipse.om2m.ipe.sample.model.SampleModel;

public class ObixUtil {
	
	
	public static String getSensorStateRep() {
		// oBIX
		Obj obj = new Obj();
		obj.add(new Str("systolic", SampleModel.getSensorSystolic()+""));
		obj.add(new Str("diastolic", SampleModel.getSensorDiastolic()+""));
		obj.add(new Str("x",SampleModel.getSensorX()+""));
		obj.add(new Str("y",SampleModel.getSensorY()+""));
		obj.add(new Str("z",SampleModel.getSensorZ()+""));
		return ObixEncoder.toString(obj);
	}
	
	public static String getStateSensorRep(String systolic, String diastolic, String x, String y, String z) {
		// oBIX
		Obj obj = new Obj();
		obj.add(new Str("systolic", systolic));
		obj.add(new Str("diastolic", diastolic));
		obj.add(new Str("x", x));
		obj.add(new Str("y", y));
		obj.add(new Str("z", z));
		return ObixEncoder.toString(obj);
	}

	
	public static String getSensorDescriptorRep(String appId, String ipeId) {
		String prefix = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + appId;
		Obj obj = new Obj();

		Op opGet = new Op();
		opGet.setName("GET");
		opGet.setHref(new Uri(prefix + "/DATA/la"));
		opGet.setIs(new Contract("retrieve"));
		obj.add(opGet);

		Op opGetDirect = new Op();
		opGetDirect.setName("GET(Direct)");
		opGetDirect.setHref(new Uri(prefix + "?op=getSensorData"));
		opGetDirect.setIs(new Contract("execute"));
		obj.add(opGetDirect);

		return ObixEncoder.toString(obj);
	}
	
}
