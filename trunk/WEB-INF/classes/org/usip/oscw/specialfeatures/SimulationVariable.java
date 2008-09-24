package org.usip.oscw.specialfeatures;

import java.sql.*;
import java.util.*;

import org.usip.oscw.baseobjects.Simulation;
import org.usip.oscw.baseobjects.SimulationSection;
import org.usip.oscw.baseobjects.RunningSimulation;
import org.usip.oscw.baseobjects.Simulation;
import org.usip.oscw.graphs.Chart;
import org.usip.oscw.graphs.VariableValue;
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
public abstract class SimulationVariable extends SpecialFeature {

    String var_type = "";

    public float maxValue = Float.MAX_VALUE;

    public float minValue = Float.MIN_VALUE;

    public String initialValue = "";
    
    public String value = "";
    
    public String tracked = "false";


    public SimulationVariable() {

        this.jsp_page = "show_variable.jsp";

    }

    /**
     * This describes how the variable propagates (changes in value) from round
     * to round.
     */
    public String propagation_type = "";

    public void setVarType(String vt) {

        if (vt == null) {
            return;
        }

        if (vt.equalsIgnoreCase("integer")) {


        } else if (vt.equalsIgnoreCase("budget")) {

            //this.chart.selectDataStatement = "SELECT game_round, `value_int` FROM [sim_value_table_name] where sim_id = [simulation_id] order by game_round";
        } else {
        }

    }

    public String getVarType() {
        return this.var_type;
    }

    /**
     * Used to list all of the variables associated with a particular
     * simulation.
     * 
     * @param game_id
     * @return
     */
    public abstract Vector getSetForASimulation(String game_id);

    /**
     * Gets all of the simulation variables for a particular running game.
     * 
     * @param tableName
     * @param gameid
     * @param rgid
     * @return
     */
    public abstract Vector getSimVariablesForARunningSimulation(Simulation simulation, String rgid);

    /**
     * Inserts into special_features table
     * 
     */
    public abstract String store(); 

    /**
     * For each simulation variable insert an intial line into the simulation
     * table.
     * 
     * @Override
     * 
     * @param running_game_id
     * @param game
     * @return
     */
    public abstract String prep(String running_game_id, Simulation game);

    public abstract String createInitialValueEntry(String tableName, String running_game_id);

    /**
     * Inserts into special_features table
     * 
     */
    public abstract String storeInRunningGameTable(String running_game_id,
            String tableName);

    /**
     * Based on the type of propagation, advances data to the next round.
     * 
     */
    public abstract String propagate(Simulation game, String gameround, String rgid);

    public abstract String addNewValue(String tableName, String running_game_id, String game_round, String newValue);
    
    public abstract String getCurrentValue(String tableName, String simId, String gameround);

    public abstract Vector getPastValues(String tableName, String simId);

    /**
     * Gets the sim_id (the id unique to this special feature and running id) from
     * the table in which it is stored.
     * 
     * @param db_table
     * @param running_game_id
     * @return
     */
    public String lookUpMySimID(String db_table, String running_game_id) {

        String returnString = "";

        String getSQL = "SELECT sim_id FROM `" + db_table + "` WHERE sf_id = "
                + this.get_sf_id() + " AND running_game_id = "
                + running_game_id;

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(getSQL);

            if (rst.next()) {
                returnString = rst.getString("sim_id");
            } // End of loop over results set

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            returnString = this.get_sf_id() + "_" + running_game_id;
        }

        return returnString;

    }
    
    public int turnRoundToInt(String round){
        
        try {
            return new Integer(round).intValue();
        } catch (Exception e){
            e.printStackTrace();
            return -99;
        }
    }

}
