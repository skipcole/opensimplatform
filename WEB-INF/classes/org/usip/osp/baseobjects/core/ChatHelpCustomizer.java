package org.usip.osp.baseobjects.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimulationSectionAssignment;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.communications.Conversation;
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

	public static final String E_FOR_EVERYONE = "E_FOR_EVERYONE"; //$NON-NLS-1$

	public ChatHelpCustomizer() {

	}

	public ChatHelpCustomizer(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {

		loadSimCustomizeSection(request, pso, cs);

	}

	@Override
	public void handleCustomizeSection(HttpServletRequest request, SessionObjectBase afso, CustomizeableSection cs) {

		cs.setContents(new Hashtable());

		constantActors = new ArrayList();
		visitingActors = new ArrayList();

		String e_for_everyone = request.getParameter("e_for_everyone");

		if ((e_for_everyone != null) && (e_for_everyone.equalsIgnoreCase("true"))) {
			cs.getContents().put(E_FOR_EVERYONE, e_for_everyone);
		} else {
			cs.getContents().put(E_FOR_EVERYONE, "false");
		}

		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String pname = (String) e.nextElement();
			String vname = (String) request.getParameter(pname);

			System.out.println("p/v: " + pname + "/" + vname);

			if (pname.startsWith("constant_actor_")) {
				pname = pname.replaceAll("constant_actor_", "");

				if ((vname != null) && (vname.equalsIgnoreCase("on"))) {
					cs.getContents().put(pname, "constant");
					addToList(constantActors, pname);
				}
			}

			if (pname.startsWith("visiting_actor_")) {
				pname = pname.replaceAll("visiting_actor_", "");

				if ((vname != null) && (vname.equalsIgnoreCase("on"))) {
					cs.getContents().put(pname, "visiting");
					addToList(visitingActors, pname);
				}
			}
		}

		// Establish the base conversation for this chat help section.

		// Should clean out any conversation for this section that existed.
		BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, cs.getId());

		// For each constact actor/ visiting actor pair set up a conversation.
		for (ListIterator<Long> li = constantActors.listIterator(); li.hasNext();) {
			Long cAct = li.next();

			if (cAct != null) {

				for (ListIterator<Long> liv = visitingActors.listIterator(); liv.hasNext();) {
					Long vAct = liv.next();

					if ((vAct != null) && (vAct.intValue() != cAct.intValue())) {
						Conversation conv = new Conversation();
						conv.setSim_id(afso.sim_id);
						conv.setConversationType(Conversation.TYPE_CHAT_HELP);
						conv.setUniqueConvName("Chat Help: " + cAct.toString() + " and " + vAct.toString());
						conv.saveMe(afso.schema);

						ConvActorAssignment caa = new ConvActorAssignment();
						caa.setActor_id(cAct);
						caa.setConv_id(conv.getId());
						caa.saveMe(afso.schema);

						ConvActorAssignment caa2 = new ConvActorAssignment();
						caa2.setActor_id(vAct);
						caa2.setConv_id(conv.getId());
						caa2.saveMe(afso.schema);

						// Create and save the assignment obect
						@SuppressWarnings("unused")
						BaseSimSectionDepObjectAssignment bssdoa = new BaseSimSectionDepObjectAssignment(cs.getId(),
								"org.usip.osp.communications.Conversation", 1, conv.getId(), afso.sim_id, afso.schema);

					}
				}
			} // End of cAct != null
		}
		// Create conversations for constant-visitor
	}

	private ArrayList constantActors = new ArrayList();
	private ArrayList visitingActors = new ArrayList();
	private boolean eForEveryone = false;

	public ArrayList getConstantActors() {
		return constantActors;
	}

	public void setConstantActors(ArrayList constantActors) {
		this.constantActors = constantActors;
	}

	public ArrayList getVisitingActors() {
		return visitingActors;
	}

	public void setVisitingActors(ArrayList visitingActors) {
		this.visitingActors = visitingActors;
	}

	public boolean isEForEveryone() {
		return eForEveryone;
	}

	public void setEForEveryone(boolean forEveryone) {
		eForEveryone = forEveryone;
	}

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request, SessionObjectBase pso, CustomizeableSection cs) {

		constantActors = new ArrayList();
		visitingActors = new ArrayList();

		for (Enumeration e = cs.getContents().keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();

			String value = (String) cs.getContents().get(key);

			if (value != null) {
				if (value.equalsIgnoreCase("constant")) {
					addToList(constantActors, key);
				}

				if (value.equalsIgnoreCase("visiting")) {
					addToList(visitingActors, key);
				}

			}
		}

		String eString = (String) cs.getContents().get(E_FOR_EVERYONE);

		if ((eString != null) && (eString.equalsIgnoreCase("true"))) {
			this.eForEveryone = true;
		} else {
			this.eForEveryone = false;
		}

	}

	public void addToList(ArrayList array, String key) {

		try {
			Long lKey = new Long(key);
			array.add(lKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean thisActorIsConstant(Long actor_id) {
		return USIP_OSP_Util.findMatchingLong(constantActors, actor_id);
	}

	public boolean thisActorIsVisiting(Long actor_id) {
		return USIP_OSP_Util.findMatchingLong(visitingActors, actor_id);
	}

}
