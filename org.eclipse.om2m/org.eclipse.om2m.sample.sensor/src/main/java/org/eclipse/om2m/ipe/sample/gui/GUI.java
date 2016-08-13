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
import java.util.Map;
import java.util.Timer;

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
import org.eclipse.om2m.commons.constants.Constants;
import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.ipe.sample.RequestSender;
import org.eclipse.om2m.ipe.sample.constants.SampleConstants;
import org.eclipse.om2m.ipe.sample.controller.LifeCycleManager;
import org.eclipse.om2m.ipe.sample.model.SampleModel;
import org.eclipse.om2m.ipe.sample.model.Sensor;
import org.eclipse.om2m.ipe.sample.monitor.SampleMonitor;
import org.eclipse.om2m.ipe.sample.util.ObixUtil;
import org.osgi.framework.FrameworkUtil;

/**
 * The Graphical User Interface of the IPE sample.
 */
public class GUI extends JFrame {
    /** Logger */
    static Log LOGGER = LogFactory.getLog(GUI.class);
    /** Serial Version UID */
    private static final long serialVersionUID = 1L;
    /** GUI Content Panel */
    private JPanel contentPanel;
    /** GUI Frame */
    static GUI frame;
    
    static JLabel sensorTitleLabel = new JLabel("Sensor simulator");
    static JLabel bloodPressureLabel = new JLabel("Blood pressure");
    static JLabel accelerometerLabel = new JLabel("Accelerometer");
    static JButton sendDataButton = new JButton("Send");
    
    static JLabel sensorSystolicLabel = new JLabel("Systolic (top): ");
    static JLabel sensorDiastolicLabel = new JLabel("Diastolic (bottom): ");
    static JLabel sensorXLabel = new JLabel("X axis: ");
    static JLabel sensorYLabel = new JLabel("Y axis: ");
    static JLabel sensorZLabel = new JLabel("Z axis: ");
    
    static JTextField sensorSystolicTextField = new JTextField("105.0");
    static JTextField sensorDiastolicTextField = new JTextField("70.0");
    static JTextField sensorXTextField = new JTextField("0.1");
    static JTextField sensorYTextField = new JTextField("0.1");
    static JTextField sensorZTextField = new JTextField("0.1");
    
    /**
     * Initiate The GUI.
     */
    public static void init() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new GUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    LOGGER.error("GUI init Error", e);
                }
            }
        });
    }

    /**
     * Stop the GUI.
     */
    public static void stop() {
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Creates the frame.
     */
    public GUI() {
        setLocationByPlatform(true);
        setVisible(false);
        setResizable(false);
        setTitle("Sample Simulated IPE");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-500)/2, (screenSize.height-570)/2, 345, 320);

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);
        
        //SENSOR
        JPanel panel_sensor = new JPanel();
        panel_sensor.setBounds(10, 5, 320, 280);
        contentPanel.add(panel_sensor);
        panel_sensor.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        panel_sensor.setLayout(null);
        
        //SENSOR SIMULATOR
        sensorTitleLabel.setFont(new Font(sensorTitleLabel.getFont().getName(), Font.PLAIN, 16));
        sensorTitleLabel.setBounds(10, 10, 200, 30);
        Font font = sensorTitleLabel.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        sensorTitleLabel.setFont(font.deriveFont(attributes));
        panel_sensor.add(sensorTitleLabel);
        
        //BLOOD PRESSURE
        bloodPressureLabel.setFont(new Font(bloodPressureLabel.getFont().getName(), Font.PLAIN, 14));
        bloodPressureLabel.setBounds(10, 50, 200, 25);
        panel_sensor.add(bloodPressureLabel);
        
        //SYSTOLIC
        sensorSystolicLabel.setBounds(10, 80, 120, 20);
        sensorSystolicTextField.setBounds(140, 80, 170, 20);
        panel_sensor.add(sensorSystolicLabel);
        panel_sensor.add(sensorSystolicTextField);
        
        //DIASTOLIC1
        sensorDiastolicLabel.setBounds(10, 100, 120, 20);
        sensorDiastolicTextField.setBounds(140, 100, 170, 20);
        panel_sensor.add(sensorDiastolicLabel);
        panel_sensor.add(sensorDiastolicTextField);
        
        //ACCELEROMETER
        accelerometerLabel.setFont(new Font(accelerometerLabel.getFont().getName(), Font.PLAIN, 14));
        accelerometerLabel.setBounds(10, 130, 200, 25);
        panel_sensor.add(accelerometerLabel);
        
        //X
        sensorXLabel.setBounds(10, 160, 120, 20);
        sensorXTextField.setBounds(70, 160, 240, 20);
        panel_sensor.add(sensorXLabel);
        panel_sensor.add(sensorXTextField);
        
        //Y
        sensorYLabel.setBounds(10, 180, 120, 20);
        sensorYTextField.setBounds(70, 180, 240, 20);
        panel_sensor.add(sensorYLabel);
        panel_sensor.add(sensorYTextField);

        //Z
        sensorZLabel.setBounds(10, 200, 120, 20);
        sensorZTextField.setBounds(70, 200, 240, 20);
        panel_sensor.add(sensorZLabel);
        panel_sensor.add(sensorZTextField);
        
        //SEND
        sendDataButton.setBounds(240, 230, 70, 30);
        panel_sensor.add(sendDataButton);
        
        sendDataButton.addActionListener(new java.awt.event.ActionListener() {
            // Switch Button Clicked
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Change all lamps states
                new Thread(){
                    public void run(){
                    	if(LifeCycleManager.systemEvalvationEnabled){
                        	systemEvalvationStart();
                        }else{
	                        // Send switch all request to create a content with the current State
	                    	SampleMonitor.sendSensorData(sensorSystolicTextField.getText(), 
	                    								 sensorDiastolicTextField.getText(),
	                    								 sensorXTextField.getText(), 
	                    								 sensorYTextField.getText(),
	                    								 sensorZTextField.getText());
	                    	                    	
	                    	Sensor sensor = new Sensor(Double.parseDouble(sensorSystolicTextField.getText()),
	                    								Double.parseDouble(sensorDiastolicTextField.getText()),
						                    			Double.parseDouble(sensorXTextField.getText()),
						                    			Double.parseDouble(sensorYTextField.getText()),
						                    			Double.parseDouble(sensorZTextField.getText()));
	                    	                    	
	                    	LifeCycleManager.cepServer.sendEvent(sensor, "SENSOR");
                        }
                    	
                    }
                }.start();
            }
        }); 
        
        if(LifeCycleManager.systemEvalvationEnabled){
        	systemEvalvationInit();
        }
        
        
    }
    
    private void systemEvalvationInit(){
    	
    	//CREATE SENSORS
    	for(int i=0; i<19; i++){
    		createSensorResource("SENSOR" + i);
    	}
    	
    	
    	createSensorResource("SENSOR"); 
    	
    	/*
    	//CREATE CEP RULES
    	for(int i=0; i<49; i++){
    		LifeCycleManager.cepServer.addCepRule("SENSOR", "CEP_DATA"+i, 
 	 			   "select systolic "
 	 			 + "from Sensor.win:length(1) "
 	 			 + "having systolic > " +i);
    	}
    	
    	
    	LifeCycleManager.cepServer.addCepRule("SENSOR", "CEP_DATA", 
	 			   "select systolic "
	 			 + "from Sensor.win:length(1) "
	 			 + "having systolic > 0 ");
    	
    	
    	//START CEP SERVER
    	LifeCycleManager.cepServer.start();
    	*/
    }
        
    
    private void systemEvalvationStart(){
    	
    	int delay = 104;
    	
    	while(delay >= 0){
    		
	    		try {
		        			        	
		    		Writer output;
		        	output = new BufferedWriter(new FileWriter("D:\\backup\\mag\\results.txt", true));
					output.append("DELAY: "+ delay +"\n");
					output.close();
		        	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	
	    		sendData(50, delay);
		    	
		    	delay -= 2;
		    	
		    	try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	}
    	
    	
    	    	
    	
    }
    
    private void sendData(int n, int delay){
    	//SEND DATA
    	for(int i=0; i<n; i++){
    		
	    	new Thread(){
	            public void run(){
	            	
	            	long time = System.currentTimeMillis();   
	            	
        			SampleMonitor.sendSensorData(time+"", "0.00", "0.0", "0.0", "0.0");
                	
	            	Sensor sensor = new Sensor(time, 0.00, 0.0, 0.0, 0.0);
	            	
	            	//LifeCycleManager.cepServer.sendEvent(sensor, "SENSOR");
	        			
	            }
	        }.start();
	        
	        try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    private static void createSensorResource(String sensorName){
		String targetId, content;
		
		targetId = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME;
		AE ae = new AE();
		ae.setRequestReachability(true);
		ae.setAppID(sensorName);
		ae.getPointOfAccess().add(SampleConstants.POA);
		ResponsePrimitive response = RequestSender.createAE(ae, sensorName);
		
		if(response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)){
			
			targetId = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + sensorName;
			Container cnt = new Container();
			cnt.setMaxNrOfInstances(BigInteger.valueOf(10));
			// Create the DESCRIPTOR container
			RequestSender.createContainer(targetId, "DESCRIPTOR", cnt);
			
			// Create the DATA container
			RequestSender.createContainer(targetId, "DATA", cnt);
			
			// Create the description contentInstance
			content = ObixUtil.getSensorDescriptorRep(sensorName, SampleConstants.POA);
			targetId = "/" + Constants.CSE_ID + "/" + Constants.CSE_NAME + "/" + sensorName + "/" + "DESCRIPTOR";
			ContentInstance cin = new ContentInstance();
			cin.setContent(content);
			cin.setContentInfo(MimeMediaType.OBIX);
			RequestSender.createContentInstance(targetId, cin);
			
			//LifeCycleManager.cepServer.insertDevice(sensorName);
			
		}
	}

}
