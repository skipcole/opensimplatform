package org.usip.osp.baseobjects;

import java.io.File;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.log4j.*;
/**
 * This class handles reading of properties out of the properties file for the program and making them available.
 */
/*
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
public class USIP_OSP_Properties {

    private static ResourceBundle resourceBundle;

    /** A Hashtable of stored values to avoid re-reading of them. */
    private static Hashtable<String, String> hashedValues = new Hashtable<String, String>();
    
    /** A string indicating when the system is planned to go down next. */
    private static String nextPlannedDowntime;
    
    private static boolean foundPropertiesFile = false;
    
    public static boolean isFoundPropertiesFile() {
		return foundPropertiesFile;
	}

	public static void setFoundPropertiesFile(boolean foundPropertiesFile) {
		USIP_OSP_Properties.foundPropertiesFile = foundPropertiesFile;
	}

	public static String getNextPlannedDowntime() {
    	if (nextPlannedDowntime == null) {
    		nextPlannedDowntime = USIP_OSP_Properties.getValue("next_planned_outage");
    		return nextPlannedDowntime;
    	} else {
    		return nextPlannedDowntime;
    	}
	}

	public static void setNextPlannedDowntime(String newNextPlannedDowntime) {
		USIP_OSP_Properties.nextPlannedDowntime = newNextPlannedDowntime;
	}
	
	/** Returns the URL to the error page. */
	public static String getErrorJspUrl() {
		String error_url = USIP_OSP_Properties.getCachedValue("base_sim_url") + "error.jsp";
	
		return error_url;
	}

	static {
    	
        try {
            resourceBundle = ResourceBundle.getBundle("USIP_OSP_Properties", new Locale("en", "US")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            foundPropertiesFile = true;
        } catch (Exception e) {
        	foundPropertiesFile = false;
        	Logger.getRootLogger().warn("Properties file USIP_OSP_Properties_en_US.properties not found. Need it. Its a Big Deal."); //$NON-NLS-1$
        }
    }
	
	/**
	 * This method returns the release number which is stored here.
	 * @return
	 */
	public static String getRelease(){
		return "0.9.6";
	}
	
	
	public static final int SAME_SOFTWARE_VERSION = 0;
	
	public static final int EARLIER_SOFTWARE_VERSION_MAJOR = 1;
	public static final int EARLIER_SOFTWARE_VERSION_MINOR = 2;
	public static final int EARLIER_SOFTWARE_VERSION_MICRO = 3;
	
	public static final int FUTURE_SOFTWARE_VERSION_MAJOR = 4;
	public static final int FUTURE_SOFTWARE_VERSION_MINOR = 5;
	public static final int FUTURE_SOFTWARE_VERSION_MICRO = 6;
	
	public static int compareRelease(String simulation_release){
		
		StringTokenizer str = new StringTokenizer(getRelease(), ".");
		
		Integer this_major = new Integer(str.nextToken().trim());
		Integer this_minor = new Integer(str.nextToken().trim());
		Integer this_micro = new Integer(str.nextToken().trim());
		
		
		str = new StringTokenizer(simulation_release, ".");

		Integer sim_major = new Integer(str.nextToken().trim());
		Integer sim_minor = new Integer(str.nextToken().trim());
		Integer sim_micro = new Integer(str.nextToken().trim());
		
		if (this_major.intValue() > sim_major.intValue()){
			return EARLIER_SOFTWARE_VERSION_MAJOR;
		} else if (this_major.intValue() < sim_major.intValue()){
			return FUTURE_SOFTWARE_VERSION_MAJOR;
		}
		
		if (this_minor.intValue() > sim_minor.intValue()){
			return EARLIER_SOFTWARE_VERSION_MINOR;
		} else if (this_minor.intValue() < sim_minor.intValue()){
			return FUTURE_SOFTWARE_VERSION_MINOR;
		}
		
		if (this_micro.intValue() > sim_micro.intValue()){
			return EARLIER_SOFTWARE_VERSION_MICRO;
		} else if (this_micro.intValue() < sim_micro.intValue()){
			return FUTURE_SOFTWARE_VERSION_MICRO;
		}
		
		return SAME_SOFTWARE_VERSION;
		
	}
    
    /**
     * Gets a value for a property based on default environment value.
     * @param propertyName
     * @return
     */
    public static String getValue(String propertyName){
    	return getCachedValue(propertyName);
    }
    
    /**
     * Gets a value for a property based on the environment name (_local, _remote) passed in.
     * 
     * @param propertyName
     * @param envName
     * @return
     */
    public static String getCachedValue(String propertyName){
    	
    	String cachedAnswer = hashedValues.get(propertyName);
    	
    	if (cachedAnswer == null){
    		cachedAnswer = getRawValue(propertyName);
    		
    		if (cachedAnswer != null){
    			hashedValues.put(propertyName, cachedAnswer);
    		}
    		
    	}
    	
    	return cachedAnswer;
    }
    
    /**
     * Gets the value for the property directly from the properties file.
     * 
     * @param propertyName
     * @return
     */
    public static String getRawValue(String propertyName){
    	return resourceBundle.getString(propertyName);
    }
    
}
