package org.usip.osp.plugins.griddoc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
/**
 * 
 */
/*
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
public class GridPageData {

	private int numCols = 0;
	
	private int numRows = 0;
	
	private String pageTitle = "";
	
	private String pageDescription = "";
	
	private String rowDesignator = "";
	
	private String colDesignator = "";
	
	private Hashtable cellData = new Hashtable();
	
	public int getNumCols() {
		return numCols;
	}


	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}


	public int getNumRows() {
		return numRows;
	}


	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}


	public String getPageTitle() {
		return pageTitle;
	}


	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}


	public String getPageDescription() {
		return pageDescription;
	}


	public void setPageDescription(String pageDescription) {
		this.pageDescription = pageDescription;
	}


	public String getRowDesignator() {
		return rowDesignator;
	}


	public void setRowDesignator(String rowDesignator) {
		this.rowDesignator = rowDesignator;
	}


	public String getColDesignator() {
		return colDesignator;
	}


	public void setColDesignator(String colDesignator) {
		this.colDesignator = colDesignator;
	}


	public static GridPageData loadPage(String schema, CustomizeableSection cs, Long simId, Long rsId){

		GridPageData gpd = new GridPageData();
		
		// Load in number of cols
		
		// Load in number of rows
		gpd.setNumRows(getNumRowsFromData(schema, simId, cs.getId(), rsId));
		
		return gpd;
	}
	
	public static int getNumRowsFromData(String schema, Long simId, Long csId, Long rsId){
		
		//Select all rows where col = 0;
		List rows = getRows(schema, simId, csId, rsId);
		
		// Get highest row number
		
		return 0;
	}
	
	/**
	 * Returns rows where col equals 0.
	 * 
	 * @param schema
	 * @param simId
	 * @param csId
	 * @param rsId
	 * @return
	 */
	public static List getRows(String schema, Long simId, Long csId, Long rsId){
		
		if ((rsId == null) || (simId == null) || (csId == null)){
			return new ArrayList();
		}
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from GridData where simId = :simId and csId = :csId and rsId = :rsId and colNum = 0") //$NON-NLS-1$
				.setLong("simId", simId)
				.setLong("csId", csId)
				.setLong("rsId", rsId)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	
	
}
