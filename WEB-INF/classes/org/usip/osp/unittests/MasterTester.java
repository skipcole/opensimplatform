package org.usip.osp.unittests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.usip.osp.baseobjects.*;
import org.usip.osp.specialfeatures.*;

import org.junit.*;

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
public class MasterTester{
    
    public static Simulation testSim1 = new Simulation();

    public static void main(String args[]){
    	
    	MasterTester mt = new MasterTester();
    	mt.testCalculate();
    	
    	// Create Database
    	
    	
    }
    
	@Test
	public void testCalculate() {
		assert(true);
		
	}
    


}
