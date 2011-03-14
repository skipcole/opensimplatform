package org.usip.osp.sharing;

import java.util.ArrayList;
import java.util.Date;

import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.communications.Tips;

import com.thoughtworks.xstream.XStream;

/** This object encapsulates the information kept in an 'experience export.'
*/
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 */
public class ExperienceExportObject {

	
	private String exportersEmail = "";
	private String exportersName = "";
	private String dbSchema = "";
	private Date exportDate = new Date();
	private String exportNotes = "";
	
	private ArrayList <RunningSimulation> setOfRunningSimIdObjects = new ArrayList<RunningSimulation>();
	private ArrayList <Tips> setOfTips = new ArrayList<Tips>();
	
	
	
	public static void main(String args[]){
		System.out.println("Hello World");
		
		XStream xstream = new XStream();
		
		ExperienceExportObject eeo = new ExperienceExportObject();
		
		
		System.out.println(xstream.toXML(eeo));
		
	}

}
