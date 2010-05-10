package org.usip.osp.baseobjects.core;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.networking.SessionObjectBase;

public class PlayerDeclarationDocumentCustomizer extends Customizer {
	
	public static final String TEMPLATE_DOC_KEY = "TEMPLATE_DOC_KEY"; //$NON-NLS-1$
	
	public PlayerDeclarationDocumentCustomizer() {

	}

	public PlayerDeclarationDocumentCustomizer(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs) {
		loadSimCustomizeSection(request, pso, cs);
	}

	@Override
	public void handleCustomizeSection(HttpServletRequest request, SessionObjectBase afso, CustomizeableSection cs) {

		String sending_page = request.getParameter("sending_page"); //$NON-NLS-1$

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("make_player_declaration_document_page"))) { //$NON-NLS-1$

			String make_player_declaration_page_text = request.getParameter("make_player_declaration_page_text"); //$NON-NLS-1$

			cs.setBigString(make_player_declaration_page_text);

			cs.setContents(new Hashtable());

			actorsWithDocs = new ArrayList();

			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
				String pname = (String) e.nextElement();
				String vname = (String) request.getParameter(pname);

				System.out.println("p/v: " + pname + "/" + vname);

				if (pname.startsWith("actor_with_doc_")) {
					pname = pname.replaceAll("actor_with_doc_", "");

					if ((vname != null) && (vname.equalsIgnoreCase("on"))) {
						cs.getContents().put(pname, "actor_with_doc");
						addToList(actorsWithDocs, pname);
					}
				}
			}
		}
		
		String doc_id = request.getParameter("doc_id");
		
		if (doc_id == null) {
			doc_id = "";
		}
		
		cs.getContents().put(TEMPLATE_DOC_KEY, doc_id);
		
		Long templateDocId = USIP_OSP_Util.stringToLong(doc_id);
		
		
		cs.saveMe(afso.schema);
		
		// Need to create Base Sim Section Dependent Objects to signal these objects (params and docs)
		// are needed for this, section.
		BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, cs.getId());
		
		@SuppressWarnings("unused")
		BaseSimSectionDepObjectAssignment bssdoa_cd1 = new BaseSimSectionDepObjectAssignment(
				cs.getId(), "org.usip.osp.communications.SharedDocument", 1, templateDocId, afso.sim_id, afso.schema);
		
	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request, SessionObjectBase pso,
			CustomizeableSection cs) {
		// TODO Auto-generated method stub

	}

	private ArrayList actorsWithDocs = new ArrayList();

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {

		actorsWithDocs = new ArrayList();

		for (Enumeration e = cs.getContents().keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();

			String value = null;
			
			try {
				value = (String) cs.getContents().get(key);
			} catch (Exception exc){
				exc.printStackTrace();
			}

			if (value != null) {
				if (value.equalsIgnoreCase("actor_with_doc")) {
					addToList(actorsWithDocs, key);
				}

			}
		}

	}

	public boolean thisActorHasDocument(Long actor_id) {
		return USIP_OSP_Util.findMatchingLong(actorsWithDocs, actor_id);
	}

}
