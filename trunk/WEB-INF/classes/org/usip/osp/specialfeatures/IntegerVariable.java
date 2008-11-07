package org.usip.osp.specialfeatures;

import java.sql.*;
import java.util.*;

import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.graphs.Chart;
import org.usip.osp.graphs.VariableValue;
import org.usip.osp.persistence.MysqlDatabase;

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
public class IntegerVariable extends SimulationVariable {

    public String has_max = "false";

    public String max_value = "0";

    public String has_min = "false";

    public String min_value = "0";

    public static final String SPECIALFIELDLABEL = "sim_int_var";

    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "sim_int_var_";
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

        String selectSDs = "SELECT * FROM `sf_var_integer` "
                + "WHERE game_id = " + game_id;

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSDs);

            while (rst.next()) {
                IntegerVariable iv = new IntegerVariable();

                iv.loadMeFromResultSet(rst);

                rv.add(iv);
            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rv;
    }

    public void setInitialValue(String inputString) {

        if (inputString == null) {
            this.initialValue = "";
            return;
        }

        initialValue = inputString;
    }

    public String getInitialValue() {

        return initialValue;
    }

    public String addNewValue(String tableName, String running_game_id,
            String game_round, String newValue) {

        String debug = "start: ";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `"
                    + tableName
                    + "` ( sim_id, "
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

    /**
     * Copies all of the variables into the running game table, and sets a value
     * for round 0 in the values table.
     */
    public String prep(String running_game_id, Simulation game) {

        String returnString = "";

        // Find out if the game has simulation variables
        Vector simIntVars = new IntegerVariable().getSetForASimulation(game.getId().toString());

        for (Enumeration e = simIntVars.elements(); e.hasMoreElements();) {
            IntegerVariable sv = (IntegerVariable) e.nextElement();

            // This stores it, and gets its sim_id
            // TODO
            //returnString += sv.storeInRunningGameTable(running_game_id,
            //        game.db_tablename_var_int);

            // TODO
            //returnString += sv.createInitialValueEntry(
            //        game.db_tablename_var_int_v, running_game_id);
        }

        return returnString;
    }

    @Override
    public String createInitialValueEntry(String tableName,
            String running_game_id) {

        return addNewValue(tableName, running_game_id, "1", this.initialValue);
    }

    /**
     * Gets all of the simulation variables for a particular running game.
     * 
     * @param game
     * @param rgid
     * @return
     */
    public Vector getSimVariablesForARunningSimulation(Simulation simulation, String rgid) {

    	//TODO
        Vector returnVector = null;
    	//getSetForASimulation(simulation.id);

        for (Enumeration e = returnVector.elements(); e.hasMoreElements();) {
            IntegerVariable iv = (IntegerVariable) e.nextElement();

            //iv.game_id = game.id;
            // TODO
            //iv.sim_id = iv.lookUpMySimID(game.db_tablename_var_int, rgid);

        }

        return returnVector;
    }

    /**
     * Inserts into sf_var_integer table
     * 
     */
    public String store() {

        String debug = "start: ";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `sf_var_integer` ( "
                    + "`sf_id`, `game_id` , `var_name`, `description` , `prop_type` , "
                    + "`initial_value` ,`value` , "
                    + "`has_max` , `max_value` , `has_min` , `min_value` ) "
                    + " VALUES ( "
                    + "NULL , ?, ?, ?, ?, ?, '0', 'false', '0', 'false', '0' )";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.game_id);
            ps.setString(2, this.name);
            ps.setString(3, this.description);
            ps.setString(4, this.propagation_type);
            ps.setString(5, this.initialValue);

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

    /**
     * Loads the value from the special features table for integers.
     * 
     * @return
     */
    public String load() {

        String selectSQL = "SELECT * FROM `sf_var_integer` WHERE sf_id = "
                + this.get_sf_id();

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSQL);

            this.name = selectSQL;

            if (rst.next()) {
                loadMeFromResultSet(rst);

            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            this.name += e.getMessage();
            return e.getMessage();
        }
        return "";
    }

    public void loadMeFromResultSet(ResultSet rst) throws SQLException {

        this.set_sf_id(rst.getLong("sf_id"));
        this.game_id = rst.getString("game_id");
        this.name = rst.getString("var_name");
        this.description = rst.getString("description");
        this.propagation_type = rst.getString("prop_type");
        this.initialValue = rst.getString("initial_value");

        this.maxValue = rst.getFloat("max_value");
        this.minValue = rst.getFloat("min_value");

    }

    public String propagate(Simulation game, String gameround, String rgid) {

        String returnString = "IntegerVariable.propogate(): ";

        int game_round = new Integer(gameround).intValue();

        if (propagation_type.equalsIgnoreCase("fibonacci")) {

            // Select past values
        	// TODO
            Vector v = null;
            //getPastValues(game.db_tablename_var_int_v, this.sim_id);

            returnString += "<BR>numb past values is: " + v.size();
            // Determin new value
            int newValue = 99;

            if (v.size() == 1) {
                VariableValue vv1 = (VariableValue) v.get(0);
                newValue = vv1.intValue;
                
            } else if (v.size() >= 2) {
                VariableValue vv1 = (VariableValue) v.get(v.size() - 1);
                VariableValue vv2 = (VariableValue) v.get(v.size() - 2);

                newValue = vv1.intValue + vv2.intValue;
            }

            returnString += "<br>new value is: " + newValue;

            // Insert line with new value
         // TODO
            //addNewValue(game.db_tablename_var_int_v, rgid, game_round + "",
            //        newValue + "");

        } else {

        	// TODO
            //String now = getCurrentValue(game.db_tablename_var_int_v,
            //        this.sim_id, (game_round - 1) + "");

            int newValue = 69;

            try {
            	// TODO
            	//newValue = new Integer(now).intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert line with new value
         // TODO
            //addNewValue(game.db_tablename_var_int_v, rgid, game_round + "",
            //        newValue + "");
        }

        return returnString;
    }

    public String storeInRunningGameTable(String running_game_id,
            String tableName) {

        String debug = "start: ";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `" + tableName
                    + "` ( sim_id, sf_id, `game_id`,`running_game_id`, "
                    + "`var_name`, `initial_value` , "
                    + "`has_min`, `has_max` , `min_value`, `max_value`, "
                    + "`prop_type` , `description` ) "
                    + "VALUES (NULL , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.get_sf_id());
            ps.setString(2, this.game_id);
            ps.setString(3, running_game_id);
            ps.setString(4, this.name);
            ps.setString(5, this.initialValue);
            ps.setString(6, this.has_min);
            ps.setString(7, this.has_max);
            ps.setString(8, this.min_value);
            ps.setString(9, this.max_value);
            ps.setString(10, this.propagation_type);
            ps.setString(11, this.description);

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
    public String getCurrentValue(String tableName, String simId,
            String gameround) {
        String returnString = "";

        String selectSQL = "select value from " + tableName + " where "
                + "sim_id = " + simId + " and game_round = " + gameround;

        returnString = selectSQL;

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            ResultSet rst = stmt.executeQuery(selectSQL);

            if (rst.next()) {
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
        String debug = "start: ";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String removeSQL = "delete from `sf_var_integer` where sf_id = "
                    + this.get_sf_id();

            debug += removeSQL;
            stmt.execute(removeSQL);

            connection.close();

        } catch (Exception e) {
            debug += " : " + e.getMessage();
            e.printStackTrace();
        }

        return debug;

    }

    @Override
    public Vector getPastValues(String tableName, String simId) {

        Vector returnVector = new Vector();

        String selectSQL = "select * from " + tableName + " where sim_id = "
                + simId + " order by game_round";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSQL);

            while (rst.next()) {
                VariableValue vv = new VariableValue();

                vv.game_round = rst.getInt("game_round");

                String int_string = rst.getString("value");
                vv.intValue = new Integer(int_string).intValue();

                returnVector.add(vv);

            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnVector;

    }



}