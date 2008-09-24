package org.usip.oscw.specialfeatures;

import java.sql.*;
import java.util.*;


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
public class AllowableResponse extends SpecialFeature{

    public String player_control_id = "";
    
    public String controlText = "Insert words";
    
    public static final String RT_SET = "freely_set";
    
    public static final String RT_SPECIFIC_VALUE = "set_value";
    
    public static final String SPECIALFIELDLABEL = "sim_allowable_response";
    
    public String response_type = "";
    
    public String set_value = "";
    
    
    public void execute(String dbtable, String sim_id, String game_round, String newValue){
        
        String updateSQL = "update " + dbtable + " set value = " + newValue + 
            " where sim_id = " + sim_id + " and game_round = " + game_round;
        
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            PreparedStatement ps = connection.prepareStatement(updateSQL);

            ps.execute();

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            this.name += e.getMessage();
        }
    }
    
    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "sim_ar_";
    }

    @Override
    public String store() {
        String debug = "start: ";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `special_features` ( sf_id, "
                    + "game_id, `sf_label` , `value_label1`, `value_label2`, `value_label3`, `value_text1` "
                    + " ) VALUES ( NULL , ?, '" + getSpecialFieldLabel() + "', ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.game_id);

            ps.setString(2, this.name);
            
            ps.setString(3, this.player_control_id);
            
            ps.setString(4, this.response_type);
            
            ps.setString(5, this.controlText);

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

    public void loadMeFromResultSet(ResultSet rst) throws SQLException {

        this.set_sf_id(rst.getLong("sf_id"));
        this.sim_id = this.get_sf_id();
        this.game_id = rst.getString("game_id");
        this.name = rst.getString("value_label1");
        this.player_control_id = rst.getString("value_label2");
        this.response_type = rst.getString("value_label3");
        this.controlText = rst.getString("value_text1");

    }
    @Override
    public String load() {
        // TODO Auto-generated method stub
        
        return "";
    }

    @Override
    public Vector getSetForASimulation(String game_id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String prep(String running_game_id, Simulation game) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String storeInRunningGameTable(String running_game_id, String tableName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String removeFromDB() {
        // TODO Auto-generated method stub
        return null;
    }

}
