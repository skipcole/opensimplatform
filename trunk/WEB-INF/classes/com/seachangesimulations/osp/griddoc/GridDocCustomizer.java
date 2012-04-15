package com.seachangesimulations.osp.griddoc;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.core.Customizer;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.networking.SessionObjectBase;
import org.apache.log4j.*;


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
public class GridDocCustomizer extends Customizer{

	public static final String KEY_FOR_PAGETITLE = "griddoc_page_title"; //$NON-NLS-1$
	public static final String KEY_FOR_NEW_COLUMN = "grid_doc_new_col"; //$NON-NLS-1$
	public static final String KEY_FOR_NEW_ROW = "grid_doc_new_row"; //$NON-NLS-1$
	
	public static final String KEY_FOR_FIXED_COL = "grid_doc_fixed_col"; //$NON-NLS-1$
	public static final String KEY_FOR_FIXED_COL_NAMES = "grid_doc_fixed_col_names"; //$NON-NLS-1$
	public static final String KEY_FOR_COLOR_CHANGEABLE = "grid_doc_color_changeable"; //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	public void handleCustomizeSection(HttpServletRequest request, 
			SessionObjectBase afso, CustomizeableSection cs) {

		Logger.getRootLogger().debug("GridDocCustomizer.handleCustomizeSection"); //$NON-NLS-1$
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			
			Logger.getRootLogger().debug("GridDocCustomizer.handleCustomizeSection.save_results"); //$NON-NLS-1$

			cs.setBigString(request.getParameter("cs_bigstring"));
			
			cs.getContents().put(KEY_FOR_PAGETITLE, request.getParameter(KEY_FOR_PAGETITLE));
			
			cs.getContents().put(KEY_FOR_NEW_COLUMN, request.getParameter(KEY_FOR_NEW_COLUMN));
			
			cs.getContents().put(KEY_FOR_NEW_ROW, request.getParameter(KEY_FOR_NEW_ROW));
			
			////////////////////
			cs.getContents().put(KEY_FOR_FIXED_COL, request.getParameter(KEY_FOR_FIXED_COL));
			cs.getContents().put(KEY_FOR_FIXED_COL_NAMES, request.getParameter(KEY_FOR_FIXED_COL_NAMES));
			cs.getContents().put(KEY_FOR_COLOR_CHANGEABLE, request.getParameter(KEY_FOR_COLOR_CHANGEABLE));

			cs.saveMe(afso.schema);

		}

	}

	public void loadSimCustomizeSection(HttpServletRequest request, 
			SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

	
	public static CustomizeableSection getGridSection(HttpServletRequest request,
			PlayerSessionObject pso){

		String cs_id = (String) request.getParameter("cs_id");
		CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
		
		int numCols = 0;
		int numRows = 0;
		
		Hashtable contents = cs.getContents();
		
		// Get num cols/rows from hashtable
		String myNumCols = (String) contents.get("myNumCols_" + pso.getRunningSimId());
		String myNumRows = (String) contents.get("myNumRows_" + pso.getRunningSimId());
		
		if (myNumCols == null){
			contents.put("myNumCols_" + pso.getRunningSimId(), "0");
			cs.saveMe(pso.schema);
		} else {
			numCols = new Long(myNumCols).intValue();
		}
		
		/////////////////////////////
		if (myNumRows == null){
			contents.put("myNumRows_" + pso.getRunningSimId(), "0");
			cs.saveMe(pso.schema);
		} else {
			numRows = new Long(myNumRows).intValue();
		}
		
		
		String do_add_col = (String) request.getParameter("do_add_col");
		String do_add_row = (String) request.getParameter("do_add_row");
		
		if (do_add_col != null) {
			numCols += 1;	
			contents.put("myNumCols_" + pso.getRunningSimId(), numCols + "");
			
			String col_name = (String) request.getParameter("col_name");
			contents.put("colname_" + pso.getRunningSimId() + "_" + numCols, col_name);
			
			cs.saveMe(pso.schema);
		}
		
		if (do_add_row != null) {;
			numRows += 1;	
			contents.put("myNumRows_" + pso.getRunningSimId(), numRows + "");
			
			String row_name = (String) request.getParameter("row_name");
			contents.put("rowname_" + pso.getRunningSimId() + "_" + numRows, row_name);		
			cs.saveMe(pso.schema);
		}
		
		
		String del_col = (String) request.getParameter("del_col");
		if (del_col != null) {
			String col = (String) request.getParameter("col");
			
			numCols -= 1;	
			contents.put("myNumCols_" + pso.getRunningSimId(), numCols + "");
			
			// Loop over keys
			for (Enumeration e = contents.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String value = (String) cs.getContents().get(key);
				
				if (key.startsWith("rowData_" + pso.getRunningSimId() + "_" + col)){
					contents.remove(key);
				}
			}
			
			cs.saveMe(pso.schema);
		
		} // end of delete column
		
		String del_row = (String) request.getParameter("del_row");
		if (del_row != null) {
			String row = (String) request.getParameter("row");
			
			numRows -= 1;	
			contents.put("myNumRows_" + pso.getRunningSimId(), numRows + "");
			
			// Loop over keys
			for (Enumeration e = contents.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String value = (String) cs.getContents().get(key);
				
				if (
				
				(key.startsWith("rowData_" + pso.getRunningSimId() + "_" )) && (key.endsWith("_" + row))
				
				){
					contents.remove(key);
				}
			}
			
			cs.saveMe(pso.schema);
		
		} // End of Delete Row
		
		return cs;
	}
	

}
