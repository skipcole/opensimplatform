package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.networking.ParticipantSessionObject;

/**
 * @author Ronald "Skip" Cole<br />
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
			ParticipantSessionObject pso, CustomizeableSection cs) {
		
		String save_results = (String) request.getParameter("save_results");

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) {
			
			String text_page_text = (String) request.getParameter("text_page_text");
			
			cs.setBigString(text_page_text);

			cs.save(pso.schema);

		}

	}

	public void loadSimCustomizeSection(HttpServletRequest request, 
			ParticipantSessionObject pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub

	}
}
