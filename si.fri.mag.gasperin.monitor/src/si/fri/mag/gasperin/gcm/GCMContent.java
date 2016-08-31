package si.fri.mag.gasperin.gcm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GCMContent implements Serializable {

	public String to;
	public Map<String,String> data;

	public void addTopic(String topic){
	    to = topic;
	}

	public void createData(String title, String message){
	    if(data == null)
	        data = new HashMap<String,String>();

	    data.put("title", title);
	    data.put("message", message);
	}
}