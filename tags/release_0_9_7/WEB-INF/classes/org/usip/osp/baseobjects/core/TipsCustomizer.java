package org.usip.osp.baseobjects.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.communications.Tips;
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
public class TipsCustomizer extends Customizer {

	public static final String KEY_FOR_UNIQUE_TIP = "unique_tip"; //$NON-NLS-1$

	public static final String KEY_FOR_CAN_LEAVE_TIPS = "can_leave_tip"; //$NON-NLS-1$

	private Tips tip;
	
	private CustomizeableSection cs;
	
	public TipsCustomizer(){}

	public TipsCustomizer(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs) {
		
		this.cs = cs;

		loadSimCustomizeSection(request, pso, cs);

	}

	@SuppressWarnings("unchecked")
	public void handleCustomizeSection(HttpServletRequest request,
			SessionObjectBase afso, CustomizeableSection cs) {
		
		List bssdoas = BaseSimSectionDepObjectAssignment.getObjectsForSection(afso.schema, cs.getId());
		
		if ((bssdoas == null) || (bssdoas.size() == 0)){
			tip = new Tips();
		} else {
			BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) bssdoas.get(0);
			tip = (Tips) BaseSimSectionDepObjectAssignment.pullOutObject(afso.schema, bssdoa);
		}
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$

			cs.getContents().put(KEY_FOR_CAN_LEAVE_TIPS,
					request.getParameter(KEY_FOR_CAN_LEAVE_TIPS));

			cs.saveMe(afso.schema);

			String tip_page_text = request.getParameter("tip_page_text");

			tip.setTipText(tip_page_text);
			tip.setActorId(afso.actor_being_worked_on_id);
			tip.setPhaseId(afso.phase_id);
			tip.setCsId(cs.getId());
			tip.setSimId(afso.sim_id);
			tip.setTipLastEditDate(new java.util.Date());
			tip.setBaseTip(true);

			tip.setTipName("s c p a " + afso.sim_id + " " 
					+ cs.getId() + " " + afso.phase_id + " " + afso.actor_being_worked_on_id);
			
			tip.saveMe(afso.schema);
			
			BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, cs.getId());
			
			@SuppressWarnings("unused")
			BaseSimSectionDepObjectAssignment bssdoa_gv = new BaseSimSectionDepObjectAssignment(
					cs.getId(), "org.usip.osp.communications.Tips", 1, tip.getId(), afso.sim_id, afso.schema);
	

		}

	}

	/**
	 * 
	 * @return
	 */
	public Tips getTip() {
		return tip;
	}
	
	public boolean getCanLeaveTip(){
		
		if (cs != null){
			String key = (String) cs.getContents().get(KEY_FOR_CAN_LEAVE_TIPS);
			
			if (( key != null) && (key.equalsIgnoreCase("true"))){
				return true;
			}
					
		}
		
		return false;
		
	}

	public void loadSimCustomizeSection(HttpServletRequest request,
			SessionObjectBase sob, CustomizeableSection cs) {
		
		List bssdoas = BaseSimSectionDepObjectAssignment.getObjectsForSection(sob.schema, cs.getId());
		
		if ((bssdoas == null) || (bssdoas.size() == 0)){
			tip = new Tips();
		} else {
			BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) bssdoas.get(0);
			tip = (Tips) BaseSimSectionDepObjectAssignment.pullOutObject(sob.schema, bssdoa);
		}

	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request,
			SessionObjectBase pso, CustomizeableSection cs) {

	}
}
