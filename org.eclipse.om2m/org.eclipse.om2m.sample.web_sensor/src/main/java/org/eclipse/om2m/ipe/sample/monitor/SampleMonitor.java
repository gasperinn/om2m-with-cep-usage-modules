package org.eclipse.om2m.ipe.sample.monitor;

import org.eclipse.om2m.ipe.sample.controller.SampleController;

/**
 * This class is simply to show the architecture and to reflect the action
 * from the real devices. Here we simulate the reception of the switch signal.
 *
 */
public class SampleMonitor {
		
	public static void sendSensorData(String systolic, String diastolic, String x, String y, String z){
		SampleController.sendSensorData(systolic, diastolic, x, y,z);
	}

}
