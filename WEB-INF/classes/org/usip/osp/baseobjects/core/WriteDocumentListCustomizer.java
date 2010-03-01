package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;

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
public class WriteDocumentListCustomizer  extends Customizer{
	
	/*
	 * We store here how many documents are stored on this page.
	 * The actual docs are s
	 */
	public static final String NUM_DOCS = "NUM_DOCS"; //$NON-NLS-1$
	
	/**
	 * The docs are stored in the hashtable in the order in which they are appear on the page. 
	 * For example, if key/value pair is 'doc_id_1/37' then document 37 will be the first document displayed.
	 */
	public static final String DOC_ID_PREFIX = "doc_id_";

	
	@Override
	public void handleCustomizeSection(HttpServletRequest request, SessionObjectBase afso, CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

}
