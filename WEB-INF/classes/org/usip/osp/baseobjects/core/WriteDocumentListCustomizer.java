package org.usip.osp.baseobjects.core;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.communications.SharedDocument;
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

	private List docs = new ArrayList();

	public List getDocs() {
		return docs;
	}

	public void setDocs(List docs) {
		this.docs = docs;
	}

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {

		if (pso.getRunningSimId() != null){
			docs = SharedDocument.getSetOfDocsForSection(pso.schema, cs.getId(), pso.getRunningSimId());
		} else {
			loadSimCustomSectionForEditing(request, pso, cs);
		}

	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs) {

		List docsAvailable = SharedDocument.getAllBaseDocumentsForSim(pso.schema, pso.sim_id);
		getDocsOnListHashtable(docsAvailable, cs, pso.schema);
		// This functionality is really done by getDocsOnListHashtable. 

	}

	/**
	 * 
	 * @param docsAvailable
	 * @param cs
	 * @param schema
	 * @return
	 */
	public Hashtable getDocsOnListHashtable(List docsAvailable, CustomizeableSection cs, String schema) {

		docs = new ArrayList();
		
		Hashtable returnHash = new Hashtable();
		List thisSetOfBSSDOA = BaseSimSectionDepObjectAssignment.getObjectsForSection(schema, cs.getId());

		// Loop over docsAvailable
		if (!((docsAvailable == null) || (docsAvailable.size() == 0))) {

			for (ListIterator li = docsAvailable.listIterator(); li.hasNext();) {

				SharedDocument sd = (SharedDocument) li.next();

				for (ListIterator lit = thisSetOfBSSDOA.listIterator(); lit.hasNext();) {

					BaseSimSectionDepObjectAssignment this_bssdoa = (BaseSimSectionDepObjectAssignment) lit.next();

					String classOfSD = SharedDocument.class.toString();
					classOfSD = classOfSD.replace("class ", "");
					
					if (this_bssdoa.getClassName().equalsIgnoreCase(classOfSD)) {

						if (sd.getId().intValue() == this_bssdoa.getObjectId().intValue()) {
							docs.add(sd);
							returnHash
									.put(sd.getId() + "_" + this_bssdoa.getDep_obj_index(), " selected=\"selected\" ");
						}
					}

				}
			}
		}

		return returnHash;
	}

}
