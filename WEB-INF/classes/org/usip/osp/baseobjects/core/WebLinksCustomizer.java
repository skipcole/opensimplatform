package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.CustomizeableSection;
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
public class WebLinksCustomizer extends Customizer {
	
	public static final String KEY_FOR_LINK_NAME = "link_name"; //$NON-NLS-1$

	@Override
	public void handleCustomizeSection(HttpServletRequest request, SessionObjectBase afso, CustomizeableSection cs) {
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			
			Logger.getRootLogger().debug("CastCustomizeableSection.handleCustomizeSection.save_results"); //$NON-NLS-1$

			cs.setBigString(request.getParameter("cs_bigstring"));
			
			String weblink_descriptor = request.getParameter("weblink_descriptor"); //$NON-NLS-1$

			cs.getContents().put(KEY_FOR_LINK_NAME, weblink_descriptor);

			cs.saveMe(afso.schema);

		}
		
		
		
	}

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

}
