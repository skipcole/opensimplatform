package org.usip.osp.baseobjects;

import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Ronald "Skip" Cole
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
public class USIP_OSCW_Properties {
    
    private static String schema = "usiposcw";

    private static ResourceBundle resourceBundle;
    
    /** This sets the set of properties we will be looking at */
    private static String environment_name = "_local";

    private static Hashtable<String, String> hashedValues = new Hashtable<String, String>();
    
    static {
        
    	String os_name = System.getProperty("os.name");
    	
    	System.out.println(os_name);
    	
    	String file_loc = System.getProperty("user.home");
    	
    	System.out.println("user.home is" + file_loc);
    	
        try {
            resourceBundle = ResourceBundle.getBundle("USIP_OSCW_Properties", new Locale("en", "US"));

            if ((os_name == null) || (!(os_name.equalsIgnoreCase("SunOS")))){
            	environment_name = "_local";
            } else {
            	environment_name = "_remote";
            }
            
        } catch (Exception e) {
            System.out.println("Properties file USIP_OSCW_Properties_en_US.properties not found. Need it. Its a Big Deal.");
        }
    }
    
    public static String getValue(String propertyName){
    	return getValue(propertyName, environment_name);
    }
    
    public static String getValue(String propertyName, String envName){
    	
    	String fullKey = propertyName + envName;
    	
    	String cachedAnswer = (String) hashedValues.get(fullKey);
    	
    	if (cachedAnswer == null){
    		cachedAnswer = resourceBundle.getString(fullKey);
    		
    		if (cachedAnswer != null){
    			hashedValues.put(fullKey, cachedAnswer);
    		}
    		
    	}
    	
    	return cachedAnswer;
    }
    
    public static String getRawValue(String propertyName){
        return resourceBundle.getString(propertyName);
    }

    public static String getSchema() {
        return schema;
    }

    public static void setSchema(String schema) {
        USIP_OSCW_Properties.schema = schema;
    }
    
}
