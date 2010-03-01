package org.usip.osp.baseobjects.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimulationSectionAssignment;
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
public class ChatHelpCustomizer extends Customizer {

	public ChatHelpCustomizer() {

	}

	public ChatHelpCustomizer(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {

		loadSimCustomizeSection(request, pso, cs);

	}

	@Override
	public void handleCustomizeSection(HttpServletRequest request, SessionObjectBase afso, CustomizeableSection cs) {

		// Remove all dependent object assignments currently associated with this page.
		BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, cs.getId());

		try {

			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
				String pname = (String) e.nextElement();

				String vname = (String) request.getParameter(pname);

				if (pname.startsWith("doc_id_")) {
					cs.getContents().put(pname, vname);
					System.out.println("p/v: " + pname + "/" + vname);
					
					pname = pname.replaceAll("doc_id_", "");
					
					BaseSimSectionDepObjectAssignment bssdoa = BaseSimSectionDepObjectAssignment.getIfExistsElseCreateIt(
							afso.schema, cs.getId(),
							"org.usip.osp.communications.SharedDocument", new Long(pname), afso.sim_id);

					bssdoa.setDepObjIndex(1);

					bssdoa.saveMe(afso.schema);
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

	private class ListSorter implements Comparable {

		public ListSorter() {

		}

		public ListSorter(Long d, Long o) {
			this.dataField = d;
			this.orderField = o;
		}

		private Long dataField = null;
		private Long orderField = null;

		public Long getDataField() {
			return dataField;
		}

		public void setDataField(Long dataField) {
			this.dataField = dataField;
		}

		public Long getOrderField() {
			return orderField;
		}

		public void setOrderField(Long orderField) {
			this.orderField = orderField;
		}

		@Override
		public int compareTo(Object arg0) {
			ListSorter ls = (ListSorter) arg0;

			return  this.orderField.intValue() - ls.orderField.intValue();
		}

	}

}
