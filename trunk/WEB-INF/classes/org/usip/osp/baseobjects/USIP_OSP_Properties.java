package org.usip.osp.baseobjects;

import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.log4j.*;
/**
 * This class handles reading of properties out of the properties file for the program and making them available.
 *
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
public class USIP_OSP_Properties {

    private static ResourceBundle resourceBundle;
    
    /** This sets the set of properties we will be looking at */
    private static String environment_name = "_local"; //$NON-NLS-1$

    /** A Hashtable of stored values to avoid re-reading of them. */
    private static Hashtable<String, String> hashedValues = new Hashtable<String, String>();
    
    static {
        
    	String os_name = System.getProperty("os.name"); //$NON-NLS-1$
    	
    	Logger.getRootLogger().debug(os_name);
    	
    	String file_loc = System.getProperty("user.home"); //$NON-NLS-1$
    	
    	Logger.getRootLogger().debug("user.home is" + file_loc); //$NON-NLS-1$
    	
        try {
            resourceBundle = ResourceBundle.getBundle("USIP_OSP_Properties", new Locale("en", "US")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            if ((os_name == null) || (!(os_name.equalsIgnoreCase("SunOS")))){ //$NON-NLS-1$
            	environment_name = "_local"; //$NON-NLS-1$
            } else {
            	environment_name = "_remote"; //$NON-NLS-1$
            }
            
        } catch (Exception e) {
            Logger.getRootLogger().debug("Properties file USIP_OSP_Properties_en_US.properties not found. Need it. Its a Big Deal."); //$NON-NLS-1$
        }
    }
    
    /**
     * Gets a value for a property based on default environment value.
     * @param propertyName
     * @return
     */
    public static String getValue(String propertyName){
    	return getValue(propertyName, environment_name);
    }
    
    /**
     * Gets a value for a property based on the environment name (_local, _remote) passed in.
     * 
     * @param propertyName
     * @param envName
     * @return
     */
    public static String getValue(String propertyName, String envName){
    	
    	String fullKey = propertyName + envName;
    	
    	String cachedAnswer = hashedValues.get(fullKey);
    	
    	if (cachedAnswer == null){
    		cachedAnswer = resourceBundle.getString(fullKey);
    		
    		if (cachedAnswer != null){
    			hashedValues.put(fullKey, cachedAnswer);
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
