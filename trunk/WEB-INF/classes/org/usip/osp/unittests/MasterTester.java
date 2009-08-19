package org.usip.osp.unittests;

import org.usip.osp.baseobjects.*;
import org.usip.osp.specialfeatures.*;

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
public class MasterTester{
    
    public static Simulation testSim1 = new Simulation();
    public static Simulation testSim2 = new Simulation();
    public static Actor act_r = new Actor();
    public static Actor act_g = new Actor();
    public static Actor act_b = new Actor();
      
    
    //public static IntegerVariable iv1 = new IntegerVariable();
    //public static IntegerVariable iv2 = new IntegerVariable();
    
    public static BudgetVariable bd_v1 = new BudgetVariable();
    public static BudgetVariable bd_v2 = new BudgetVariable();
    
    public static PlayerControlBudgetTransfer pcbt1 = new PlayerControlBudgetTransfer();

    
    
    public static String runTests(){
        String runOut = "Run Started: <BR>"; //$NON-NLS-1$
        
        runOut += cleanDatabase();
        
        runOut += testSimCreation();
        
        runOut += testSimIntroCreation();
        
        runOut += testCreateSharedDocuments();
        
        runOut += testCreateIntegerVariable();
        
        runOut += testCreateChart();
        
        runOut += testCreateBudgetVariable();
        
        runOut += testCreatePlayerBudgetTransferControl();
        
        //runOut += testCreatePlayerControl();
        
        runOut += testSelectDefaultSections();
        
        runOut += testCreateActor();
        
        runOut += testSectionCreation();
        
        runOut += testAssignActor();
        
        runOut += testAssignSimVarSection();
        
        runOut += testAssignPlayerControlSection();
        
        runOut += testAssignPlayerBudgetTransferControlSection();
        
        runOut += testCreateUser();
        
        runOut += testCreateRunningSim();
        
        runOut += testAssignUserToSim();
        
        runOut += b + b + "Run Finished: "; //$NON-NLS-1$
        
        return runOut;
    }
    
    /**
     * Cleans the database. 
     * @return
     */
    public static String cleanDatabase(){
        String returnString = makeHeader("Cleaning Database - Not!"); //$NON-NLS-1$
        
        
        return returnString;
    }
    
    public static String makeHeader(String headerName){
        
        return(b + "------------------------" + b + g + headerName + ef ); //$NON-NLS-1$
    }
    
    public static final String b = "<BR>"; //$NON-NLS-1$
    public static final String r = "<font color=red>"; //$NON-NLS-1$
    public static final String g = "<font color=green>"; //$NON-NLS-1$
    public static final String ef = "</font>"; //$NON-NLS-1$
    
    public static String testSimCreation(){
        String returnString = makeHeader("Making Simulations 'Test1' and 'Test2'"); //$NON-NLS-1$
        
        testSim1.setName("Test1"); //$NON-NLS-1$
        testSim1.setVersion("1"); //$NON-NLS-1$
        //testGame1.layout.bannertitle = "Banner Heading Unit 1";
        //testGame1.store();
        
        returnString += b + "Created Simulation Test1 version 1"; //$NON-NLS-1$
        
        testSim2.setName("Test2"); //$NON-NLS-1$
        testSim2.setVersion("1"); //$NON-NLS-1$
        //testGame2.layout.bannertitle = "Banner Heading Unit 2";
        //testGame2.store();
        
        returnString += b + "Created Simulation Test2 version 1"; //$NON-NLS-1$
        
        return returnString;
    }
    
    public static String testSimIntroCreation(){
        String returnString = makeHeader("Inserting Simulation Introduction"); //$NON-NLS-1$
        
        //testGame1.setIntroductionInDB("These are the times that try men's souls.");

        returnString += b + "Simulation Introduction set (Paine quote)for Test version 1"; //$NON-NLS-1$
        
        return returnString;
    }
    
    public static String testCreateSharedDocuments(){
        String returnString = makeHeader("Creating Shared Document"); //$NON-NLS-1$
        

        returnString += b + "First Shared Document Created"; //$NON-NLS-1$
        
        return returnString;
    }
    
    public static String testCreateIntegerVariable(){
        String returnString = makeHeader("Creating Simulation Integer Variable"); //$NON-NLS-1$
        
        //iv1.game_id  = testGame1.getId();

        //iv1.propagation_type = "fibonacci"; //$NON-NLS-1$
        //iv1.initialValue = "1"; //$NON-NLS-1$
        
        //returnString += b + iv1.store();

        returnString += b + "First (Fibonacci) Integer Variable Created"; //$NON-NLS-1$
        
        //iv2.game_id  = testGame1.id;

        //iv2.propagation_type = "constant"; //$NON-NLS-1$
        //iv2.initialValue = "1"; //$NON-NLS-1$
        
        //returnString += b + iv2.store();

        returnString += b + "Second (Constant) Integer Variable Created"; //$NON-NLS-1$
        
        return returnString;
    }
    
    public static String testCreateChart(){
        String returnString = makeHeader("disabled Chart of Integer"); //$NON-NLS-1$
        
        /*
        chart1.title = "Rat Population Chart";
        chart1.type = "LineChart";
        
        chart1.game_id = testGame1.id;
        
        chart1.variableSFID = iv1.get_sf_id();
        
        chart1.xAxisTitle = "Round";
        chart1.yAxisTitle = "Rats";
        chart1.height = 247;
        chart1.width = 400;
        
        chart1.name = "Rat Population";
        
        returnString += b + chart1.store();
        
        returnString += b + "Chart1 Created";
        */
        return returnString;
    }

    
    public static String testCreateBudgetVariable(){
        String returnString = makeHeader("Creating Budget Variable"); //$NON-NLS-1$
        
        //bd_v1.game_id  = testGame1.id;
        //bd_v1.name = "Budget Name";
        //bd_v1.description = "Description of First Budget Variable.";
        bd_v1.value = "10000"; //$NON-NLS-1$
        bd_v1.accumulates = true;


        returnString += b + "First Budget Variable Created"; //$NON-NLS-1$
        

        //bd_v2.game_id  = testGame1.id;
        //bd_v2.name = "Budget Variable 2 Name";
        //bd_v2.description = "Description of Second Budget Variable.";
        bd_v1.value = "0"; //$NON-NLS-1$
        bd_v2.accumulates = false;
        //bd_v2.store();

        returnString += b + "Second Budget Variable Created"; //$NON-NLS-1$
        
        return returnString;
    }
    
    public static String testCreatePlayerBudgetTransferControl(){
        String returnString = makeHeader("Creating Player Budget Transfer Control"); //$NON-NLS-1$
        
        //pcbt1.game_id = testGame1.id;
        pcbt1.name = "Player Control"; //$NON-NLS-1$
        pcbt1.description = "Text describing this player control."; //$NON-NLS-1$
        
        //pcbt1.fromAcctString = bd_v1.get_sf_id();
        //pcbt1.toAcctString = bd_v2.get_sf_id();
        
        returnString += b + "Player Budget Transfer Control Created"; //$NON-NLS-1$
        
        return returnString;
    }
    

    
    public static String testSelectDefaultSections(){
        String returnString = makeHeader("Setting Default Sections"); //$NON-NLS-1$
        /*
        testGame1.listOfStdSectionIds.add("1");
        testGame1.listOfStdSectionIds.add("2");
        testGame1.listOfStdSectionIds.add("3");
        testGame1.listOfStdSectionIds.add("4");
        testGame1.listOfStdSectionIds.add("5");
        testGame1.listOfStdSectionIds.add("6");
        testGame1.listOfStdSectionIds.add("7");
        testGame1.listOfStdSectionIds.add("8");
        testGame1.listOfStdSectionIds.add("9");
        testGame1.listOfStdSectionIds.add("10");
        testGame1.listOfStdSectionIds.add("11");
        testGame1.listOfStdSectionIds.add("12");
        testGame1.listOfStdSectionIds.add("13");
        
        
        testGame1.updateGameStdSections();
        */
        returnString += b + "Assigned default sections to Test version 1"; //$NON-NLS-1$
        
        return returnString;
    }
    
    
    public static String testSectionCreation(){
        String returnString = makeHeader("Making Sections"); //$NON-NLS-1$
        
        SimulationSectionAssignment gsb = new SimulationSectionAssignment();
        
        //gsb.section_short_name = "funding";
        gsb.setTab_heading("Funding"); //$NON-NLS-1$
        //gsb.page_file_name = "funding.jsp";

        //gsb.storeBaseSection();
        
        returnString += b + "saved funding section"; //$NON-NLS-1$
        
        return returnString;
    }
    
    /**
     * 
     * @return
     */
    public static String testCreateActor(){
        String returnString = makeHeader("Making Actor not working"); //$NON-NLS-1$
        
        // TODO
        /*
        act_r.name = "Mr. Red";
        act_r.public_description = "Mr. Red's public description.";
        act_r.semi_public_description = "Mr. Red's semi-public description.";
        act_r.private_description = "Mr. Red's private description.";
        act_r.imageFilename = "mr_red.png";
        
        act_r.store();
        returnString += b + "created actor Mr. Red";
        
        act_g.name = "Mr. Green";
        act_g.public_description = "Mr. Green's public description.";
        act_g.semi_public_description = "Mr. Green's semi-public description.";
        act_g.private_description = "Mr. Green's private description.";
        act_g.imageFilename = "mr_green.png";
        
        act_g.store();
        returnString += b + "created actor Mr. Green";
        
        act_b.name = "Mr. Blue";
        act_b.public_description = "Mr. Blue's public description.";
        act_b.semi_public_description = "Mr. Blue's semi-public description.";
        act_b.private_description = "Mr. Blue's private description.";
        act_b.imageFilename = "mr_blue.png";
        
        act_b.store();
        returnString += b + "created actor Mr. Blue";
        */
        return returnString;
    }
    
    public static String testAssignActor(){
        
        String returnString = makeHeader("Assigning Actor"); //$NON-NLS-1$
        
        /*
        testGame1.addActor(act_r.id);
        testGame1.addActor(act_g.id);
        
        testGame2.addActor(act_g.id);
        testGame2.addActor(act_b.id);
        */
        
        returnString += b + "assigned actor a"; //$NON-NLS-1$
        
        return returnString;
    }
    
    public static String testAssignSharedDocSection(){
        
        String returnString = makeHeader("Assigning sim variable chart to user."); //$NON-NLS-1$
        //SimulationSectionAssignment gs = new SimulationSectionAssignment();
        
        // TODO
        
        //gs.game_id = testGame1.id;
        //gs.actor_id = act_r.id;
        //gs.game_status = "in progress";
        //gs.section_short_name = iv1.name;
        //gs.setTab_heading(iv1.name);
        //gs.tab_position = "6";
        //gs.directory = "/osp_core/";
        //gs.page_file_name = "show_variable.jsp";
            
        //returnString += " : " + gs.store();
        
        return returnString;
        
    }
    
    public static String testAssignSimVarSection(){
        
        String returnString = makeHeader("Disabled Assigning sim variable chart to user."); //$NON-NLS-1$
        /*
        
        chart1.gs.game_id = testGame1.id;
        chart1.gs.actor_id = act_r.id;
        chart1.gs.game_status = "in progress";
        chart1.gs.section_short_name = "Rats";
        chart1.gs.section_long_name = chart1.name;
        //gs.tab_position = iv1.gs.tab_position;
        chart1.gs.tab_position = "6";
        chart1.gs.directory = "/osp_core/";
            
        returnString += " : " + chart1.gs.store();
        */
        return returnString;
        
        
    }
    
    public static String testAssignPlayerControlSection(){
        
        String returnString = makeHeader("Assigning Player Control to user."); //$NON-NLS-1$
        
        // TODO
        
        SimulationSectionAssignment gs_base = null;
        //GameSection.getBase(pc1.gs.id);
        
        SimulationSectionAssignment gs = new SimulationSectionAssignment();
        
            
        //returnString += " : " + gs.store();
        
        return returnString;
        
    }
    
    public static String testAssignPlayerBudgetTransferControlSection(){
        
        String returnString = makeHeader("Assigning Player Budget Transfer Control to user."); //$NON-NLS-1$
        
        // TODO
        SimulationSectionAssignment gs_base = null;
        //GameSection.getBase(pcbt1.gs.id);
        
        SimulationSectionAssignment gs = new SimulationSectionAssignment();
        
        //gs.game_id = testGame1.id;
        // TODO
        //gs.actor_id = act_r.id;
        //gs.game_status = "in progress";
        //gs.section_short_name = pcbt1.gs.section_short_name;
        gs.setTab_heading(pcbt1.gs.getTab_heading());
        //gs.tab_position = "6"; //pcbt1.gs.tab_position;
        //gs.directory = "/osp_core/";
        //gs.page_file_name = pcbt1.jsp_page;
            
        //returnString += " : " + gs.store();
        
        return returnString;
        
    }
    
    public static String testCreateUser(){
        String returnString = makeHeader("Making User"); //$NON-NLS-1$
        /*
        userA.setUsername("a");
        //userA.setPassword("a");
        //userA.setFull_name("Mr. A.");
        userA.setEmail("scole@usip.org");

        //userA.store();
        
        returnString += b + "created user a";
        
        userB.setUsername("b");
        //userB.setPassword("b");
        //userB.setFull_name("Mr. B.");
        userB.setEmail("scole@usip.org");

        //userB.store();
        
        returnString += b + "created user b";
        
        userC.setUsername("c");
        //userC.setPassword("c");
        //userC.setFull_name("Mr. C.");
        userC.setEmail("scole@usip.org");
		*/
        //userC.store();
        
        returnString += b + "created user c"; //$NON-NLS-1$
        
        return returnString;
    }
    
    public static String testCreateRunningSim(){
        String returnString = makeHeader("Creating Running Simulation"); //$NON-NLS-1$
        
 
        //returnString += b + "Error out: " + rg.store();
        
        returnString += b + "created running simulation rg"; //$NON-NLS-1$
        
        return returnString;
    }
    
    public static String testAssignUserToSim(){
        String returnString = makeHeader("Assigning User to Simulation"); //$NON-NLS-1$
        
        // TODO
        //returnString += b + User.assignUserToGame(testGame1.id, rg.id, act_r.id, userA.getId() + "");
        
        returnString += b + "Assigned user to running simulation"; //$NON-NLS-1$
        
        return returnString;
    }
    
    

}
