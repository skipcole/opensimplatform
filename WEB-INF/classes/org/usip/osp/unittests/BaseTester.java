package org.usip.osp.unittests;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.*;

/**
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
public class BaseTester {

    public static final String b = "<BR>"; //$NON-NLS-1$
    public static final String r = "<font color=red>"; //$NON-NLS-1$
    public static final String g = "<font color=green>"; //$NON-NLS-1$
    public static final String ef = "</font>"; //$NON-NLS-1$
    
    private static ResourceBundle resourceBundle;
    
    static {
        try {
            resourceBundle = ResourceBundle.getBundle("MasterTester", new Locale("en", "US")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        } catch (Exception e) {
            Logger.getRootLogger().debug("Properties file MasterTester_en_US.properties not found. Need it. Its a Big Deal."); //$NON-NLS-1$
        }
    }
    
    /**
     * Cleans the database. 
     * @return
     */
    public static String cleanDatabase(){
        String returnString = makeHeader("Cleaning Database"); //$NON-NLS-1$
        
        String numb_clean_db_inst = resourceBundle.getString("numb_clean_db_inst"); //$NON-NLS-1$
        
        int numb_inst = new Integer(numb_clean_db_inst).intValue();
        
        Vector setToRun = new Vector();
        
        for (int ii = 1; ii <= numb_inst; ++ii){
            String inst = resourceBundle.getString("clean_db" + ii); //$NON-NLS-1$
            setToRun.add(inst);
        }
        
        return returnString;
    }
    
    public static String makeHeader(String headerName){
        
        return(b + "------------------------" + b + g + headerName + ef ); //$NON-NLS-1$
    }
}
