package org.usip.osp.baseobjects.core;

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
