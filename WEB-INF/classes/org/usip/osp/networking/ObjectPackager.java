package org.usip.osp.networking;

import java.io.File;
import java.util.ListIterator;

import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.BaseSimSection;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
public class ObjectPackager {
	
    
    public static void main(String[] args) {


        Simulation sim1 = new Simulation("test");
        
        sim1.saveMe("test");
        
        //XStream xstream = new XStream();
        
        //xstream.alias("sim", Simulation.class);
        
        //String s = xstream.toXML(sim1);
        
        System.out.println(packageSimulation("test", sim1.getId()));
    	
        
        
    	//play();
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
        
        //xstream.alias("Simulation", Simulation.class);

        return xstream.toXML(sim);
        
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
