package org.usip.osp.unittests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.usip.osp.baseobjects.*;
import org.usip.osp.persistence.DatabaseCreator;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
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
public class MasterTester {

	public static Simulation testSim1 = new Simulation();

	public static void main(String args[]) {

		System.out.println("hello");


			MasterTester mt = new MasterTester();
			mt.createDB();

	}

	@Test
	public void createDB() {
		// Create Database
		// Need to clean out test schema, then unpackage there. If the sim
		// begins in a clean schema, and then exports to the test schema, many
		// little things
		// like actor_ids will map automatically. This will let us do a good
		// comparison.
		DatabaseCreator.createOrCleanUnitTestSchema("true", new Long(
				1));
	}

	@Test
	public void testCalculate() {
		assert (true);

	}

}
