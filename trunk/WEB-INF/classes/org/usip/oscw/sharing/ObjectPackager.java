package org.usip.oscw.sharing;

import org.usip.oscw.baseobjects.BaseSimSection;
import org.usip.oscw.baseobjects.Simulation;
import org.usip.oscw.baseobjects.Simulation;

import com.thoughtworks.xstream.XStream;

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

    	/*
        Simulation simulation = new Simulation();
        
        XStream xstream = new XStream();
        
        xstream.alias("sim", Simulation.class);
        
        String s = xstream.toXML(simulation);
        
        System.out.println(s);
    	*/
    	play();
    }
    
    public static void play() {
    	BaseSimSection bss_intro = new BaseSimSection("usiposcw", "", "../oscw_osp", "introduction.jsp", "Introduction",
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
    public static String packageObject(Object obj){
         
        XStream xstream = new XStream();
        
        return xstream.toXML(obj);
        
    }
    
    /**
     * This prepares a simulation for transmittal and sharing.
     * 
     * @param simulation
     * @return
     */
    public static String packageSimulation(Simulation simulation){
         
        XStream xstream = new XStream();
        
        return xstream.toXML(simulation);
        
    }
}
