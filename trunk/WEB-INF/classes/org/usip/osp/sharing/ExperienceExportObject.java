package org.usip.osp.sharing;

import java.util.ArrayList;

import org.usip.osp.communications.Tips;

import com.thoughtworks.xstream.XStream;

public class ExperienceExportObject {

	RunningSimIdentityObject rsio = new RunningSimIdentityObject();
	
	public ArrayList tings = new ArrayList();
	
	
	public static void main(String args[]){
		System.out.println("Hello World");
		
		XStream xstream = new XStream();
		
		ExperienceExportObject eeo = new ExperienceExportObject();
		eeo.rsio.setSimulationName("test");
		
		Tips tip = new Tips();
		
		tip.setTipText("Dont take and wooden nickels");
		eeo.tings.add(tip);
		
		System.out.println(xstream.toXML(eeo));
		
	}

}
