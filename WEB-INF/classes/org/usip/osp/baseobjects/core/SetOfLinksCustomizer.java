package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.networking.SessionObjectBase;

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
public class SetOfLinksCustomizer extends Customizer {
	
	/** Key to pull id out if stored in Hashtable */
	public static final String SETOFLINKSKEY = "setoflinks_key"; //$NON-NLS-1$
	
	private Long solId = null;
	
	public Long getSolId() {
		return solId;
	}

	public void setSolId(Long solId) {
		this.solId = solId;
	}

	public SetOfLinksCustomizer(){
		
	}
	
	 public SetOfLinksCustomizer(HttpServletRequest request, SessionObjectBase pso,
				CustomizeableSection cs){
			 
			 loadSimCustomizeSection(request, pso, cs);
			 
		 }
	

	@Override
	public void handleCustomizeSection(HttpServletRequest request, SessionObjectBase afso, CustomizeableSection cs) {

		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$

			String sol_id = request.getParameter("sol_id");
			
			Long thisSOLId = USIP_OSP_Util.stringToLong(sol_id);
		
			cs.getContents().put(SETOFLINKSKEY, thisSOLId);
			
			// Need to create Base Sim Section Dependent Objects to signal these objects (params and docs)
			// are needed for this, section.
			BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, cs.getId());
			
			@SuppressWarnings("unused")
			BaseSimSectionDepObjectAssignment bssdoa_gv = new BaseSimSectionDepObjectAssignment(
					cs.getId(), "org.usip.osp.specialfeatures.SetOfLinks", 1, thisSOLId, afso.sim_id, afso.schema);
		}
		
	}

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {
		solId = (Long) cs.getContents().get(SETOFLINKSKEY);
		
	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs) {

		solId = (Long) cs.getContents().get(SETOFLINKSKEY);
		
	}

}
