package org.usip.osp.specialfeatures;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.usip.osp.baseobjects.Simulation;

/*
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
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
public class PlayerControlToggleBoolean extends SpecialFeature {
    
    /** The Special Field id of the boolean variable affected by this control. */
    public String booleanVarSFid = ""; //$NON-NLS-1$
    
    /** The simulation run specific id of the boolean variable affected by this control. */
    public String booleanVarSimID = ""; //$NON-NLS-1$
    
    /** The message on the label to mark this variable as true. */
    public String setToTrueLabelMessage = ""; //$NON-NLS-1$
    
    /** The message on the label to mark this variable as false. */
    public String setToFalseLabelMessage = ""; //$NON-NLS-1$
    
    /** The title shown on the page. */
    public String title = ""; //$NON-NLS-1$
    
    /** */
    public String tracked = "false"; //$NON-NLS-1$
    
    public static final String SPECIALFIELDLABEL = "sim_player_toggle_boolean"; //$NON-NLS-1$

    public PlayerControlToggleBoolean (){
        this.jsp_page = "show_toggle_boolean.jsp"; //$NON-NLS-1$
    }
    
    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "sim_pc_toggle_boolean_"; //$NON-NLS-1$
    }

    
    public void loadMeFromResultSet(ResultSet rst) throws SQLException {


    }
    

    @Override
    public String prep(String running_game_id, Simulation game) {
        
        String returnString = ""; //$NON-NLS-1$
        

        return returnString;
    }

    @Override
    public String storeInRunningGameTable(String running_game_id, String tableName) {
        return "No need to store in running game table."; //$NON-NLS-1$
    }
    
    public String convertListToString(){
        return null;
    }
    
    public Vector convertStringToList(){
        return null;
    }

    @Override
    public String removeFromDB() {
        // TODO Auto-generated method stub
        return null;
    }

}
