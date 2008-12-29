package org.usip.osp.specialfeatures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MysqlDatabase;

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
public class PlayerControlToggleBoolean extends SpecialFeature {
    
    /** The Special Field id of the boolean variable affected by this control. */
    public String booleanVarSFid = "";
    
    /** The simulation run specific id of the boolean variable affected by this control. */
    public String booleanVarSimID = "";
    
    /** The message on the label to mark this variable as true. */
    public String setToTrueLabelMessage = "";
    
    /** The message on the label to mark this variable as false. */
    public String setToFalseLabelMessage = "";
    
    /** The title shown on the page. */
    public String title = "";
    
    /** */
    public String tracked = "false";
    
    public static final String SPECIALFIELDLABEL = "sim_player_toggle_boolean";

    public PlayerControlToggleBoolean (){
        this.jsp_page = "show_toggle_boolean.jsp";
    }
    
    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "sim_pc_toggle_boolean_";
    }

    
    public void loadMeFromResultSet(ResultSet rst) throws SQLException {


    }
    

    @Override
    public String prep(String running_game_id, Simulation game) {
        
        String returnString = "";
        

        return returnString;
    }

    @Override
    public String storeInRunningGameTable(String running_game_id, String tableName) {
        return "No need to store in running game table.";
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
