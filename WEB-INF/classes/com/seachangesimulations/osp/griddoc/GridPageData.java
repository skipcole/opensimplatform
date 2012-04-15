package com.seachangesimulations.osp.griddoc;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.SimulationSectionAssignment;
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
	
	/** True if this has a fixed set of columns to display. */
	private boolean fixedCols = false;

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

	public boolean isFixedCols() {
		return fixedCols;
	}

	public void setFixedCols(boolean fixedCols) {
		this.fixedCols = fixedCols;
	}

	/**
	 * Looks at a section and determines if it has fixed columns and has been initialized. 
	 * If it has fixed columns, but has not been initialized, it creates the initials columns.
	 * @param request
	 * @param cs
	 * @param schema
	 * @param simId
	 * @param rsId
	 */
	public static void initialize(HttpServletRequest request,
			CustomizeableSection cs, String schema, Long simId, Long rsId) {
		
		if ((hasFixedColumns(cs)) && (simId != null) && (rsId != null)) {
			
			// Check to see if column headings created
			int topRow = 0;
			List cols = getCellItemsForRow(schema, simId, cs.getId(), rsId,
					new Long(topRow));

			if ((cols == null) || (cols.size() == 0)) {
				
				// Create column headings, if not found.
				String setOfColumHeadings = (String) cs.getContents().get(
						GridDocCustomizer.KEY_FOR_FIXED_COL_NAMES);

				StringTokenizer str = new StringTokenizer(setOfColumHeadings,
						",");

				int colNumber = 1;
				while (str.hasMoreTokens()) {
					// create column
					String col_name = str.nextToken().trim();
					
					if (col_name.length() > 0) {
						GridData newGD = new GridData();
						// column name, 
						newGD.setCsId(cs.getId());
						newGD.setSimId(simId);
						newGD.setRsId(rsId);
						newGD.setRowNum(topRow);
						newGD.setColNum(colNumber);
						newGD.setCellData(col_name);
						newGD.saveMe(schema);
						
						++colNumber;
					}

				}
			}
		}
	}

	/**
	 * Determines if Grid Page has fixed columns. Used to determine wether or not to show the 
	 * delete column link.
	 * 
	 * @return
	 */
	public static boolean hasFixedColumns(CustomizeableSection cs) {

		String fixedCol = (String) cs.getContents().get(
				GridDocCustomizer.KEY_FOR_FIXED_COL);

		if ((fixedCol != null) && (fixedCol.equalsIgnoreCase("yes"))) {
			return true;
		}

		return false;

	}

	/**
	 * 
	 * @param request
	 * @param cs
	 */
	public static void handleChanges(HttpServletRequest request,
			CustomizeableSection cs, String schema, Long simId, Long rsId) {
		
		// Check to see if this is a fixed grid and create necessary objects.
		initialize(request, cs, schema, simId, rsId);

		String do_add_col = (String) request.getParameter("do_add_col");
		String do_add_row = (String) request.getParameter("do_add_row");

		GridPageData gpd = new GridPageData();
		GridData newGD = new GridData();

		if ((do_add_col != null) || (do_add_row != null)) {
			newGD.setCsId(cs.getId());
			newGD.setSimId(simId);
			newGD.setRsId(rsId);
		}

		// Adding Column
		if (do_add_col != null) {

			String col_name = (String) request.getParameter("col_name");

			if ((col_name != null) && (col_name.trim().length() > 0)) {

				// Load in number of cols
				gpd.loadColumnNames(schema, cs, simId, rsId);

				newGD.setRowNum(0);
				newGD.setColNum(gpd.getNumCols() + 1);
				newGD.setCellData(col_name);
				newGD.saveMe(schema);
			}

		}

		// Adding row
		if (do_add_row != null) {

			String row_name = (String) request.getParameter("row_name");

			if ((row_name != null) && (row_name.trim().length() > 0)) {
				// Load in row names
				gpd.loadRowNames(schema, cs, simId, rsId);

				newGD.setRowNum(gpd.getNumRows() + 1);
				newGD.setColNum(0);
				newGD.setCellData(row_name);
				newGD.saveMe(schema);
			}

		}

		// Deleting Column
		String del_col = (String) request.getParameter("del_col");
		if (del_col != null) {
			String col = (String) request.getParameter("col");

			List objectsToDelete = getCellItemsForColumn(schema, simId,
					cs.getId(), rsId, new Long(col));

			for (ListIterator<GridData> li = objectsToDelete.listIterator(); li
					.hasNext();) {
				GridData dataToDelete = li.next();
				GridData.deleteGridData(schema, dataToDelete);
			}

		}

		// Deleting Row
		String del_row = (String) request.getParameter("del_row");
		if (del_row != null) {
			String row = (String) request.getParameter("row");

			List objectsToDelete = getCellItemsForRow(schema, simId,
					cs.getId(), rsId, new Long(row));

			for (ListIterator<GridData> li = objectsToDelete.listIterator(); li
					.hasNext();) {
				GridData dataToDelete = li.next();
				GridData.deleteGridData(schema, dataToDelete);
			}

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
		
		gpd.setFixedCols(hasFixedColumns(cs));

		// Load in number of cols
		gpd.loadColumnNames(schema, cs, simId, rsId);

		// Load in number of rows
		gpd.loadRowNames(schema, cs, simId, rsId);

		return gpd;
	}

	/**
	 * Loads the data into the col
	 * 
	 * @param schema
	 * @param cs
	 * @param simId
	 * @param rsId
	 */
	public void loadColumnNames(String schema, CustomizeableSection cs,
			Long simId, Long rsId) {

		int topRow = 0;
		
		// Load in number of cols
		List cols = getCellItemsForRow(schema, simId, cs.getId(), rsId,
				new Long(topRow));

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

	/**
	 * 
	 * @param schema
	 * @param cs
	 * @param simId
	 * @param rsId
	 */
	public void loadRowNames(String schema, CustomizeableSection cs,
			Long simId, Long rsId) {

		List rows = getCellItemsForColumn(schema, simId, cs.getId(), rsId,
				new Long(0));

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
	public static List getCellItemsForRow(String schema, Long simId, Long csId,
			Long rsId, Long rowNumber) {

		if ((rsId == null) || (simId == null) || (csId == null)) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from GridData where simId = :simId and csId = :csId and rsId = :rsId and rowNum = :rowNumber") //$NON-NLS-1$
				.setLong("simId", simId).setLong("csId", csId)
				.setLong("rowNumber", rowNumber).setLong("rsId", rsId).list();

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
	public static List<GridData> getCellItemsForColumn(String schema,
			Long simId, Long csId, Long rsId, Long colNumber) {

		if ((rsId == null) || (simId == null) || (csId == null)) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from GridData where simId = :simId and csId = :csId and rsId = :rsId and colNum = :colNumber") //$NON-NLS-1$
				.setLong("simId", simId).setLong("csId", csId)
				.setLong("colNumber", colNumber).setLong("rsId", rsId).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

}
