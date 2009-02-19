package org.usip.osp.baseobjects.core;

/**
 * This interface ensures that community developer objects that customize a particular simualtion section 
 * view can work both in the customizing of the view (when the sim author is working on things) and when 
 * a player actually clicks on and looks at a view.
 * 
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
import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.networking.ParticipantSessionObject;

public abstract class Customizer {

	/**
	 * This method is called by the customized 'make' page that fine tunes this section while the simulation
	 * is being authored.
	 * 
	 * @param request
	 * @param cs
	 * @return
	 */
	public abstract void handleCustomizeSection(HttpServletRequest request, 
			ParticipantSessionObject pso, CustomizeableSection cs);
	
	/**
	 * This method is called by the simulation section during play.
	 * 
	 * @param request
	 * @param cs
	 * @return
	 */
	public abstract void loadSimCustomizeSection(HttpServletRequest request, 
			ParticipantSessionObject pso, CustomizeableSection cs);
}