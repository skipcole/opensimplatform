package org.usip.osp.baseobjects;

import java.util.ResourceBundle;
/**
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
public class Debug {
    
    public static boolean debug_on = true;
    
    private static ResourceBundle resourceBundle;

    public static String text = "";

    public static void addText(String textToAdd){
        
        text = text + "<BR>";
        
    }
    
    public static String getText(){
        return text;
    }
    
    /**
     * Echoes back the string passed into it if debugging is on. 
     * @param message
     * @return
     */
    public static String getDebug(String message){
        
        if (debug_on){
            return message;
        } else {
            return "";
        }
    }
}
