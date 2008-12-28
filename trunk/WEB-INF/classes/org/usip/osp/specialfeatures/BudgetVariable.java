package org.usip.osp.specialfeatures;

import java.sql.*;
import java.util.*;

import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.graphs.VariableValue;
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
public class BudgetVariable {

    public boolean accumulates = true;
    
    public static final String TRANSTYPE_INITIAL = "initial";
    
    public static final String TRANSTYPE_MOVE = "move";
    
    public static final String TRANSTYPE_FINAL = "final";
    
    public static final String SPECIALFIELDLABEL = "sim_player_budget_transfer";
    
    String var_type = "";

    public float maxValue = Float.MAX_VALUE;

    public float minValue = Float.MIN_VALUE;

    public String initialValue = "";
    
    public String value = "";
    
    public String tracked = "false";

    public BudgetVariable(){
        value = "0";
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
                //insertLine(tableName, game_id, acct_id,
               //         running_game_id, game_round, "final", final_amount + "");
                
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
        /*
        debug += 
            insertLine(tableName, this.game_id, this.sim_id, running_game_id, "0", "initial", f.floatValue() + "");

        debug += 
            insertLine(tableName, this.game_id, this.sim_id, running_game_id, "0", "final", f.floatValue() + "");

        //Create the initial and final lines for this account for 1 time
        debug += 
            insertLine(tableName, this.game_id, this.sim_id, running_game_id, "1", "initial", f.floatValue() + "");

        debug += 
            insertLine(tableName, this.game_id, this.sim_id, running_game_id, "1", "final", f.floatValue() + "");
*/
        return debug;

    }

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


    public String removeFromDB() {
        // TODO Auto-generated method stub
        return null;
    }


    public Vector getPastValues(String tableName, String simId) {
        // TODO Auto-generated method stub
        return null;
    }


}
