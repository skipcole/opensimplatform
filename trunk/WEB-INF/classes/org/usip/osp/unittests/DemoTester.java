package org.usip.osp.unittests;

import org.usip.osp.baseobjects.*;

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
public class DemoTester extends BaseTester{

    public static Simulation testDemoGame = new Simulation();

    public static Actor act_pres = new Actor();
    public static Actor act_legistator = new Actor();
    public static Actor act_business_owner = new Actor();
    public static Actor act_control = new Actor();
     
    
    public static String runTests(){
        String runOut = "Run Started: <BR>";
  
        runOut += cleanDatabase();
        runOut += testSimCreation();
        
        return runOut;
        
    }
    
    public static String testSimCreation(){
        String returnString = makeHeader("Making Simulations 'Test1' and 'Test2'");
        
        //testDemoGame.name = "One For All";
        //testDemoGame.version = "1";
        //testDemoGame.layout.bannertitle = "My Organization";
        //testDemoGame.store();
        
        returnString += b + "Created Demo Simulation";
        
        
        return returnString;
    }
    
    public static String testSimIntroCreation(){
        String returnString = makeHeader("Inserting Simulation Introduction");
        
        //testDemoGame.setIntroductionInDB("A Sample Sim.");

        returnString += b + "Simulation Introduction Set";
        
        return returnString;
    }
}
