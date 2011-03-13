package org.usip.osp.sharing;

import java.util.ArrayList;
import java.util.Date;

import org.usip.osp.communications.Tips;

import com.thoughtworks.xstream.XStream;

public class ExperienceExportObject {

	RunningSimIdentityObject rsio = new RunningSimIdentityObject();
	
	private String exportersEmail = "";
	private String exportersName = "";
	private String dbSchema = "";
	private Date exportDate = new Date();
	private String exportNotes = "";
	
	private ArrayList <RunningSimIdentityObject> setOfRunningSimIdObjects = new ArrayList<RunningSimIdentityObject>();
	private ArrayList <Tips> setOfTips = new ArrayList<Tips>();
	
	
	
	public static void main(String args[]){
		System.out.println("Hello World");
		
		XStream xstream = new XStream();
		
		ExperienceExportObject eeo = new ExperienceExportObject();
		eeo.rsio.setSimulationName("test");
		
		
		System.out.println(xstream.toXML(eeo));
		
	}

}
