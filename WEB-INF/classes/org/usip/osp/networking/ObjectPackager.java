package org.usip.osp.networking;

import java.io.File;
import java.util.List;
import java.util.ListIterator;

import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.BaseSimSection;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.communications.Inject;
import org.usip.osp.communications.InjectGroup;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Packages and unpackages objects to XML using the opensource software library XStream.
 * 
 * @author Ronald "Skip" Cole<br />
 *
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
public class ObjectPackager {
	
	public static final String ObjectDelimiter = "<------------------------------------------>";
    
    public static void main(String[] args) {

    }
    
    public static void play() {
    	BaseSimSection bss_intro = new BaseSimSection("usiposcw", "", "../osp_core", "introduction.jsp", "Introduction",
		"Your introductory text will be shown here.");
    	
    	XStream xstream = new XStream();
    	
    	xstream.alias("bss", BaseSimSection.class);
    	
    	System.out.println(xstream.toXML(bss_intro));
    	
    }
    
    
    
    
    public static Object unpackageXML(){
    	
    	return null;
    }
    
    /**
     * This prepares a simulation for transmittal and sharing.
     * 
     * @param simulation
     * @return
     */
    public static String packageSimulation(String schema, Long sim_id){
         
    	Simulation sim = Simulation.getMeFullyLoaded(schema, sim_id);
        XStream xstream = new XStream();
                
        String returnString = xstream.toXML(sim);
        
        returnString += "<------------------------------------------------------->";

        List <InjectGroup> allInjectGroups = InjectGroup.getAllForSim(schema, sim_id);
        
		for (ListIterator<InjectGroup> li = allInjectGroups.listIterator(); li.hasNext();) {
			InjectGroup thisInjectGroup = li.next();
			
			returnString += xstream.toXML(thisInjectGroup);
			
			List <Inject> allInjects = Inject.getAllForSimAndGroup(schema, sim_id, thisInjectGroup.getId());
		
	        
			for (ListIterator<Inject> li_i = allInjects.listIterator(); li_i.hasNext();) {
				Inject thisInject = li_i.next();
				
				returnString += xstream.toXML(thisInject);
				
			}
		}
		
		returnString += "<------------------------------------------------------->";
		

        
        return returnString;
        
    }
    
    public static String packageObject(Object obj){
        

        XStream xstream = new XStream();
        
        return xstream.toXML(obj);
        
    }

	/**
	 * 
	 * @param fileloc
	 * @param schema
	 */
	public static void unpackSim(String fileloc, String schema){
		
		String fileLocation = FileIO.packaged_sim_dir + File.separator + fileloc;
		
		System.out.println("looking for file to unpack at " + fileLocation);
		
		File simToUnpackFile = new File(fileLocation);
		
		String xmlString = FileIO.getFileContents(simToUnpackFile);
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("sim", Simulation.class);
		
		// Saving this is necessary when there is no sims at all in the database
		// Otherwise, when try to save the first sim, one gets an error.
		Simulation simRead = new Simulation();
		
		simRead.saveMe(schema);
		
		simRead = (Simulation) xstream.fromXML(xmlString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(simRead);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		/*
		MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator<Actor> li = simRead.getActors().listIterator(); li.hasNext();) {
			Actor this_a = (Actor) li.next();
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this_a);
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		*/
	}
}
