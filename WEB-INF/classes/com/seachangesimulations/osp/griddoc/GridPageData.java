package com.seachangesimulations.osp.griddoc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * GridPageData is used to summarize data pulled from GridData points.
 */
/*
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 */
public class GridPageData {

	private int numCols = 0;

	private int numRows = 0;

	private ArrayList<String> rowNames = new ArrayList<String>();

	private ArrayList<String> colNames = new ArrayList<String>();

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

	public ArrayList<String> getRowNames() {
		return rowNames;
	}

	public void setRowNames(ArrayList<String> rowNames) {
		this.rowNames = rowNames;
	}

	public ArrayList<String> getColNames() {
		return colNames;
	}

	public void setColNames(ArrayList<String> colNames) {
		this.colNames = colNames;
	}

	/**
	 * 
	 * @param request
	 * @param cs
	 */
	public static void handleChanges(HttpServletRequest request,
			CustomizeableSection cs, String schema, Long simId, Long rsId) {

		String do_add_col = (String) request.getParameter("do_add_col");
		String do_add_row = (String) request.getParameter("do_add_row");

		GridPageData gpd = new GridPageData();
		GridData newGD = new GridData();

		if ((do_add_col != null) || (do_add_row != null)) {
			newGD.setCsId(cs.getId());
			newGD.setSimId(simId);
			newGD.setRsId(rsId);
		}

		if (do_add_col != null) {

			String col_name = (String) request.getParameter("col_name");

			System.out.println("adding col: " + col_name);

			// Load in number of cols
			gpd.loadColumns(schema, cs, simId, rsId);

			newGD.setRowNum(0);
			newGD.setColNum(gpd.getNumCols() + 1);
			newGD.setCellData(col_name);
			newGD.saveMe(schema);

		}

		if (do_add_row != null) {

			String row_name = (String) request.getParameter("row_name");

			System.out.println("adding row: " + row_name);

			// Load in number of cols
			gpd.loadRows(schema, cs, simId, rsId);

			newGD.setRowNum(gpd.getNumRows() + 1);
			newGD.setColNum(0);
			newGD.setCellData(row_name);
			newGD.saveMe(schema);

		}

	}

	/**
	 * 
	 * @param schema
	 * @param cs
	 * @param simId
	 * @param rsId
	 * @return
	 */
	public static GridPageData loadPage(String schema, CustomizeableSection cs,
			Long simId, Long rsId) {

		GridPageData gpd = new GridPageData();

		// Load in number of cols
		gpd.loadColumns(schema, cs, simId, rsId);

		// Load in number of rows
		gpd.loadRows(schema, cs, simId, rsId);

		return gpd;
	}

	public void loadColumns(String schema, CustomizeableSection cs, Long simId,
			Long rsId) {

		// Load in number of cols
		List cols = getBaseCols(schema, simId, cs.getId(), rsId);

		if (cols == null) {
			this.setNumCols(0);
			this.setColNames(new ArrayList<String>());
		} else {
			this.setNumCols(cols.size());

			ArrayList<String> newList = new ArrayList<String>();
			// loop over and pull out names
			for (ListIterator li = cols.listIterator(); li.hasNext();) {
				GridData gd = (GridData) li.next();

				newList.add(gd.getCellData());
			}
			this.setColNames(newList);
		}
	}

	public void loadRows(String schema, CustomizeableSection cs, Long simId,
			Long rsId) {

		List rows = getBaseRows(schema, simId, cs.getId(), rsId);
		if (rows == null) {
			this.setNumRows(0);
			this.setRowNames(new ArrayList<String>());
		} else {
			this.setNumRows(rows.size());

			ArrayList<String> newList = new ArrayList<String>();
			// loop over and pull out names
			for (ListIterator li = rows.listIterator(); li.hasNext();) {
				GridData gd = (GridData) li.next();

				newList.add(gd.getCellData());
			}
			this.setRowNames(newList);
		}

	}



	/**
	 * Returns rows where col equals 0. (Which contains the row names.)
	 * 
	 * @param schema
	 * @param simId
	 * @param csId
	 * @param rsId
	 * @return
	 */
	public static List getBaseRows(String schema, Long simId, Long csId,
			Long rsId) {

		if ((rsId == null) || (simId == null) || (csId == null)) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from GridData where simId = :simId and csId = :csId and rsId = :rsId and colNum = 0") //$NON-NLS-1$
				.setLong("simId", simId).setLong("csId", csId)
				.setLong("rsId", rsId).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Returns cols where row equals 0. (Which contains the column names.)
	 * 
	 * @param schema
	 * @param simId
	 * @param csId
	 * @param rsId
	 * @return
	 */
	public static List getBaseCols(String schema, Long simId, Long csId,
			Long rsId) {

		if ((rsId == null) || (simId == null) || (csId == null)) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from GridData where simId = :simId and csId = :csId and rsId = :rsId and rowNum = 0") //$NON-NLS-1$
				.setLong("simId", simId).setLong("csId", csId)
				.setLong("rsId", rsId).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

}
