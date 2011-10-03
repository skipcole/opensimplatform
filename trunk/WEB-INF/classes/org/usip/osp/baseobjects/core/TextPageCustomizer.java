package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;

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
public class TextPageCustomizer extends Customizer{


	public void handleCustomizeSection(HttpServletRequest request, 
			SessionObjectBase afso, CustomizeableSection cs) {
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			
			String text_page_text = request.getParameter("text_page_text"); //$NON-NLS-1$
			
			cs.setBigString(text_page_text);
			
			setIfUnsetUniqueName(cs);

			cs.saveMe(afso.schema);

		}

	}
	
	/** If the random number has not been set on this, then set it. 
	 * 
	 * @param cs
	 */
	public static void setIfUnsetUniqueName(CustomizeableSection cs){
		
		if (!(USIP_OSP_Util.stringFieldHasValue(cs.getUniqueName()))
				|| (cs.getUniqueName().equalsIgnoreCase("Text Page"))
		){
			cs.setUniqueName(Math.random() + "");
		}
	}
	
	/**
	 * Returns the section to display on the text age.
	 * @param request
	 * @param sob
	 * @return
	 */
	public static CustomizeableSection getTextPageCS(HttpServletRequest request, SessionObjectBase sob)
	{
		String cs_id = (String) request.getParameter("cs_id");
		String unique_name = (String) request.getParameter("unique_name");
		
		CustomizeableSection cs = new CustomizeableSection();
		
		if (USIP_OSP_Util.stringFieldHasValue(cs_id)){
			cs = CustomizeableSection.getById(sob.schema, cs_id);	
		} else if (USIP_OSP_Util.stringFieldHasValue(unique_name)){
			cs = CustomizeableSection.getByUniqueName(sob.schema, unique_name, sob);
		}
		
		return cs;
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
