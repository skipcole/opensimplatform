package org.usip.osp.specialfeatures;

import java.sql.*;
import java.util.Enumeration;
import java.util.Vector;

import org.usip.osp.baseobjects.*;
import org.usip.osp.persistence.*;

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
public class PlayerControl extends SpecialFeature {


    public IntegerVariable intVar = new IntegerVariable();

    /**
     * The set of things that a player can do using this control. For example, a
     * control may allow a player to set a variable to one of three fixed
     * values. Each of these 3 may have a radio button associated with them.
     */
    public Vector allowableResponses = new Vector();

    public String controlType = "direct";

    public static final String SPECIALFIELDLABEL = "sim_player_control";

    public PlayerControl() {

        this.jsp_page = "show_playercontrol.jsp";

    }

    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    public String getShortNameBase() {
        return "sim_pc_";
    }

    /**
     * Inserts into special_features table
     * 
     */
    public String store() {

        String debug = "start: ";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `special_features` ( sf_id, "
                    + "game_id, `sf_label` , `value_label1`, `value_label2`, `value_text1` "
                    + " ) VALUES ( NULL , ?, '" + getSpecialFieldLabel()
                    + "', ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.game_id);

            ps.setString(2, this.name);

            ps.setString(3, this.intVar.get_sf_id());

            ps.setString(4, this.description);

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

        // Store the responses allowed from the user
        for (Enumeration e = allowableResponses.elements(); e.hasMoreElements();) {
            AllowableResponse ar = (AllowableResponse) e.nextElement();

            ar.player_control_id = this.get_sf_id();
            ar.game_id = this.game_id;

            debug += ar.store();
        }

        // Make an entry so this player control can be assigned to players.
        gs.setTab_heading("PC: " + this.name);
        saveGameSectionEntry();

        return debug;

    }

    /*
     * Used to list all of the variables associated with a particular
     * simulation.
     * 
     * @param game_id @return
     * 
     * public static Vector getPlayerControlsForASimulation(String game_id) {
     * Vector rv = new Vector();
     * 
     * String selectSDs = "SELECT * FROM `special_features` " + "WHERE sf_label =
     * 'sim_playercontrol' AND game_id = " + game_id;
     * 
     * try { Connection connection = MysqlDatabase.getConnection(); Statement
     * stmt = connection.createStatement(); ResultSet rst =
     * stmt.executeQuery(selectSDs);
     * 
     * while (rst.next()) { PlayerControl pc = new PlayerControl();
     * 
     * pc.set_sf_id(rst.getString("sf_id")); pc.sim_id = pc.get_sf_id();
     * pc.game_id = rst.getString("game_id"); pc.name =
     * rst.getString("value_label1"); //sv.var_type =
     * rst.getString("value_label2");
     * 
     * rv.add(pc); } // End of loop over results set
     * 
     * connection.close();
     *  } catch (Exception e) { e.printStackTrace(); }
     * 
     * return rv; }
     */

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
            return e.getMessage();
        }
        
        

        this.intVar.load();

        loadMyAllowableResponses();
        
        return "";

    }

    /**
     * 
     * 
     */
    public void loadMyAllowableResponses() {
        
        this.allowableResponses = new Vector();
        
        String getResponsesSQL = "SELECT * FROM `special_features` WHERE `value_label2` = "
                + this.get_sf_id()
                + " AND `sf_label` = 'sim_allowable_response' ORDER BY sf_id";
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(getResponsesSQL);
            
            while (rst.next()) {
                AllowableResponse ar = new AllowableResponse();
                ar.loadMeFromResultSet(rst);
                
                this.allowableResponses.add(ar);
            }
            
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            this.description = e.getMessage();
        }
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
                PlayerControl pc = new PlayerControl();

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
        this.sim_id = this.get_sf_id();
        this.game_id = rst.getString("game_id");
        this.name = rst.getString("value_label1");
        this.intVar.set_sf_id(rst.getLong("value_label2"));
        this.description = rst.getString("value_text1");

    }

    @Override
    public String prep(String running_game_id, Simulation game) {
        String returnString = "";

        // Find out if the game has simulation variables
        Vector simPCs = new PlayerControl().getSetForASimulation(game.getId().toString());

        for (Enumeration e = simPCs.elements(); e.hasMoreElements();) {
            PlayerControl pc = (PlayerControl) e.nextElement();

            // TODO
            // This stores it, and gets its sim_id
            //returnString += pc.storeInRunningGameTable(running_game_id,
            //        game.db_tablename);

            // Get the sim_id of the variable that this player control affects
            //TODO
            //String var_sim_id = pc.intVar.lookUpMySimID(game.db_tablename_var_int,
            //        running_game_id);

            // Take the record id of the entry created above, and use that in
            // the game sections
            // TODO
            /*
            String updateSQL = "UPDATE `game_sections` SET page_file_name = '"
                    + this.jsp_page + "?sf_id=" + pc.get_sf_id()
                    + "&var_sim_id=" + var_sim_id
                    + "' WHERE `section_short_name` = '" + getShortNameBase()
                    + pc.get_sf_id() + "' " + "AND running_game_id = "
                    + running_game_id;
                    

            returnString += "<P>" + updateSQL + "</P>";
*/
            try {
                Connection connection = MysqlDatabase.getConnection();
                Statement stmt = connection.createStatement();

                //TODO
                //stmt.execute(updateSQL);

                connection.close();

            } catch (Exception er) {
                returnString += er.getMessage();
                er.printStackTrace();
            }

            // TODO
            // returnString += pc.createInitialValueEntry(running_game_id,
            // game.db_tablename + "_values");
        }

        return returnString;
    }

    @Override
    public String storeInRunningGameTable(String running_game_id,
            String tableName) {

        String debug = "start: ";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `"
                    + tableName
                    + "` ( sim_id, sf_id, "
                    + "game_id, running_game_id, `sf_label` , `value_label1`, `value_label2` "
                    + " ) VALUES ( NULL , ?, ?, ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.get_sf_id());
            ps.setString(2, this.game_id);
            ps.setString(3, running_game_id);
            ps.setString(4, this.getSpecialFieldLabel());
            ps.setString(5, this.name);
            ps.setString(6, this.intVar.get_sf_id());

            ps.execute();

            String queryId = "select LAST_INSERT_ID()";

            ResultSet rs = stmt.executeQuery(queryId);

            if (rs.next()) {
                this.sim_id = rs.getInt(1) + "";
            }
            connection.close();

        } catch (Exception e) {
            debug += "<font color=red>" + e.getMessage() + ":" + e.toString()
                    + "</font>";
            e.printStackTrace();
        }

        return debug;

    }

    @Override
    public String removeFromDB() {
        // TODO Auto-generated method stub
        return null;
    }

}
