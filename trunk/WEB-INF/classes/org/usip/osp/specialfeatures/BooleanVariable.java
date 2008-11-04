package org.usip.osp.specialfeatures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.Simulation;
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
public class BooleanVariable extends SimulationVariable{

    @Override
    public String getSpecialFieldLabel() {
        return "sim_var_bool";
    }

    @Override
    public String getShortNameBase() {
        return "sim_var_bool_";
    }

    public static void createGameBooleanTable(String tableName){
        
        String createTableSQL = "CREATE TABLE `" + tableName + "` ( " +
            "`sim_id` int(11) NOT NULL auto_increment, " +
            "`sf_id` int(11) NOT NULL, " +
            "`game_id` int(11) NOT NULL, " +
            "`running_game_id` int(11) default '0', " + 
            "`var_name` varchar(100) default NULL, " +
            "`initial_value` varchar(5) default 'false', " +
            "`description` longtext, " +
            "PRIMARY KEY  (`sim_id`)) ";
              
            createTable(createTableSQL);
    }
    
    public static void createGameBooleanTableValues(String tableName){
        
        String createTableSQL = "CREATE TABLE `" + tableName + "` ( " +
            "`sim_id` int(11) NOT NULL, " +
            "`game_id` int(11) NOT NULL, " +
            "`running_game_id` int(11) default '0', " + 
            "`game_round` int(11) default '0', " +
            "`var_name` varchar(100) default NULL, " +
            "`value` varchar(5) default 'false' )";
              
            createTable(createTableSQL);
    }
    
    
    public BooleanVariable(){
             
        this.value = "false";
        
    }
    
    public void setValue(String inputString){
        
        if (inputString == null){
            this.value = "false";
            return;
        }
        
        if (inputString.equalsIgnoreCase("true")){
            this.value = "true";
        } else {
            this.value = "false";
        }
    }
    
    /**
     * Used to list all of the variables associated with a particular
     * simulation.
     * 
     * @param game_id
     * @return
     */
    public Vector getSetForASimulation(String game_id) {
        Vector rv = new Vector();

        String selectSDs = "SELECT * FROM `sf_var_boolean` "
                + "WHERE game_id = " + game_id;

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSDs);

            while (rst.next()) {
                BooleanVariable bv = new BooleanVariable();

                bv.set_sf_id(rst.getLong("sf_id"));
                bv.game_id = rst.getString("game_id");
                bv.name = rst.getString("var_name");
                bv.description = rst.getString("description");
                bv.initialValue = rst.getString("initial_value");

                rv.add(bv);
            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rv;
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

            String insertSQL = "INSERT INTO `sf_var_boolean` ( sf_id, "
                    + "game_id , `var_name` ,`initial_value`, `description`  "
                    + " ) VALUES ( NULL, ?, ?, ?, ?)";

            debug += insertSQL;
            
            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.game_id);
            ps.setString(2, this.name);       
            ps.setString(3, this.initialValue);
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

        return debug;

    }

    @Override
    public Vector getSimVariablesForARunningSimulation(Simulation simulation, String rgid) {
        
    	//TODO
        Vector returnVector = null; //this.getSetForASimulation(simulation.id);
        
        /* TODO
        for(Enumeration e = returnVector.elements(); e.hasMoreElements();){
            BooleanVariable bv = (BooleanVariable) e.nextElement();
            
            bv.game_id = simulation.id;
            bv.sim_id = bv.lookUpMySimID(simulation.db_tablename_var_bool, rgid);   
            
        }
        */

        return returnVector;

    }

    @Override
    public String prep(String running_game_id, Simulation game) {
        String returnString = "<br>BooleanVariable.prep start: <br>";

        // Find out if the game has simulation variables
        Vector simBoolVars = new BooleanVariable().getSetForASimulation(game.getId().toString());

        for (Enumeration e = simBoolVars.elements(); e.hasMoreElements();) {
            BooleanVariable bool_v = (BooleanVariable) e.nextElement();

            /*
            // This stores it, and gets its sim_id
            returnString += bool_v.storeInRunningGameTable(running_game_id,
                    game.db_tablename_var_bool);

            returnString += bool_v.createInitialValueEntry(game.db_tablename_var_bool_v, running_game_id);
            */
        }

        return returnString;
    }

    @Override
    public String addNewValue(String tableName, String running_game_id,
            String game_round, String newValue) {
        String debug = "start: ";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `" + tableName + "` ( sim_id, "
                    + "`game_id`, `running_game_id` , `game_round` , `var_name` , "
                    + "`value` ) VALUES ( ?, ?, ?, ?, ?, ? )";
            
            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.sim_id);
            ps.setString(2, this.game_id);
            ps.setString(3, running_game_id);
            ps.setString(4, game_round);
            ps.setString(5, this.name);
            ps.setString(6, newValue);
            
            ps.execute();

            connection.close();

        } catch (Exception e) {
            debug += e.getMessage();
            e.printStackTrace();
        }

        return debug;
    }

    @Override
    public String load() {
        
        try {
        
            String selectSQL = "Select * from sf_var_boolean where sf_id = " + this.get_sf_id();
            
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSQL);

            this.name = selectSQL;

            if (rst.next()) {

                this.set_sf_id(rst.getLong("sf_id"));
                this.game_id = rst.getString("game_id");
                this.name = rst.getString("var_name");
                
                this.value = rst.getString("initial_value");
                this.description = rst.getString("description");

            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        
        return "";
    }

    @Override
    public String createInitialValueEntry(String tableName, String running_game_id) {

        return addNewValue(tableName, running_game_id, "1", this.initialValue);

    }

    @Override
    public String propagate(Simulation game, String gameround, String rgid) {
        
    	//TODO
        String returnString = "";
        /*
        int this_round = turnRoundToInt(gameround);
        int past_round = this_round - 1;
        
        String pastValue = 
            this.getCurrentValue(game.db_tablename_var_bool_v, this.sim_id, past_round + "");
              
        returnString += this.addNewValue(game.db_tablename_var_bool_v, rgid, gameround, pastValue);
*/
        return returnString + "<br>propagated value of " + this.sim_id + " in round " + gameround + " to ";
        //+ pastValue;
   
        
    }

    @Override
    public String storeInRunningGameTable(String running_game_id, String tableName) {
        String debug = "<br>BooleanVariable.storeInRunningGameTable start: <br>";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `"
                    + tableName
                    + "` ( sim_id, sf_id, "
                    + "game_id, running_game_id, `var_name` , "
                    + "`initial_value`, "
                    + " description ) VALUES ( NULL, ?, ?, ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.get_sf_id());
            ps.setString(2, this.game_id);
            ps.setString(3, running_game_id);
            ps.setString(4, this.name);
            ps.setString(5, this.initialValue);
            ps.setString(6, this.description);

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
    public String getCurrentValue(String tableName, String simId, String gameround) {

        String returnString = "";
        
        String selectSQL = "select value from " + tableName + " where " +
            "sim_id = " + simId + " and game_round = " + gameround;
        
        returnString = selectSQL;
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            ResultSet rst = stmt.executeQuery(selectSQL);
            
            if (rst.next()){
                returnString = rst.getString("value");
                
            }
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            returnString += e.getMessage();
        }

        return returnString;
    }

    @Override
    public String removeFromDB() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector getPastValues(String tableName, String simId) {
        // TODO Auto-generated method stub
        return null;
    }
}
