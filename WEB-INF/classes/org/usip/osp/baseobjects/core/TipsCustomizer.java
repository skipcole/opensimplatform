package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;

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
public class TipsCustomizer extends Customizer{
	
	public static final String KEY_FOR_UNIQUE_TIP = "unique_tip"; //$NON-NLS-1$
	
	public static final String KEY_FOR_CAN_LEAVE_TIPS = "can_leave_tip"; //$NON-NLS-1$
	
	

	@SuppressWarnings("unchecked")
	public void handleCustomizeSection(HttpServletRequest request, 
			SessionObjectBase afso, CustomizeableSection cs) {
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			
			cs.getContents().put(KEY_FOR_UNIQUE_TIP, request.getParameter(KEY_FOR_UNIQUE_TIP));
			
			cs.getContents().put(KEY_FOR_CAN_LEAVE_TIPS, request.getParameter(KEY_FOR_CAN_LEAVE_TIPS));
			
			String create_new_tip = request.getParameter("create_new_tip");
			String new_tip_name = request.getParameter("new_tip_name");
			
			System.out.println("cn: " + create_new_tip);
			System.out.println("ntn: " + new_tip_name);
			
			if (create_new_tip != null){
				
				
				if ((new_tip_name != null) && (new_tip_name.trim().length() > 0)){
					Tips tip = new Tips();
					
					tip.setTipName(new_tip_name);
					tip.setSimId(afso.sim_id);
					
					tip.saveMe(afso.schema);
				}
			}

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
}
