package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
public class BishopsCustomizer  extends Customizer{
	
	public static final String ALLOW_EDIT_KEY = "ALLOW_EDIT_KEY"; //$NON-NLS-1$
	
	public static final String ALLOW_CONFLICTDOC_EDIT_KEY = "ALLOW_CONFLICTDOC_EDIT_KEY"; //$NON-NLS-1$
	
	public static final String GET_PARAMETER_KEY = "GET_PARAMETER_KEY"; //$NON-NLS-1$
	
	public static final String GET_CONFLICT_DOC1_KEY = "GET_CONFLICT_DOC1_KEY"; //$NON-NLS-1$
	
	public static final String GET_CONFLICT_DOC2_KEY = "GET_CONFLICT_DOC2_KEY"; //$NON-NLS-1$
	
	public static final String GET_DOC_TO_SHOW_KEY = "GET_DOC_TO_SHOW_KEY"; //$NON-NLS-1$
	
	 public BishopsCustomizer(){}
	 
	 /** Creates and loads the info.
	  * 
	  * @param request
	  * @param pso
	  * @param cs
	  */
	 public BishopsCustomizer(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs){
		 
		 loadSimCustomizeSection(request, pso, cs);
		 
	 }
	 

	@Override
	public void handleCustomizeSection(HttpServletRequest request, SessionObjectBase afso,
			CustomizeableSection cs) {
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			
			Logger.getRootLogger().debug("CastCustomizeableSection.handleCustomizeSection.save_results"); //$NON-NLS-1$

			cs.setBigString(request.getParameter("cs_bigstring"));
			
			String selected_allow_edit = request.getParameter("selected_allow_edit");
			String selected_allow_cd_edit = request.getParameter("selected_allow_cd_edit");
			String gv_id = request.getParameter("gv_id");
			String conflict_doc_1 = request.getParameter("conflict_doc_1");
			String conflict_doc_2 = request.getParameter("conflict_doc_2");
			String doc_to_show = request.getParameter("doc_to_show");
			
			
			if ((selected_allow_edit != null) && (selected_allow_edit.equalsIgnoreCase("true"))) {
				cs.getContents().put(ALLOW_EDIT_KEY, selected_allow_edit);
			} else {
				cs.getContents().put(ALLOW_EDIT_KEY, "false");
			}
			
			if ((selected_allow_cd_edit != null) && (selected_allow_cd_edit.equalsIgnoreCase("true"))) {
				cs.getContents().put(ALLOW_CONFLICTDOC_EDIT_KEY, selected_allow_cd_edit);
			} else {
				cs.getContents().put(ALLOW_CONFLICTDOC_EDIT_KEY, "false");
			}
			
			// TODO. This should not be Null!  If they are, we should provide feedback to the user.
			Long gvId = USIP_OSP_Util.stringToLong(gv_id);
			Long cd1Id = USIP_OSP_Util.stringToLong(conflict_doc_1);
			Long cd2Id = USIP_OSP_Util.stringToLong(conflict_doc_2);
			Long d2show = USIP_OSP_Util.stringToLong(doc_to_show);
			
			cs.getContents().put(GET_PARAMETER_KEY, gvId);
			cs.getContents().put(GET_CONFLICT_DOC1_KEY, cd1Id);
			cs.getContents().put(GET_CONFLICT_DOC2_KEY, cd2Id);
			cs.getContents().put(GET_DOC_TO_SHOW_KEY, d2show);

			cs.saveMe(afso.schema);
			
			// Need to create Base Sim Section Dependent Objects to signal these objects (params and docs)
			// are needed for this, section.
			BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, cs.getId());
			
			@SuppressWarnings("unused")
			BaseSimSectionDepObjectAssignment bssdoa_gv = new BaseSimSectionDepObjectAssignment(
					cs.getId(), "org.usip.osp.specialfeatures.GenericVariable", 1, gvId, afso.sim_id, afso.schema);
			
			@SuppressWarnings("unused")
			BaseSimSectionDepObjectAssignment bssdoa_cd1 = new BaseSimSectionDepObjectAssignment(
					cs.getId(), "org.usip.osp.communications.SharedDocument", 2, cd1Id, afso.sim_id, afso.schema);
			
			@SuppressWarnings("unused")
			BaseSimSectionDepObjectAssignment bssdoa_cd2 = new BaseSimSectionDepObjectAssignment(
					cs.getId(), "org.usip.osp.communications.SharedDocument", 3, cd2Id, afso.sim_id, afso.schema);
			

		}
		
	}
	
	

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs) {
		
		String ae = (String) cs.getContents().get(ALLOW_EDIT_KEY);
		
		if ((ae != null) && (ae.equalsIgnoreCase("true"))){
			allowEdit = true;
		} else {
			allowEdit = false;
		}
		///////////////////////////
		String aeCD = (String) cs.getContents().get(ALLOW_CONFLICTDOC_EDIT_KEY);
		
		if ((aeCD != null) && (aeCD.equalsIgnoreCase("true"))){
			allowConflictDocumentEdit = true;
		} else {
			allowConflictDocumentEdit = false;
		}
		
		parameterId = (Long) cs.getContents().get(GET_PARAMETER_KEY);
		doc1Id = (Long) cs.getContents().get(GET_CONFLICT_DOC1_KEY);
		doc2Id = (Long) cs.getContents().get(GET_CONFLICT_DOC2_KEY);
		Long documentToShowRaw = (Long) cs.getContents().get(GET_CONFLICT_DOC2_KEY);
		
		if (documentToShowRaw != null){
			documentToShow = documentToShowRaw.intValue();
		}
		
	}
	
	private boolean allowEdit = false;
	private boolean allowConflictDocumentEdit = false;
	private Long parameterId = null;
	private Long doc1Id = null;
	private Long doc2Id = null;
	private int documentToShow = 1;

	public boolean isAllowEdit() {
		return allowEdit;
	}

	public void setAllowEdit(boolean allowEdit) {
		this.allowEdit = allowEdit;
	}

	public boolean isAllowConflictDocumentEdit() {
		return allowConflictDocumentEdit;
	}

	public void setAllowConflictDocumentEdit(boolean allowConflictDocumentEdit) {
		this.allowConflictDocumentEdit = allowConflictDocumentEdit;
	}

	public Long getParameterId() {
		return parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	public Long getDoc1Id() {
		return doc1Id;
	}

	public void setDoc1Id(Long doc1Id) {
		this.doc1Id = doc1Id;
	}

	public Long getDoc2Id() {
		return doc2Id;
	}

	public void setDoc2Id(Long doc2Id) {
		this.doc2Id = doc2Id;
	}

	public void setDocumentToShow(int documentToShow) {
		this.documentToShow = documentToShow;
	}

	public int getDocumentToShow() {
		return documentToShow;
	}
	
	
	

}
