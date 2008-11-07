package org.usip.osp.specialfeatures;

import java.sql.*;
import java.util.*;

import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.Simulation;
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
public class BudgetVariable extends SimulationVariable {

    public boolean accumulates = true;
    
    public static final String TRANSTYPE_INITIAL = "initial";
    
    public static final String TRANSTYPE_MOVE = "move";
    
    public static final String TRANSTYPE_FINAL = "final";
    
    public static final String SPECIALFIELDLABEL = "sim_player_budget_transfer";

    public BudgetVariable(){
        value = "0";
    }
    
    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }


    @Override
    public String getShortNameBase() {
        return "sim_player_budg_xfer";
    }
    
    public static void createGameBudgetTable(String tableName){
        
        String createTableSQL = "CREATE TABLE `" + tableName + "` ( " +
            "`sim_id` int(11) NOT NULL auto_increment, " +
            "`sf_id` int(11) NOT NULL, " +
            "`game_id` int(11) NOT NULL, " +
            "`running_game_id` int(11) default '0', " + 
            "`var_name` varchar(100) default NULL, " +
            "`initial_value` varchar(10) default NULL, " +
            "`tracked` varchar(5) default NULL, " +
            "`prop_type` varchar(100) default NULL, " +
            "`description` longtext, " +
            "PRIMARY KEY  (`sim_id`)) ";
              
            createTable(createTableSQL);
    }
    
    public static void createGameBudgetTableValues(String tableName){
        
        String createTableSQL = "CREATE TABLE `" + tableName + "` ( " +
            "`sim_id` int(11) NOT NULL, " +
            "`game_id` int(11) NOT NULL, " +
            "`running_game_id` int(11) default '0', " + 
            "`game_round` int(11) default '0', " +
            "`trans_type` varchar(100) NOT NULL, " +
            "`trans_value` varchar(10) default NULL, " +
            "`source_acct` varchar(20) default '0', " +
            "`sink_acct` varchar(20) default '0'," +
            "`description` varchar(100) default NULL " +
            ") ";
              
            createTable(createTableSQL);
    }
    
    public static String removeTransaction(String tablename, String game_id, String running_game_id, 
            String game_round, String fromAcctID, String toAcctID) {
        
        String debug = "";
        
        String deleteSQL = "delete from " + tablename +" where " +
            "game_id = ? and running_game_id = ? and " +
            "game_round = ? and source_acct = ? and sink_acct = ?";
        
        debug = deleteSQL;
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            // Insert credit line
            PreparedStatement ps = connection
                    .prepareStatement(deleteSQL);
            ps.setString(1, game_id);
            ps.setString(2, running_game_id);
            ps.setString(3, game_round);
            ps.setString(4, fromAcctID);
            ps.setString(5, toAcctID);
            
            ps.execute();

            connection.close();

        } catch (Exception e) {
            debug += e.getMessage();
            e.printStackTrace();
        }

        return debug;
        
    }
    
    public static String getCurrentTransactionAmount(String tablename, String game_id, String running_game_id, 
            String game_round, String fromAcctID, String toAcctID){
        
        String selectSQL = "select trans_value from " + tablename + 
            " where ";
        
        // TODO
        return selectSQL;
        
    }
    

    /**
     * 
     * @param tablename
     * @param game_id
     * @param running_game_id
     * @param game_round
     * @param amount
     * @param fromAcctID
     * @param toAcctID
     * @return
     */
    public static String moveMoney(String tablename, String game_id, String running_game_id, 
            String game_round, String amount, String fromAcctID, String toAcctID, String description) {

        String debug = "";
        
        // Delete previous transaction for this round if it exists.
        removeTransaction(tablename, game_id, running_game_id, 
                game_round, fromAcctID, toAcctID);
        
        
        String insertSQL = "INSERT INTO " + tablename + " ( " +
            "`sim_id` , `game_id` , `running_game_id` , `game_round` , " +
            "`trans_type`, `trans_value` , `source_acct` , `sink_acct`,  `description`) " + 
            "VALUES ( '-1', ?, ?, ?, 'move', ?, ?, ?, ?) ";

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            // Insert the transaction line
            PreparedStatement ps = connection
                    .prepareStatement(insertSQL);
            ps.setString(1, game_id);
            ps.setString(2, running_game_id);
            ps.setString(3, game_round);
            ps.setString(4, amount);
            ps.setString(5, fromAcctID);
            ps.setString(6, toAcctID);
            ps.setString(7, description);
            
            ps.execute();

            connection.close();

        } catch (Exception e) {
            debug += e.getMessage();
            e.printStackTrace();
        }

        return debug;
    }
    
    class TransactionSum{
        
        public String acct = "";
        
        public String amount = "";
        
    }
    
    public String sumRound(String tableName, String game_id, String running_game_id, String game_round){
        
        String debug = "BudgetVariable.sumRound ";
        
        String selectRecords = "SELECT * FROM `" + tableName + "` " +
                "WHERE game_id = '1' " +
                "and running_game_id = '1' and game_round = '1' and trans_type = 'move'";
            
        debug += selectRecords;
        
        Hashtable listOfAccounts = new Hashtable();
        
        Vector transactionSums = new Vector();
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectRecords);

            while (rst.next()) {
                // Trans type will determine what we do with it.
                String ttype = rst.getString("trans_type");
                
                // If it is an initial amount, create a transaction sum containing it.
                if (ttype.equalsIgnoreCase(TRANSTYPE_INITIAL)){
                    TransactionSum ts = new TransactionSum();
                    ts.acct = rst.getString("sim_id");
                    ts.amount = rst.getString("trans_value");
                    transactionSums.add(ts);
                    //Put this account id in the master list
                    listOfAccounts.put(ts.acct, "set");
                } else if (ttype.equalsIgnoreCase(TRANSTYPE_MOVE)){
                    TransactionSum ts_from = new TransactionSum();
                    ts_from.acct = rst.getString("source_acct");
                    ts_from.amount = " - " + rst.getString("trans_value");
                    
                    TransactionSum ts_to = new TransactionSum();
                    ts_to.acct = rst.getString("sink_acct");
                    ts_to.amount = " - " + rst.getString("trans_value");
                    
                    //Make sure both accounts show up in the master list
                    listOfAccounts.put(ts_from.acct, "set");
                    listOfAccounts.put(ts_to.acct, "set");
                }    
                
            } // End of loop over results set
            
            // Loop over all of the accounts we found.
            for (Enumeration ea = listOfAccounts.keys(); ea.hasMoreElements();){
                String acct_id = (String) ea.nextElement();
                
                float final_amount = 0;
                
                // Going to loop over transaction sums, pick out the ones for this account,
                // and create final line.
                for (Enumeration ts = transactionSums.elements(); ts.hasMoreElements();){
                    TransactionSum this_ts = (TransactionSum) ts.nextElement();
                    
                    float thisAmount = 0;
                    try {
                        thisAmount = new Float(this_ts.amount).floatValue();
                    } catch (Exception ne){
                        debug += ne.getMessage();
                    }
                    final_amount += thisAmount;
                    
                }
                
                String deletePrevFinal = "delete from " + tableName + " where " +
                    "game_id = " + game_id + " and sim_id = " + acct_id + " and " +
                    "running_game_id = " + running_game_id + " and game_round = " + 
                    game_round + " and trans_type = 'final' ";
                
                stmt.execute(deletePrevFinal);
                
                //String insertFinalAmount 
                insertLine(tableName, game_id, acct_id,
                        running_game_id, game_round, "final", final_amount + "");
                
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return debug;
    }

    /**
     * Based on the type of propagation, advances data to the next round.
     * 
     */
    public String propagate(Simulation game, String gameround, String rgid) {

        String returnString = "SimulationVariable.propogate(): ";

        int game_round = new Integer(gameround).intValue();
        
        int last_round = game_round -1;

        // TODO
        /*
        // Sum round and make/replace final entry.
        sumRound(game.db_tablename_var_bud_v, game.id, rgid, last_round + "");
        
        // Get final value
        String nowValue = 
            getCurrentValue(game.db_tablename_var_bud_v, sim_id, last_round + "");
        
        //Store it in the initial/final line for the next round.    
        insertLine(game.db_tablename_var_bud_v, game.id, sim_id,
                rgid, game_round + "", "initial", nowValue);
        insertLine(game.db_tablename_var_bud_v, game.id, sim_id,
                rgid, game_round + "", "final", nowValue);
        
        */
        return returnString;
    }

    @Override
    public Vector getSetForASimulation(String game_id) {
        Vector rv = new Vector();

        String selectSDs = "SELECT * FROM `sf_var_budget` "
                + "WHERE game_id = " + game_id;

        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectSDs);

            while (rst.next()) {
                BudgetVariable bv = new BudgetVariable();

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

    @Override
    public Vector getSimVariablesForARunningSimulation(Simulation simulation, String rgid) {

    	// TODO
        Vector returnVector = null;
        
        /*this.getSetForASimulation(game.id);
        
        for(Enumeration e = returnVector.elements(); e.hasMoreElements();){
            BudgetVariable bv = (BudgetVariable) e.nextElement();
            
            bv.game_id = game.id;
            bv.sim_id = bv.lookUpMySimID(game.db_tablename_var_bud, rgid);   
            
        }
        */

        return returnVector;

    }

    @Override
    /**
     * Inserts into special_features table
     * 
     */
    public String store() {

        String debug = "start: ";         
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `sf_var_budget` ( sf_id, "
                    + "game_id , `var_name` ,`initial_value`, `description`  "
                    + " ) VALUES ( NULL, ?, ?, ?, ?)";

            debug += insertSQL;
            
            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.game_id);
            ps.setString(2, this.name);       
            ps.setString(3, this.value);
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
    public String prep(String running_game_id, Simulation game) {
        String returnString = "<br>BudgetVariable.prep<br>";
        
        // TODO
        //returnString += "table is: " + game.db_tablename_var_bud;

        // Find out if the game has simulation variables
        Vector simBudVars = new BudgetVariable().getSetForASimulation(game.getId().toString());

        for (Enumeration e = simBudVars.elements(); e.hasMoreElements();) {
            BudgetVariable bv = (BudgetVariable) e.nextElement();

            // This stores it, and gets its sim_id
            // TODO
            //bv.storeInRunningGameTable(running_game_id,
            //        game.db_tablename_var_bud);

            // Creates an initial value for it in the initial value table
            //TODO
            //returnString += 
            //    bv.createInitialValueEntry(game.db_tablename_var_bud_v, running_game_id);
        }

        return returnString;

    }
    
    /**
     * Inserts into special_features table
     * 
     */
    public String storeInRunningGameTable(String running_game_id,
            String tableName) {

        String debug = "<br>BudgetVariable.storeInRunningGameTable start: <br>";
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `"
                    + tableName
                    + "` ( sim_id, sf_id, "
                    + "game_id, running_game_id, `var_name` , "
                    + "`initial_value`,`tracked`, "
                    + " prop_type, description ) VALUES ( NULL, ?, ?, ?, ?, ?, ?, ?, ?)";

            debug += insertSQL;

            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, this.get_sf_id());
            ps.setString(2, this.game_id);
            ps.setString(3, running_game_id);
            ps.setString(4, this.name);
            ps.setString(5, this.initialValue);
            ps.setString(6, this.tracked);
            ps.setString(7, this.propagation_type);
            ps.setString(8, this.description);

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
    public String addNewValue(
            String tableName, String running_game_id, String game_round, String newValue) {
        return "addNewValue for a budget item is unused, since budget items imply double entry.";
    }

    @Override
    public String load() {
        try {
            
            String selectSQL = "Select * from sf_var_budget where sf_id = " + this.get_sf_id();
            
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
    
    public static String insertLine(String tableName, String game_id, String sim_id,
            String running_game_id, String game_round, String ttype, String tvalue){
        
        String debug = "BudgetVariable.insertLine <br>";
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            String insertSQL = "INSERT INTO `" + tableName + "` ( sim_id, "
                    + "game_id, running_game_id, game_round, "
                    + " trans_type, trans_value ) VALUES "
                    + "( ?, ?, ?, ?, '" + ttype + "', ? )";

            debug += insertSQL;
            
            PreparedStatement ps = connection.prepareStatement(insertSQL);

            ps.setString(1, sim_id);
            ps.setString(2, game_id);
            ps.setString(3, running_game_id);
            ps.setString(4, game_round);
            ps.setString(5, tvalue);

            ps.execute();

            connection.close();

        } catch (Exception e) {
            debug += e.getMessage();
            e.printStackTrace();
        }
        
        return debug;
    }

    @Override
    public String createInitialValueEntry(String tableName, String running_game_id) {
        String debug = "start: ";
        
        Float f = new Float(0);
        
        try {
            f = new Float(this.initialValue);
        } catch (Exception e){
            debug += e.getMessage();
            e.printStackTrace();
        }
        
        // Create the initial and final lines for this account for 0 time
        debug += 
            insertLine(tableName, this.game_id, this.sim_id, running_game_id, "0", "initial", f.floatValue() + "");

        debug += 
            insertLine(tableName, this.game_id, this.sim_id, running_game_id, "0", "final", f.floatValue() + "");

        //Create the initial and final lines for this account for 1 time
        debug += 
            insertLine(tableName, this.game_id, this.sim_id, running_game_id, "1", "initial", f.floatValue() + "");

        debug += 
            insertLine(tableName, this.game_id, this.sim_id, running_game_id, "1", "final", f.floatValue() + "");

        return debug;

    }

    @Override
    public String getCurrentValue(String tableName, String simId, String gameround) {
        // TODO 
        // Maybe add a dirty flag to allow transactions that don't sum the whol round,
        // and then have this check to see if dirty, and sum round before returning final value
        
        String returnString = "BudgetVariable.getCurrentValue ";
        
        String selectSQL = "SELECT trans_value FROM `" + tableName + "` WHERE `sim_id` = " +
            simId + " AND `game_round` = " + gameround + " AND `trans_type` = 'final'";
            
        returnString += selectSQL;
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            ResultSet rst = stmt.executeQuery(selectSQL);

            if (rst.next()) {
                returnString = rst.getString("trans_value");

            }

            connection.close();

        } catch (Exception e) {
            returnString += e.getMessage();
            e.printStackTrace();
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
