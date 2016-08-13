package org.eclipse.om2m.sample.ipe;

import si.fri.mag.gasperin.cep.utils.DataInterface;

public class Data implements DataInterface{
	double value;

    public Data(double value) {
    	this.value = value;
    }
    
    public double getValue() {
    	return value;
    }
    
    @Override
    public String toString() {
        return "Value: " + value;
    }
}
