package org.usip.osp.unittests;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import org.usip.osp.persistence.MysqlDatabase;

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
public class BaseTester {

    public static final String b = "<BR>";
    public static final String r = "<font color=red>";
    public static final String g = "<font color=green>";
    public static final String ef = "</font>";
    
    private static ResourceBundle resourceBundle;
    
    static {
        try {
            resourceBundle = ResourceBundle.getBundle("MasterTester", new Locale("en", "US"));

        } catch (Exception e) {
            System.out.println("Properties file MasterTester_en_US.properties not found. Need it. Its a Big Deal.");
        }
    }
    
    /**
     * Cleans the database. 
     * @return
     */
    public static String cleanDatabase(){
        String returnString = makeHeader("Cleaning Database");
        
        String numb_clean_db_inst = resourceBundle.getString("numb_clean_db_inst");
        
        int numb_inst = new Integer(numb_clean_db_inst).intValue();
        
        Vector setToRun = new Vector();
        
        for (int ii = 1; ii <= numb_inst; ++ii){
            String inst = resourceBundle.getString("clean_db" + ii);
            setToRun.add(inst);
        }
        
        return returnString;
    }
    
    public static String makeHeader(String headerName){
        
        return(b + "------------------------" + b + g + headerName + ef );
    }
}
