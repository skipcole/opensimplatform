package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;
import org.usip.osp.baseobjects.CustomizeableSection;
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
public class SimilieTimelineCustomizer extends Customizer{

	public static final String KEY_FOR_DISPLAY = "timeline_to_show"; //$NON-NLS-1$

	public void handleCustomizeSection(HttpServletRequest request, 
			SessionObjectBase pso, CustomizeableSection cs) {

		Logger.getRootLogger().debug("SimilieTimelineCustomizer.handleCustomizeSection"); //$NON-NLS-1$
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			
			Logger.getRootLogger().debug("SimilieTimelineCustomizer.handleCustomizeSection.save_results"); //$NON-NLS-1$

			cs.setBigString(request.getParameter("cs_bigstring"));
			
			String timeline_to_show = request.getParameter("timeline_to_show"); //$NON-NLS-1$

			cs.getContents().put(KEY_FOR_DISPLAY, timeline_to_show);

			cs.saveMe(pso.schema);

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
