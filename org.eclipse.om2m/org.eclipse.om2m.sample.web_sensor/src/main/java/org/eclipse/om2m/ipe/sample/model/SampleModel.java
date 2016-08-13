package org.eclipse.om2m.ipe.sample.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.om2m.commons.exceptions.BadRequestException;

public class SampleModel {
		
	private static Sensor sensor = new Sensor(0,0,0,0,0);
	
	private SampleModel(){
	}
	
	
	public static void setSensorState(String systolic, String diastolic, String x, String y, String z) {
		sensor.setSystolic(Double.parseDouble(systolic));
		sensor.setDiastolic(Double.parseDouble(diastolic));
		sensor.setX(Double.parseDouble(x));
		sensor.setY(Double.parseDouble(y));
		sensor.setZ(Double.parseDouble(z));
	}
	
	
	public static double getSensorSystolic() {
		return sensor.getSystolic();
	}
	
	public static double getSensorDiastolic() {
		return sensor.getDiastolic();
	}
	
	public static double getSensorX() {
		return sensor.getX();
	}
	
	public static double getSensorY() {
		return sensor.getY();
	}
	
	public static double getSensorZ() {
		return sensor.getZ();
	}
	
	
}
