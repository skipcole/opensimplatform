package org.usip.oscw.graphs;

import java.sql.*;
import java.util.Enumeration;
import java.util.Vector;

import org.jfree.chart.*;

import org.usip.oscw.baseobjects.Simulation;
import org.usip.oscw.persistence.MysqlDatabase;
import org.usip.oscw.specialfeatures.IntegerVariable;
import org.usip.oscw.specialfeatures.SpecialFeature;

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
public class Chart extends SpecialFeature {

    public JFreeChart this_chart = null;

    public String title = "";

    public String type = "";
    
    public String variableSFID = "";

    public String xAxisTitle = "";

    public String yAxisTitle = "";

    public int height = 300;

    public int width = 400;

    public String selectDataStatement = "";

    public static final String SPECIALFIELDLABEL = "linechart_page";

    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "linechart_page_";
    }
    
    public Chart(){
        
        selectDataStatement = "SELECT game_round, value FROM [sim_value_table_name] where sim_id = [simulation_id] order by game_round";
        
        jsp_page = "show_linechart.jsp";
    }

    public String store() {

        String debug = "start: ";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO charts ( "
                    + "`sf_id` , `game_id`, `chart_name`, `title` , `tab_heading`, `description`, "
                    + "`type` , `x_axis_title` , `y_axis_title`, `first_data_source`, "
                    + "`height`, `width`, var_sf_id) " + "VALUES ( "
                    + "NULL , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.game_id);
            ps.setString(2, this.name);
            ps.setString(3, this.title);
            ps.setString(4, this.tab_heading);
            ps.setString(5, this.description);

            ps.setString(6, this.type);
            ps.setString(7, this.xAxisTitle);
            ps.setString(8, this.yAxisTitle);
            ps.setString(9, this.selectDataStatement);
            ps.setString(10, this.height + "");
            ps.setString(11, this.width + "");
            
            ps.setString(12, this.variableSFID);

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
        
        gs.setTab_heading(this.name);
        saveGameSectionEntry();

        return debug;
    }

    public String load() {

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String selectSQL = "select * from charts where chart_id = "
                    + this.get_sf_id();

            ResultSet rst = stmt.executeQuery(selectSQL);

            if (rst.next()) {
                this.loadMeFromResultSet(rst);
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return "";

    }

    public void setHeight(String h) {

        try {
            this.height = new Integer(h).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWidth(String w) {

        try {
            this.width = new Integer(w).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector getSetForASimulation(String game_id) {
        Vector rv = new Vector();

        String selectSDs = "SELECT * FROM `charts` WHERE game_id = " + game_id;

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSDs);

            while (rst.next()) {
                Chart c = new Chart();

                c.loadMeFromResultSet(rst);

                rv.add(c);
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
        
        this.variableSFID = rst.getString("var_sf_id");
        
        this.name = rst.getString("chart_name");
        this.page_title = rst.getString("title");
        this.tab_heading = rst.getString("tab_heading");
        this.description = rst.getString("description");
        this.type = rst.getString("type");
        this.xAxisTitle = rst.getString("x_axis_title");
        this.yAxisTitle = rst.getString("y_axis_title");
        this.selectDataStatement = rst.getString("first_data_source");
        this.setHeight(rst.getString("height"));
        this.setWidth(rst.getString("width"));

    }

    @Override
    public String prep(String running_game_id, Simulation game) {
        
        String returnString = "Chart.prep<BR>";

        // Find out if the game has simulation variables
     // TODO
        Vector simCharts = null;
        //new Chart().getSetForASimulation(game.id);

        for (Enumeration e = simCharts.elements(); e.hasMoreElements();) {
            Chart chart = (Chart) e.nextElement();
            
            IntegerVariable iv = new IntegerVariable();
            
            //TODO
            //iv.set_sf_id(chart.variableSFID);
            //iv.sim_id = iv.lookUpMySimID(game.db_tablename_var_int, running_game_id);
            
            
            // Take the record id of the entry created above, and use that in
            // the game sections
            String updateSQL = "UPDATE `game_sections` SET page_file_name = "
                    + "'show_linechart.jsp?sim_id=" + iv.sim_id + "&chart_id="
                    + chart.get_sf_id() + "' WHERE `section_short_name` = 'linechart_page_"
                    + chart.get_sf_id() + "' " + "AND running_game_id = "
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
    public String storeInRunningGameTable(String running_game_id,
            String tableName) {
        return "No need to store in running game table.";
    }

    @Override
    public String removeFromDB() {
        String debug = "start: ";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String removeSQL = "delete from `charts` where sf_id = " + this.get_sf_id();

            debug += removeSQL;
            stmt.execute(removeSQL);
            
            connection.close();

        } catch (Exception e) {
            debug += " : " + e.getMessage();
            e.printStackTrace();
        }
        
        return debug;
    }

}