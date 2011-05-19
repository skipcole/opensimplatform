package org.usip.osp.plugins.griddoc;

import java.util.Hashtable;

import org.usip.osp.baseobjects.CustomizeableSection;
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


	public static GridPageData loadPage(CustomizeableSection cs){

		GridPageData gpd = new GridPageData();
		cs.getContents().get(GridDocCustomizer.KEY_FOR_NEW_COLUMN);
		
		return null;
	}
	
	
	
}
