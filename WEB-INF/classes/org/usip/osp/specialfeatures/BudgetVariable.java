package org.usip.osp.specialfeatures;

import java.sql.*;
import java.util.*;

import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MysqlDatabase;

/*
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
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
    
    public static final String TRANSTYPE_INITIAL = "initial"; //$NON-NLS-1$
    
    public static final String TRANSTYPE_MOVE = "move"; //$NON-NLS-1$
    
    public static final String TRANSTYPE_FINAL = "final"; //$NON-NLS-1$
    
    public static final String SPECIALFIELDLABEL = "sim_player_budget_transfer"; //$NON-NLS-1$
    
    String var_type = ""; //$NON-NLS-1$

    public float maxValue = Float.MAX_VALUE;

    public float minValue = Float.MIN_VALUE;

    public String initialValue = ""; //$NON-NLS-1$
    
    public String value = ""; //$NON-NLS-1$
    
    public String tracked = "false"; //$NON-NLS-1$

    public BudgetVariable(){
        this.value = "0"; //$NON-NLS-1$
    }
    
    private Long sim_id;
    private Long running_sim_id;
    private String var_name;
    private String description;
    

    
    public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public String getVar_name() {
		return var_name;
	}

	public void setVar_name(String var_name) {
		this.var_name = var_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


    
    
    public static String getCurrentTransactionAmount(String tablename, String game_id, String running_game_id, 
            String game_round, String fromAcctID, String toAcctID){
        
        String selectSQL = "select trans_value from " + tablename +  //$NON-NLS-1$
            " where "; //$NON-NLS-1$
        
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

        
        // Delete previous transaction for this round if it exists.
       // removeTransaction(tablename, game_id, running_game_id, 
       //         game_round, fromAcctID, toAcctID);
        
        
        String insertSQL = "INSERT INTO " + tablename + " ( " + //$NON-NLS-1$ //$NON-NLS-2$
            "`sim_id` , `game_id` , `running_game_id` , `game_round` , " + //$NON-NLS-1$
            "`trans_type`, `trans_value` , `source_acct` , `sink_acct`,  `description`) " +  //$NON-NLS-1$
            "VALUES ( '-1', ?, ?, ?, 'move', ?, ?, ?, ?) "; //$NON-NLS-1$


        return insertSQL;
    }
    
    class TransactionSum{
        
        public String acct = ""; //$NON-NLS-1$
        
        public String amount = ""; //$NON-NLS-1$
        
    }
    
    public String sumRound(String tableName, String game_id, String running_game_id, String game_round){
        
        String debug = "BudgetVariable.sumRound "; //$NON-NLS-1$
        
        String selectRecords = "SELECT * FROM `" + tableName + "` " + //$NON-NLS-1$ //$NON-NLS-2$
                "WHERE game_id = '1' " + //$NON-NLS-1$
                "and running_game_id = '1' and game_round = '1' and trans_type = 'move'"; //$NON-NLS-1$
            
        debug += selectRecords;
        
        Hashtable listOfAccounts = new Hashtable();
        
        Vector transactionSums = new Vector();
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(selectRecords);

            while (rst.next()) {
                // Trans type will determine what we do with it.
                String ttype = rst.getString("trans_type"); //$NON-NLS-1$
                
                // If it is an initial amount, create a transaction sum containing it.
                if (ttype.equalsIgnoreCase(TRANSTYPE_INITIAL)){
                    TransactionSum ts = new TransactionSum();
                    ts.acct = rst.getString("sim_id"); //$NON-NLS-1$
                    ts.amount = rst.getString("trans_value"); //$NON-NLS-1$
                    transactionSums.add(ts);
                    //Put this account id in the master list
                    listOfAccounts.put(ts.acct, "set"); //$NON-NLS-1$
                } else if (ttype.equalsIgnoreCase(TRANSTYPE_MOVE)){
                    TransactionSum ts_from = new TransactionSum();
                    ts_from.acct = rst.getString("source_acct"); //$NON-NLS-1$
                    ts_from.amount = " - " + rst.getString("trans_value"); //$NON-NLS-1$ //$NON-NLS-2$
                    
                    TransactionSum ts_to = new TransactionSum();
                    ts_to.acct = rst.getString("sink_acct"); //$NON-NLS-1$
                    ts_to.amount = " - " + rst.getString("trans_value"); //$NON-NLS-1$ //$NON-NLS-2$
                    
                    //Make sure both accounts show up in the master list
                    listOfAccounts.put(ts_from.acct, "set"); //$NON-NLS-1$
                    listOfAccounts.put(ts_to.acct, "set"); //$NON-NLS-1$
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
                
                String deletePrevFinal = "delete from " + tableName + " where " + //$NON-NLS-1$ //$NON-NLS-2$
                    "game_id = " + game_id + " and sim_id = " + acct_id + " and " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    "running_game_id = " + running_game_id + " and game_round = " +  //$NON-NLS-1$ //$NON-NLS-2$
                    game_round + " and trans_type = 'final' "; //$NON-NLS-1$
                
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

        String returnString = "SimulationVariable.propogate(): "; //$NON-NLS-1$

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
        String debug = "start: "; //$NON-NLS-1$
        
        /*
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
        
        String returnString = "BudgetVariable.getCurrentValue "; //$NON-NLS-1$
        
        String selectSQL = "SELECT trans_value FROM `" + tableName + "` WHERE `sim_id` = " + //$NON-NLS-1$ //$NON-NLS-2$
            simId + " AND `game_round` = " + gameround + " AND `trans_type` = 'final'"; //$NON-NLS-1$ //$NON-NLS-2$
            
        returnString += selectSQL;
        
        try {
            Connection connection = MysqlDatabase.getConnection();
            Statement stmt = connection.createStatement();

            ResultSet rst = stmt.executeQuery(selectSQL);

            if (rst.next()) {
                returnString = rst.getString("trans_value"); //$NON-NLS-1$

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
