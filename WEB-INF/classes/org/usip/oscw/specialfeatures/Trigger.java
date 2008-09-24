package org.usip.oscw.specialfeatures;

import java.util.*;
import org.usip.oscw.baseobjects.*;

import org.usip.oscw.baseobjects.Simulation;

/**
 * This class allows one to set a trigger point such that when the value of one
 * variable switches beyond a limit, a separate action is taken.
 * 
 * This will probably later have to be subclassed, and as the incorporation of more 
 * advanced formulas comes, this will probably change a lot.
 * 
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
public class Trigger extends SpecialFeature{

    public static final String SPECIALFIELDLABEL = "sim_trigger";
    
    public static final String BOOLEAN_TYPE = "boolean";
    public static final String BUDGET_TYPE = "budget";
    public static final String INTEGER_TYPE = "integer";
    
    public String triggerType = BUDGET_TYPE;
    public String targetType = INTEGER_TYPE;
    
    public BooleanVariable triggerBoolVar = new BooleanVariable();    
    public BudgetVariable triggerBudVar = new BudgetVariable();
    public IntegerVariable triggerIntVar = new IntegerVariable();
    
    public BooleanVariable targetBoolVar = new BooleanVariable();    
    public BudgetVariable targetBudVar = new BudgetVariable();
    public IntegerVariable targetIntVar = new IntegerVariable();


    @Override
    public String getSpecialFieldLabel() {
        return SPECIALFIELDLABEL;
    }

    @Override
    public String getShortNameBase() {
        return "sim_trig_";
    }

    @Override
    public String store() {
        // TODO Auto-generated method stub
        return null;
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
