package org.usip.osp.graphs;

import java.sql.*;
import java.util.Enumeration;
import java.util.Vector;

import org.jfree.chart.*;

import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MysqlDatabase;
import org.usip.osp.specialfeatures.IntegerVariable;
import org.usip.osp.specialfeatures.SpecialFeature;

/**
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
public class Chart extends SpecialFeature {

    private JFreeChart this_chart = null;

    private String title = ""; //$NON-NLS-1$

    private String type = ""; //$NON-NLS-1$
    
    private String variableSFID = ""; //$NON-NLS-1$

    private String xAxisTitle = ""; //$NON-NLS-1$

    private String yAxisTitle = ""; //$NON-NLS-1$

    private int height = 300;

    private int width = 400;

    private String selectDataStatement = ""; //$NON-NLS-1$

    public static final String SPECIALFIELDLABEL = "linechart_page"; //$NON-NLS-1$

    public JFreeChart getThis_chart() {
		return this.this_chart;
	}

	public void setThis_chart(JFreeChart this_chart) {
		this.this_chart = this_chart;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVariableSFID() {
		return this.variableSFID;
	}

	public void setVariableSFID(String variableSFID) {
		this.variableSFID = variableSFID;
	}

	public String getXAxisTitle() {
		return this.xAxisTitle;
	}

	public void setXAxisTitle(String axisTitle) {
		this.xAxisTitle = axisTitle;
	}

	public String getYAxisTitle() {
		return this.yAxisTitle;
	}

	public void setYAxisTitle(String axisTitle) {
		this.yAxisTitle = axisTitle;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getSelectDataStatement() {
		return this.selectDataStatement;
	}

	public void setSelectDataStatement(String selectDataStatement) {
		this.selectDataStatement = selectDataStatement;
	}

	@Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "linechart_page_"; //$NON-NLS-1$
    }
    
    public Chart(){
        
        this.selectDataStatement = "SELECT game_round, value FROM [sim_value_table_name] where sim_id = [simulation_id] order by game_round"; //$NON-NLS-1$
        
        this.jsp_page = "show_linechart.jsp"; //$NON-NLS-1$
    }

    public String store() {

        String debug = "start: "; //$NON-NLS-1$
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO charts ( " //$NON-NLS-1$
                    + "`sf_id` , `game_id`, `chart_name`, `title` , `tab_heading`, `description`, " //$NON-NLS-1$
                    + "`type` , `x_axis_title` , `y_axis_title`, `first_data_source`, " //$NON-NLS-1$
                    + "`height`, `width`, var_sf_id) " + "VALUES ( " //$NON-NLS-1$ //$NON-NLS-2$
                    + "NULL , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //$NON-NLS-1$

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
            ps.setString(10, this.height + ""); //$NON-NLS-1$
            ps.setString(11, this.width + ""); //$NON-NLS-1$
            
            ps.setString(12, this.variableSFID);

            ps.execute();

            String queryId = "select LAST_INSERT_ID()"; //$NON-NLS-1$

            ResultSet rs = stmt.executeQuery(queryId);

            if (rs.next()) {
            	
                //this.set_sf_id(rs.getLong(1));
            }

            connection.close();

        } catch (Exception e) {
            debug += e.getMessage();
            e.printStackTrace();
        }
        
        this.gs.setTab_heading(this.name);
        //saveGameSectionEntry();

        return debug;
    }

    public String load() {

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String selectSQL = "select * from charts where chart_id = " //$NON-NLS-1$
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

        return ""; //$NON-NLS-1$

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

    public Vector getSetForASimulation(String game_id) {
        Vector rv = new Vector();

        String selectSDs = "SELECT * FROM `charts` WHERE game_id = " + game_id; //$NON-NLS-1$

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

        //this.set_sf_id(rst.getLong("sf_id"));
        this.game_id = rst.getString("game_id"); //$NON-NLS-1$
        
        this.variableSFID = rst.getString("var_sf_id"); //$NON-NLS-1$
        
        this.name = rst.getString("chart_name"); //$NON-NLS-1$
        this.page_title = rst.getString("title"); //$NON-NLS-1$
        this.tab_heading = rst.getString("tab_heading"); //$NON-NLS-1$
        this.description = rst.getString("description"); //$NON-NLS-1$
        this.type = rst.getString("type"); //$NON-NLS-1$
        this.xAxisTitle = rst.getString("x_axis_title"); //$NON-NLS-1$
        this.yAxisTitle = rst.getString("y_axis_title"); //$NON-NLS-1$
        this.selectDataStatement = rst.getString("first_data_source"); //$NON-NLS-1$
        this.setHeight(rst.getString("height")); //$NON-NLS-1$
        this.setWidth(rst.getString("width")); //$NON-NLS-1$

    }

    @Override
    public String prep(String running_game_id, Simulation game) {
        
        String returnString = "Chart.prep<BR>"; //$NON-NLS-1$

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
            String updateSQL = "UPDATE `game_sections` SET page_file_name = " //$NON-NLS-1$
                    + "'show_linechart.jsp?sim_id=" + iv.sim_id + "&chart_id=" //$NON-NLS-1$ //$NON-NLS-2$
                    + chart.get_sf_id() + "' WHERE `section_short_name` = 'linechart_page_" //$NON-NLS-1$
                    + chart.get_sf_id() + "' " + "AND running_game_id = " //$NON-NLS-1$ //$NON-NLS-2$
                    + running_game_id;

            returnString += "<P>" + updateSQL + "</P>"; //$NON-NLS-1$ //$NON-NLS-2$
            
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
        return "No need to store in running game table."; //$NON-NLS-1$
    }

    @Override
    public String removeFromDB() {
        String debug = "start: "; //$NON-NLS-1$

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String removeSQL = "delete from `charts` where sf_id = " + this.get_sf_id(); //$NON-NLS-1$

            debug += removeSQL;
            stmt.execute(removeSQL);
            
            connection.close();

        } catch (Exception e) {
            debug += " : " + e.getMessage(); //$NON-NLS-1$
            e.printStackTrace();
        }
        
        return debug;
    }

}