package org.usip.osp.specialfeatures;

import java.sql.Connection;
import java.sql.Statement;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.SimulationSectionAssignment;
import org.usip.osp.persistence.MysqlDatabase;

/**
 * This is the base class of all of the 'special features' one can add to a simulation.
 * Essentially, it makes sure that the mechanics of storing and loading the object into
 * the archetypal, and also runs specific, tables gets taken care of.
 *
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
public abstract class SpecialFeature{
    
    /** This contains the name of the jsp page that typically shows this feature to the 
     * player. It is set, by convention, when the object is created. */
    public String jsp_page = "missing.jsp"; //$NON-NLS-1$

    /** The name of this special feature. */
    public String name = ""; //$NON-NLS-1$
    
    /** The tab heading of this feature seen by the player. */
    public String tab_heading = ""; //$NON-NLS-1$
    
    /** The title, if any, on the page. */
    public String page_title = ""; //$NON-NLS-1$
    
    /** A description of this special feature. */
    public String description = ""; //$NON-NLS-1$
    
    /** The game id associated with this special feature. */
    public String game_id = ""; //$NON-NLS-1$

    /** The id of the record in the SIM_values table particular to this special features. */ 
    public String sim_id = ""; //$NON-NLS-1$
    
    /** The id of the record in the 'special features' table. */
    private Long sf_id;
    
    /** The game section object used to point the user to the section containing the
     * web page exposing this special feature to the user. */
    public SimulationSectionAssignment gs = new SimulationSectionAssignment();
    
    /** This is the label that identifies this special feature in the special feature 
     * table. This is not as necessary now that many of the variables are being moved
     * into their own tables. */
    public abstract String getSpecialFieldLabel();
    
    public abstract String getShortNameBase();
    
    
    /**
     * Returns the special feature id of this special feature.
     * @return
     */
    public String get_sf_id(){
        return this.sf_id.toString();
    }
    
    
    /**
     * In general this gets a list of all of the special features of this particular
     * type for a simulation, and for each of them it
     * <ol>
     * <li>Stores a copy of it in the running simulation table.</li>
     * <li>Looks up any related elements that it must interact with.</li>
     * <li>Updates the information in the game section entry for this running game to
     * point at the right entries for this running simulation.</li>
     * </ol>
     * @param running_game_id
     * @param game
     * @return
     */
    public abstract String prep(String running_game_id, Simulation game);
    
    
    /**
     * This inserts the entry in the running game table to establish a version of this
     * object for each running simulation.
     * 
     * @param running_game_id
     * @param tableName
     * @return
     */
    public abstract String storeInRunningGameTable (String running_game_id, String tableName);
        
    /** Executes the SQL to create a table to hold this form of special feature. */
    static void createTable(String createTableSQL){
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            
            stmt.execute(createTableSQL);
            
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public abstract String removeFromDB();
}
