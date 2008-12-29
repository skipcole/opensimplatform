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
public class PlayerControlBudgetTransfer extends SpecialFeature {
    
    /** Special Feature ID(s) of the accounts from which one can move funds from. */
    public String fromAcctString = "";
    
    /** Simulation run specific ID(s) of the accounts from which one can move funds from. */
    public String fromSimAcctString = "";
    
    /** Vector of all of the from accounts. */
    public Vector fromAccounts = new Vector();
    
    /** Special Feature ID(s) of the accounts from which one can move funds from. */
    public String toAcctString = "";
    
    /** Simulation run specific ID(s) of the accounts from which one can move funds from. */
    public String toSimAcctString = "";
    
    /** Vector of all of the to accounts. */
    public Vector toAccounts = new Vector();
    
    

    public static final String SPECIALFIELDLABEL = "sim_player_budget_transfer";

    public PlayerControlBudgetTransfer (){
        this.jsp_page = "show_budget_transfer.jsp";
    }
    
    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "sim_pc_fund_xfer_";
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
                PlayerControlBudgetTransfer pc = new PlayerControlBudgetTransfer();

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


    }
    

    @Override
    public String prep(String running_game_id, Simulation game) {
        
        String returnString = "";
        
        // Find out if the game has simulation variables
        Vector simPCs = new PlayerControlBudgetTransfer().getSetForASimulation(game.getId().toString());

        for (Enumeration e = simPCs.elements(); e.hasMoreElements();) {
            PlayerControlBudgetTransfer pcbt = (PlayerControlBudgetTransfer) e.nextElement();

            // Get the sim id of each of the from budgets 
            BudgetVariable fromBudgetVar = new BudgetVariable();
            //fromBudgetVar.set_sf_id(new Long(pcbt.fromAcctString));
            //fromBudgetVar.load();
            // TODO
            //fromBudgetVar.sim_id = fromBudgetVar.lookUpMySimID(game.db_tablename_var_bud, running_game_id);
            //pcbt.fromSimAcctString = fromBudgetVar.sim_id;
                
            // Get the sim id of each of the to accounts
            BudgetVariable toBudgetVar = new BudgetVariable();
            //toBudgetVar.set_sf_id(new Long(pcbt.toAcctString));
            //toBudgetVar.load();
            //TODO
            //toBudgetVar.sim_id = toBudgetVar.lookUpMySimID(game.db_tablename_var_bud, running_game_id);
            //pcbt.toSimAcctString = toBudgetVar.sim_id;
            
            
            // This stores it, and gets its sim_id
            // TODO
            //returnString += pcbt.storeInRunningGameTable(running_game_id,
            //        game.db_tablename);

            // Get the sim_id of the variable that this player control affects
            //String var_sim_id = pc.intVar.lookUpMySimID(game.db_tablename,
            //        running_game_id);

            // Take the record id of the entry created above, and use that in
            // the game sections
            String updateSQL = "UPDATE `game_sections` SET page_file_name = '"
                    + this.jsp_page + "?sf_id=" + pcbt.get_sf_id()
                    + "&sim_id=" + pcbt.sim_id
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
        String debug = "start: ";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `"
                    + tableName
                    + "` ( sim_id, sf_id, "
                    + "game_id, running_game_id, `sf_label` , `value_label1`, `value_label2`, `value_label3`, `value_text1`,  `value_text2`, `value_text3` "
                    + " ) VALUES ( NULL , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.get_sf_id());
            ps.setString(2, this.game_id);
            ps.setString(3, running_game_id);
            ps.setString(4, this.getSpecialFieldLabel());
            ps.setString(5, this.name);
            ps.setString(6, this.fromSimAcctString);
            ps.setString(7, this.toSimAcctString);
            ps.setString(8, this.description);
            ps.setString(9, this.fromAcctString);
            ps.setString(10, this.toAcctString);

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
