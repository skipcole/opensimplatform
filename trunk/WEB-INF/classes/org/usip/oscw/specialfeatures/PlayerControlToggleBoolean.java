package org.usip.oscw.specialfeatures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import org.usip.oscw.baseobjects.Simulation;
import org.usip.oscw.persistence.MysqlDatabase;

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

    @Override
    public String store() {
        String debug = "start: ";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
 
            String insertSQL = "INSERT INTO `special_features` ( sf_id, "
                    + "game_id, `sf_label` , `value_label1`, `value_label2`, `value_label3`, "
                    + "`value_label4`, "
                    + "`value_text1`, `value_text2`, `value_text3` "
                    + " ) VALUES ( NULL , ?, '" + getSpecialFieldLabel()
                    + "', ?, ?, ?, ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.game_id);
            ps.setString(2, this.name);             // value_label1
            ps.setString(3, this.booleanVarSFid);   //value_label2
            ps.setString(4, this.title);            //value_label3
            ps.setString(5, this.tracked);          //value_label4
            
            ps.setString(6, this.description);              //value_text1
            ps.setString(7, this.setToFalseLabelMessage);   //value_text2
            ps.setString(8, this.setToTrueLabelMessage);    //value_text3

            ps.execute();

            String queryId = "select LAST_INSERT_ID()";

            ResultSet rs = stmt.executeQuery(queryId);

            if (rs.next()) {
                this.set_sf_id(rs.getLong(1));
            }
            connection.close();

        } catch (Exception e) {
            debug += e.getMessage();
            e.printStackTrace();
        }

        // Make an entry so this player control can be assigned to players.
        gs.setTab_heading("Toggle: " + this.name);
        saveGameSectionEntry();

        return debug;

    }

    @Override
    public String load() {
        
        String selectSQL = "SELECT * FROM `special_features` WHERE sf_id = " + this.get_sf_id();
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSQL);

            this.name = selectSQL;

            if (rst.next()) {
                this.loadMeFromResultSet(rst);
            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            this.name += e.getMessage();
            return e.getMessage();
        }
        
        return "";
    
    }

    @Override
    public Vector getSetForASimulation(String game_id) {
        Vector rv = new Vector();

        String selectSDs = "SELECT * FROM `special_features` "
                + "WHERE sf_label = '" + this.getSpecialFieldLabel()
                + "' AND game_id = " + game_id;

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSDs);

            while (rst.next()) {
                PlayerControlToggleBoolean pc = new PlayerControlToggleBoolean();

                pc.loadMeFromResultSet(rst);

                rv.add(pc);
            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rv;
    }
    
    public void loadMeFromResultSet(ResultSet rst) throws SQLException {

        this.set_sf_id(rst.getLong("sf_id"));
        this.game_id = rst.getString("game_id");
        this.name = rst.getString("value_label1");
        this.booleanVarSFid = rst.getString("value_label2");
        
        this.title = rst.getString("value_label3");
        this.tracked = rst.getString("value_label4");
        
        this.description = rst.getString("value_text1");
        this.setToFalseLabelMessage = rst.getString("value_text2");
        this.setToTrueLabelMessage = rst.getString("value_text3");

    }
    

    @Override
    public String prep(String running_game_id, Simulation game) {
        
        String returnString = "";
        
        // Find out if the game has simulation variables
        Vector simPCs = new PlayerControlToggleBoolean().getSetForASimulation(game.getId().toString());

        for (Enumeration e = simPCs.elements(); e.hasMoreElements();) {
            PlayerControlToggleBoolean pcbt = (PlayerControlToggleBoolean) e.nextElement();

            // Get the sim id of each of the from budgets 
            BooleanVariable boolVar = new BooleanVariable();
            boolVar.set_sf_id(new Long(pcbt.booleanVarSFid));
            boolVar.load();
            // TODO
            //boolVar.sim_id = boolVar.lookUpMySimID(game.db_tablename_var_bool, running_game_id);
            pcbt.booleanVarSimID = boolVar.sim_id;

            // Take the record id of the entry created above, and use that in
            // the game sections
            String updateSQL = "UPDATE `game_sections` SET page_file_name = '"
                    + this.jsp_page + "?sf_id=" + pcbt.get_sf_id()
                    + "&sim_id=" + boolVar.sim_id
                    + "' WHERE `section_short_name` = '" + getShortNameBase()
                    + pcbt.get_sf_id() + "' " + "AND running_game_id = "
                    + running_game_id;

            returnString += "<P>" + updateSQL + "</P>";

            try {
                Connection connection = MysqlDatabase.getConnection();
                Statement stmt = connection.createStatement();

                stmt.execute(updateSQL);

                connection.close();

            } catch (Exception er) {
                returnString += er.getMessage();
                er.printStackTrace();
            }

        }

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
