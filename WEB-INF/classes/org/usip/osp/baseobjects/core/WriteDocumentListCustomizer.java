package org.usip.osp.baseobjects.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
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
public class WriteDocumentListCustomizer extends Customizer {

	/*
	 * We store here how many documents are stored on this page. The actual docs
	 * are s
	 */
	public static final String NUM_DOCS = "NUM_DOCS"; //$NON-NLS-1$

	/**
	 * The docs are stored in the hashtable in the order in which they are
	 * appear on the page. For example, if key/value pair is 'doc_id_1/37' then
	 * document 37 will be the first document displayed.
	 */
	public static final String DOC_ID_PREFIX = "doc_id_";

	public WriteDocumentListCustomizer() {

	}

	public WriteDocumentListCustomizer(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {
		loadSimCustomizeSection(request, pso, cs);
	}

	@Override
	public void handleCustomizeSection(HttpServletRequest request, SessionObjectBase afso, CustomizeableSection cs) {

		// Remove all dependent object assignments currently associated with
		// this page.
		BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, cs.getId());

		try {

			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
				String pname = (String) e.nextElement();

				String vname = (String) request.getParameter(pname);

				if (pname.startsWith("doc_id_")) {
					if ((vname != null) && (!(vname.equalsIgnoreCase("0")))) {
						System.out.println("p/v: " + pname + "/" + vname);

						pname = pname.replaceAll("doc_id_", "");

						BaseSimSectionDepObjectAssignment bssdoa = BaseSimSectionDepObjectAssignment
								.getIfExistsElseCreateIt(afso.schema, cs.getId(),
										"org.usip.osp.communications.SharedDocument", new Long(pname), afso.sim_id);

						bssdoa.setDepObjIndex(new Long(vname).intValue());
						bssdoa.setUniqueTagName("doclist: " + cs.getId() + ", doc: " + pname + ", order: " + vname);

						bssdoa.saveMe(afso.schema);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList docs = new ArrayList();

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {
		ArrayList tempList = new ArrayList();
		docs = new ArrayList();

		for (Enumeration e = cs.getContents().keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();

			Logger.getRootLogger().warn("      checking against key: " + key); //$NON-NLS-1$

			if (key.startsWith("doc_id_")) {

				String position = (String) cs.getContents().get(key);

				if ((position != null) && (!(position.equalsIgnoreCase("0")))) {

					key = key.replaceAll("doc_id_", "");

					// Need to create an anonymous inner class to do the sorting
					ListSorter ls = new ListSorter(new Long(key), new Long(position));
					tempList.add(ls);
				}

			}

			Collections.sort(tempList);

			// Now loop over sorted list, pull out the longs, and add them to
			// the final list of longs,
			// and store it back

			for (ListIterator<ListSorter> li = tempList.listIterator(); li.hasNext();) {
				ListSorter ls = li.next();
				docs.add(ls.getDataField());
			}
		}

	}

}
