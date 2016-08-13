package org.eclipse.om2m.ipe.sample.model;

import si.fri.mag.gasperin.cep.utils.DataInterface;

public class Sensor implements DataInterface {

    double systolic; //sistolièni krvni tlak
    double diastolic; //diastolièni krvni tlak
    double x;
    double y;
    double z;
    
    public Sensor(double systolic, double diastolic, double x, double y, double z){
    	this.systolic = systolic;
    	this.diastolic = diastolic;
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }

	public double getSystolic() {
		return systolic;
	}

	public void setSystolic(double systolic) {
		this.systolic = systolic;
	}

	public double getDiastolic() {
		return diastolic;
	}

	public void setDiastolic(double diastolic) {
		this.diastolic = diastolic;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public String toString(){
		return "Systolic (top): " + systolic + 
			   ", Diastolic (bottom): " + diastolic + 
			   ", X: "+ x + 
			   ", Y: " + y +
			   ", Z: "+ z;
	}
	
	
}
