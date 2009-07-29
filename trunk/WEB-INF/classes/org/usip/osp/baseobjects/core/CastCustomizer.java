package org.usip.osp.baseobjects.core;

/**
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
import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.networking.ParticipantSessionObject;

public class CastCustomizer extends Customizer{

	public static final String KEY_FOR_DISPLAY_CONTROL = "display_control";
	public static final String KEY_FOR_CONTROL_ON_BOTTOM = "control_on_bottom";

	public void handleCustomizeSection(HttpServletRequest request, 
			ParticipantSessionObject pso, CustomizeableSection cs) {

		System.out.println("CastCustomizeableSection.handleCustomizeSection");
		
		String save_results = (String) request.getParameter("save_results");

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) {
			
			System.out.println("CastCustomizeableSection.handleCustomizeSection.save_results");

			String display_control = (String) request.getParameter("display_control");

			cs.getContents().put(KEY_FOR_DISPLAY_CONTROL, display_control);

			cs.save(pso.schema);

		}

	}

	public void loadSimCustomizeSection(HttpServletRequest request, 
			ParticipantSessionObject pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub

	}
}
