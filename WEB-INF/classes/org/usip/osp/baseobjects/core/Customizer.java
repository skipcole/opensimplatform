package org.usip.osp.baseobjects.core;

/**
 * This interface ensures that community developer objects that customize a particular simualtion section 
 * view can work both in the customizing of the view (when the sim author is working on things) and when 
 * a player actually clicks on and looks at a view.
 *
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
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.networking.SessionObjectBase;

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
			SessionObjectBase afso, CustomizeableSection cs);
	
	/**
	 * This method is called by the simulation section during play to load the values stored in the hashtable.
	 * 
	 * @param request
	 * @param cs
	 * @return
	 */
	public abstract void loadSimCustomizeSection(HttpServletRequest request, 
			SessionObjectBase pso, CustomizeableSection cs);
	
	/**
	 * This loads the items for editing, which might be different than those actually used in a simulation run.
	 * For example, document references when editing what documents exist on a page point to bssdoas, but 
	 * during a game the document references will point to ssrdoas.
	 * 
	 * @param request
	 * @param pso
	 * @param cs
	 */
	public abstract void loadSimCustomSectionForEditing(HttpServletRequest request, 
			SessionObjectBase pso, CustomizeableSection cs);
	
	/**
	 * Turns the long into a key and adds it to the list.
	 * 
	 * @param array
	 * @param key
	 */
	public void addToList(ArrayList array, String key) {

		try {
			Long lKey = new Long(key);
			array.add(lKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
