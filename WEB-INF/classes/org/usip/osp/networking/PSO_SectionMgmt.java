package org.usip.osp.networking;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.usip.osp.baseobjects.*;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.communications.Conversation;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.specialfeatures.AllowableResponse;
import org.usip.osp.specialfeatures.GenericVariable;

import com.oreilly.servlet.MultipartRequest;

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
public class PSO_SectionMgmt {

	public static final String GEN_VAR_KEY = "gen_var_key";
	
	private Long phase_being_worked_on_id;

	/** Index of actor being worked on in simulation creation wizard */
	private int currentActorIndex = 0;

	public int getCurrentActorIndex() {
		return currentActorIndex;
	}

	public void setCurrentActorIndex(int currentActorIndex) {
		this.currentActorIndex = currentActorIndex;
	}

	ParticipantSessionObject pso;

	PSO_SectionMgmt(ParticipantSessionObject pso) {
		this.pso = pso;
		this.phase_being_worked_on_id = pso.phase_id;
	}

	/** Index of the actor, 1 to n, that we are working on. */
	private String _actor_index = "";

	/** Id of the base simulation section. */
	private String _bss_id = "";

	/** Id of the custom simulation section. */
	private String _custom_section_id = "";

	public String get_custom_section_id() {
		return _custom_section_id;
	}

	public void set_custom_section_id(String _custom_section_id) {
		this._custom_section_id = _custom_section_id;
	}

	/** If a command button was entered, this was the command. */
	private String _command = "";

	/** id of the page being sought. */
	private String _page_id = "";

	/** Value for phase id passed in from form. */
	private String _phase_id = "";

	/** Tab heading of the simulation section being added. */
	public String _tab_heading = "";

	public String get_tab_heading() {
		return _tab_heading;
	}

	public void set_tab_heading(String _tab_heading) {
		this._tab_heading = _tab_heading;
	}

	/** Tab position of the simulation section being added. */
	private String _tab_pos = "";

	/**
	 * Whether or not this is a 'universal' section, that is one applied to all
	 * users.
	 */
	private String _universal = "";

	private String sending_page = "";
	private String save_page = "";
	private String save_and_add = "";
	
	/**
	 * 
	 * @param request
	 */
	public void getSimSectionsInternalVariables(HttpServletRequest request) {

		_actor_index = setIfPassedIn(_actor_index, request, "actor_index");
		_bss_id = setIfPassedIn(_bss_id, request, "bss_id");
		_custom_section_id = setIfPassedIn(_custom_section_id, request,
				"custom_page");
		_command = setIfPassedIn(_command, request, "command");
		_page_id = setIfPassedIn(_page_id, request, "page_id");
		_phase_id = setIfPassedIn(_phase_id, request, "phase_id");
		_tab_heading = setIfPassedIn(_tab_heading, request, "tab_heading");
		_tab_pos = setIfPassedIn(_tab_pos, request, "tab_pos");
		_universal = setIfPassedIn(_universal, request, "universal");
		
		// Don't store these, just get them if passed in.
		sending_page = (String) request.getParameter("sending_page");
		save_page = (String) request.getParameter("save_page");
		save_and_add = (String) request.getParameter("save_and_add");

	}

	/**
	 * 
	 * @param original
	 * @param request
	 * @param parameter_name
	 * @return
	 */
	public String setIfPassedIn(String original, HttpServletRequest request,
			String parameter_name) {

		String new_string = (String) request.getParameter(parameter_name);

		if ((new_string != null) && (new_string.length() > 0)
				&& (!(new_string.equalsIgnoreCase("null")))) {
			return new_string;
		} else {
			return original;
		}

	}

	/**
	 * Based on parameters received attempts to determine the simulation phase
	 * being worked on. If all else fails, it just returns the first phase of
	 * the simulation in question.
	 * 
	 * @param simulation
	 */
	public void determinePhase(Simulation simulation) {

		System.out.println("Determining Phase and _phase_id is " + _phase_id);

		if ((_phase_id != null) && (_phase_id.length() > 0)) {
			phase_being_worked_on_id = new Long(_phase_id);
		} else {
			if (phase_being_worked_on_id == null) {
				phase_being_worked_on_id = simulation.getFirstPhaseId();
			}
		}

		pso.phase_id = phase_being_worked_on_id;
		pso.phaseSelected = true;

	}

	/**
	 * The 'set simulation sections' page is a complicated page. The user can do
	 * the following on this page
	 * <ul>
	 * <li>Enter the page to set the sections that every actor in a sim will
	 * have.</li>
	 * <li>Enter the page changing the phase.</li>
	 * <li>Enter the page after having added a simulation section.</li>
	 * <li>Enter the page after having removed a simulation section.</li>
	 * <li>Enter the page after having changed the order of the simulation
	 * sections.</li>
	 * </ul>
	 * 
	 * Processing is done in the following order:
	 * <ol>
	 * <li>Read in possible parameters</li>
	 * <li>Determine what phase we are working on</li>
	 * <li>Respond to the particular command submitted</li>
	 * <li>Check to see if this a re-order of sections, and finally</li>
	 * <li>Set the set of simulation sections for this actor for this phase to
	 * show.</li>
	 * </ol>
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleSetUniversalSimSectionsPage(
			HttpServletRequest request) {

		getSimSectionsInternalVariables(request);

		Simulation simulation = pso.giveMeSim();

		determinePhase(simulation);

		pso.actor_being_worked_on_id = new Long(0);

		pso.tempSimSecList = SimulationSection.getBySimAndActorAndPhase(
				pso.schema, pso.sim_id, pso.actor_being_worked_on_id,
				phase_being_worked_on_id);

		return simulation;
	}

	/**
	 * This is a complicated page. The user can do the following on this page
	 * <ul>
	 * <li>Enter the page for a particular actor for a sim.</li>
	 * <li>Enter the page changing the phase.</li>
	 * <li>Enter the page after having added a simulation section.</li>
	 * <li>Enter the page after having removed a simulation section.</li>
	 * <li>Enter the page after having changed the order of the simulation
	 * sections.</li>
	 * </ul>
	 * 
	 * Processing is done in the following order:
	 * <ol>
	 * <li>Read in possible parameters</li>
	 * <li>Determine the actor we are working on</li>
	 * <li>Determine what phase we are working on</li>
	 * <li>Respond to the particular command submitted</li>
	 * <li>Check to see if this a re-order of sections, and finally</li>
	 * <li>Set the set of simulation sections for this actor for this phase to
	 * show.</li>
	 * </ol>
	 * 
	 * @param request
	 */
	public Simulation handleSetSimSectionsPage(HttpServletRequest request) {

		// ///////////////////////////////////////////////////////////////
		// Read in possible parameters
		getSimSectionsInternalVariables(request);

		// //////////////////////////////////////////////////////////////
		// Determine the actor we are working on.
		if (_actor_index != null) {
			currentActorIndex = new Integer(_actor_index).intValue();
		} else {
			currentActorIndex = 1;
		}

		System.out.println("Current actor index = " + currentActorIndex);

		Simulation simulation = pso.giveMeSim();

		Actor this_actor;

		if (simulation.getActors().size() > 0) {
			this_actor = (Actor) simulation.getActors().get(
					currentActorIndex - 1);
			pso.actor_being_worked_on_id = this_actor.getId();
		} else {
			System.out
					.println("Warning! This simulation appears to have no actors.");
		}

		System.out.println("actor id is " + pso.actor_being_worked_on_id);
		// ////////////////////////////////////////////////////////////

		// //////////////////////////////////////////////////////////
		// Determine what phase we are working on.
		determinePhase(simulation);

		System.out.println("command is = " + _command);

		if (_command != null) {
			if (_command.equalsIgnoreCase("Change Phase")) {
				// This has been handled in the phase id section above.
			} else if (_command.equalsIgnoreCase("move_right")) {
				String m_index = (String) request.getParameter("m_index");
				System.out.println("doing something on index = " + m_index);

				int int_tab_pos = new Long(m_index).intValue() + 1;
				SimulationSection ss0 = SimulationSection
						.getBySimAndActorAndPhaseAndPos(pso.schema, pso.sim_id,
								new Long(pso.actor_being_worked_on_id),
								new Long(phase_being_worked_on_id), int_tab_pos);

				SimulationSection ss1 = SimulationSection
						.getBySimAndActorAndPhaseAndPos(pso.schema, pso.sim_id,
								new Long(pso.actor_being_worked_on_id),
								new Long(phase_being_worked_on_id),
								int_tab_pos + 1);

				ss0.setTab_position(int_tab_pos + 1);
				ss1.setTab_position(int_tab_pos);

				ss0.save(pso.schema);
				ss1.save(pso.schema);

				if (ss0 != null) {
					System.out.println(ss0.getTab_heading());
				} else {
					System.out.println("warning ss0 is null");
				}

			} else if (_command.equalsIgnoreCase("move_left")) {

				String m_index = (String) request.getParameter("m_index");
				System.out.println("doing something on index = " + m_index);

				int int_tab_pos = new Long(m_index).intValue() + 1;
				SimulationSection ss0 = SimulationSection
						.getBySimAndActorAndPhaseAndPos(pso.schema, pso.sim_id,
								new Long(pso.actor_being_worked_on_id),
								new Long(phase_being_worked_on_id), int_tab_pos);

				SimulationSection ss1 = SimulationSection
						.getBySimAndActorAndPhaseAndPos(pso.schema, pso.sim_id,
								new Long(pso.actor_being_worked_on_id),
								new Long(phase_being_worked_on_id),
								int_tab_pos - 1);

				ss0.setTab_position(int_tab_pos - 1);
				ss1.setTab_position(int_tab_pos);

				ss0.save(pso.schema);
				ss1.save(pso.schema);

				if (ss0 != null) {
					System.out.println(ss0.getTab_heading());
				} else {
					System.out.println("warning ss0 is null");
				}

			}
		}

		System.out.println("getting simSecList for s/a/p:   " + pso.sim_id
				+ "/" + pso.actor_being_worked_on_id + "/"
				+ phase_being_worked_on_id);
		pso.tempSimSecList = SimulationSection.getBySimAndActorAndPhase(
				pso.schema, pso.sim_id, pso.actor_being_worked_on_id,
				phase_being_worked_on_id);

		return simulation;
	}

	/**
	 * The router page is called from either the set universal sections or set
	 * simulation sections jsp. It directs the output to where it need to go.
	 * 
	 * @param request
	 * @return
	 */
	public String handleSimSectionsRouter(HttpServletRequest request) {

		getSimSectionsInternalVariables(request);

		// bss_id is either an integeer, or the string 'new_section'
		if (_bss_id.equalsIgnoreCase("new_section")) {
			return "create_simulation_section.jsp";
		}

		// Base sim section is retrieved by ID
		BaseSimSection bss = BaseSimSection.getMe(pso.schema, _bss_id);

		if (_command.equalsIgnoreCase("Add Section")) {

			if (bss.getClass().getName().equalsIgnoreCase(
					"org.usip.osp.baseobjects.BaseSimSection")) {
				// Here we add the class straight away.
				addSectionFromRouter(request);

				return pso.backPage;

			} else if (bss.getClass().getName().equalsIgnoreCase(
					"org.usip.osp.baseobjects.CustomizeableSection")) {

				_custom_section_id = _bss_id;

				bss = null;

				CustomizeableSection cbss = CustomizeableSection.getMe(
						pso.schema, _bss_id);

				if (!cbss.isHasASpecificMakePage()) {
					return ("customize_page.jsp?custom_page=" + new Long(
							_bss_id));
				} else {
					return (cbss.getSpecificMakePage() + "?custom_page=" + new Long(
							_bss_id));
				}

			}
		}

		System.out.println("Router Accidentally called");
		return pso.backPage;

	}

	/**
	 * 
	 * @param request
	 * @param _universal
	 */
	private void addSectionFromRouter(HttpServletRequest request) {

		boolean universal = false;

		if ((_universal != null) && (_universal.equalsIgnoreCase("true"))) {
			universal = true;
		}
		
		// Read in possible parameters
		getSimSectionsInternalVariables(request);

		System.out.println("schema: " + pso.schema + ", sim_id: " + pso.sim_id
				+ ", a_id: " + pso.actor_being_worked_on_id + ", phase_id:"
				+ phase_being_worked_on_id + ", bss_id: " + _bss_id
				+ ", tab heading: " + _tab_heading + ", tab pos: " + _tab_pos);

		System.out.flush();

		Long this_tab_pos = getTabPos();

		SimulationSection ss0 = new SimulationSection(pso.schema, pso.sim_id,
				new Long(pso.actor_being_worked_on_id), new Long(
						phase_being_worked_on_id), new Long(_bss_id),
				_tab_heading, this_tab_pos.intValue());

		if (universal) {
			System.out.println("applying universal page");
			Simulation simulation = pso.giveMeSim();
			SimulationSection.applyUniversalSectionsToAllActors(pso.schema,
					simulation, phase_being_worked_on_id);
		}

	}

	/**
	 * Gets the tab position to add this at.
	 * 
	 * @return
	 */
	public Long getTabPos() {
		Long this_tab_pos = new Long(1);

		try {
			this_tab_pos = new Long(_tab_pos);
		} catch (NumberFormatException nfe) {

			this_tab_pos = SimulationSection.getHighestBySimAndActorAndPhase(
					pso.schema, pso.sim_id, new Long(
							pso.actor_being_worked_on_id), new Long(
							phase_being_worked_on_id));
			System.out.println("problem converting tab position: "
					+ nfe.getMessage());
		}

		return this_tab_pos;
	}

	public void addSectionFromProcessCustomPage(Long bss_id,
			String string_tab_pos, String tab_heading,
			HttpServletRequest request, String universal) {

		System.out.println("bss_id " + bss_id);
		System.out.println("tabhead " + tab_heading);
		System.out.println("universal " + universal);

		SimulationSection ss0 = new SimulationSection(pso.schema, pso.sim_id,
				new Long(pso.actor_being_worked_on_id), new Long(
						phase_being_worked_on_id), new Long(bss_id),
				tab_heading, getTabPos().intValue());

		if ((universal != null) && (universal.equalsIgnoreCase("true"))) {
			Simulation simulation = pso.giveMeSim();
			SimulationSection.applyUniversalSectionsToAllActors(pso.schema,
					simulation, phase_being_worked_on_id);
		}

	}

	private CustomizeableSection customizableSectionOnScratchPad;

	public CustomizeableSection getCustomizableSectionOnScratchPad() {
		return customizableSectionOnScratchPad;
	}

	public void setCustomizableSectionOnScratchPad(
			CustomizeableSection customizableSectionOnScratchPad) {
		this.customizableSectionOnScratchPad = customizableSectionOnScratchPad;
	}

	public Long sim_conv_id;

	/**
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeReflectionPage(
			HttpServletRequest request) {

		getSimSectionsInternalVariables(request);

		MultiSchemaHibernateUtil.beginTransaction(pso.schema);
		customizableSectionOnScratchPad = (CustomizeableSection) MultiSchemaHibernateUtil
				.getSession(pso.schema).get(CustomizeableSection.class,
						new Long(_custom_section_id));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);

		if ((sending_page != null)
				&& ((save_page != null) || (save_and_add != null))

				&& (sending_page.equalsIgnoreCase("make_reflection_page"))) {
			// If this is the original custom page, make a new page

			makeCopyOfCustomizedSectionIfNeeded();

			// Update page values
			String make_reflection_page_text = (String) request
					.getParameter("make_reflection_page_text");
			customizableSectionOnScratchPad
					.setBigString(make_reflection_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(pso.schema);

			if (save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(customizableSectionOnScratchPad
						.getId(), _tab_pos, _tab_heading, request, _universal);
				// send them back
				pso.forward_on = true;
				return customizableSectionOnScratchPad;

			}

		} // End of if this is the make_write_news_page

		return customizableSectionOnScratchPad;
	}

	private SharedDocument sharedDocument = new SharedDocument();

	public SharedDocument getSharedDocument() {
		return sharedDocument;
	}

	public void setSharedDocument(SharedDocument sharedDocument) {
		this.sharedDocument = sharedDocument;
	}

	/**
	 * This method is called at the top of the make_write_document_page jsp.
	 * This jsp can be reached from 2 places: the sim_section_router and by
	 * calling itself.
	 * 
	 * When the router is called the parameter _custom_section_id will be set.
	 * 
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeWriteDocumentPage(
			HttpServletRequest request) {

		this.getSimSectionsInternalVariables(request);

		customizableSectionOnScratchPad = CustomizeableSection.getMe(
				pso.schema, _custom_section_id);

		if ((sending_page != null)
				&& ((save_page != null) || (save_and_add != null))
				&& (sending_page.equalsIgnoreCase("make_write_document_page"))) {

			// If this is the original custom page, make a new page
			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				System.out.println("making copy");
				customizableSectionOnScratchPad = customizableSectionOnScratchPad
						.makeCopy(pso.schema);
				_custom_section_id = customizableSectionOnScratchPad.getId()
						+ "";
				sharedDocument = new SharedDocument();
			}

			// Update values based on those passed in
			String make_write_document_page_text = (String) request
					.getParameter("make_write_document_page_text");
			customizableSectionOnScratchPad
					.setBigString(make_write_document_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(pso.schema);

			
			//String doc_title = (String) request.getParameter("doc_title");
			String _doc_string = (String) request
					.getParameter(SharedDocument.DOCS_IN_HASHTABLE_KEY);

			// Get the document associated with this customized section
			try {
				Long doc_id = new Long(_doc_string);

				customizableSectionOnScratchPad.getContents().put(
						SharedDocument.DOCS_IN_HASHTABLE_KEY, _doc_string);

			} catch (Exception e) {
				e.printStackTrace();
			}

			customizableSectionOnScratchPad.save(pso.schema);

			if (save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(customizableSectionOnScratchPad.getId(), _tab_pos, _tab_heading, request, _universal);
				// send them back
				pso.forward_on = true;
			}

		} // End of if we are coming from the make_write_document_page

		return customizableSectionOnScratchPad;
	}

	/**
	 * 
	 * @param request
	 */
	public void handleMakePrivateChatPage(HttpServletRequest request) {

		String sending_page = (String) request.getParameter("sending_page");

		ArrayList<Long> playersWithChat = new ArrayList<Long>();

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("make_private_chat_page"))) {

			// ////////////////////////////////////////////////////
			// Get the simulation we are working on
			Simulation sim = new Simulation();
			if (pso.sim_id != null) {
				sim = pso.giveMeSim();
			}

			// /////////////////////////////////////////////////////////
			// Pull this custom page out of the database based on its id.
			_custom_section_id = request.getParameter("custom_page");
			MultiSchemaHibernateUtil.beginTransaction(pso.schema);
			customizableSectionOnScratchPad = (CustomizeableSection) MultiSchemaHibernateUtil
					.getSession(pso.schema).get(CustomizeableSection.class,
							new Long(_custom_section_id));
			MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);

			// Delete all private conversations for this simulation since we
			// recreate them below.
			Conversation.deleteAllPrivateChatForSim(pso.schema, pso.sim_id);

			for (Enumeration<String> e = request.getParameterNames(); e
					.hasMoreElements();) {
				String pname = (String) e.nextElement();

				String vname = (String) request.getParameter(pname);
				System.out.println(pname + " " + vname);

				if (pname.startsWith("act_cb_")) {
					pname = pname.replaceAll("act_cb_", "");

					StringTokenizer str = new StringTokenizer(pname, "_");

					String f_actor = str.nextToken();
					String s_actor = str.nextToken();
					System.out.println("setting up actors " + f_actor + " and "
							+ s_actor);

					try {
						Long actorWithChat = new Long(f_actor);
						Long actorWithChat2 = new Long(s_actor);

						if (!(playersWithChat.contains(actorWithChat))) {
							playersWithChat.add(actorWithChat);
						}

						if (!(playersWithChat.contains(actorWithChat2))) {
							playersWithChat.add(actorWithChat2);
						}

						Conversation conv = new Conversation();
						conv.setSim_id(pso.sim_id);
						conv.setConversation_type(Conversation.TYPE_PRIVATE);
						conv.setConversation_name("One on One");

						ConvActorAssignment caa = new ConvActorAssignment();
						caa.setActor_id(actorWithChat);
						caa.save(pso.schema);

						ConvActorAssignment caa2 = new ConvActorAssignment();
						caa2.setActor_id(actorWithChat2);
						caa2.save(pso.schema);

						ArrayList al = new ArrayList();
						al.add(caa);
						al.add(caa2);

						conv.setConv_actor_assigns(al);

						MultiSchemaHibernateUtil.beginTransaction(pso.schema);
						MultiSchemaHibernateUtil.getSession(pso.schema)
								.saveOrUpdate(conv);
						MultiSchemaHibernateUtil
								.commitAndCloseTransaction(pso.schema);

					} catch (Exception er) {
						er.printStackTrace();
					}
				}
			}

			String save_and_add = (String) request.getParameter("save_and_add");

			if (save_and_add != null) {

				// add section to the applicable actors

				System.out.println("this: " + this);
				System.out.println("csosp: " + customizableSectionOnScratchPad);

				SimulationSection.applySectionToSpecificActors(pso.schema, sim,
						this.phase_being_worked_on_id,
						customizableSectionOnScratchPad.getId(), _tab_heading,
						playersWithChat);
				// send them back
				pso.forward_on = true;
			}
		}

	}

	/**
	 * This method handles the creation of the page to allow player access to
	 * read a document or documents.
	 * 
	 * @param request
	 */
	public void handleMakeReadDocumentPage(HttpServletRequest request) {

		this.getSimSectionsInternalVariables(request);

		customizableSectionOnScratchPad = CustomizeableSection.getMe(
				pso.schema, _custom_section_id);

		if ((sending_page != null)
				&& ((save_page != null) || (save_and_add != null))

				&& (sending_page.equalsIgnoreCase("make_read_document_page"))) {

			// If this is the original custom page, make a new page

			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				System.out.println("making copy");
				customizableSectionOnScratchPad = customizableSectionOnScratchPad
						.makeCopy(pso.schema);
				_custom_section_id = customizableSectionOnScratchPad.getId()
						+ "";
				sharedDocument = new SharedDocument();
			}

			String _doc_ids = (String) request
					.getParameter(SharedDocument.DOCS_IN_HASHTABLE_KEY);

			System.out.println("Got Document ids!!!!!: " + _doc_ids);

			StringTokenizer str = new StringTokenizer(_doc_ids, ",");

			while (str.hasMoreTokens()) {
				String nextToken = str.nextToken();
				nextToken = nextToken.trim();
				System.out.println("found token for: " + nextToken);
			}

			customizableSectionOnScratchPad.getContents().put(
					SharedDocument.DOCS_IN_HASHTABLE_KEY, _doc_ids);

			// Update page values
			String make_read_document_page_text = (String) request
					.getParameter("make_read_document_page_text");
			customizableSectionOnScratchPad
					.setBigString(make_read_document_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(pso.schema);

			if (save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(customizableSectionOnScratchPad
						.getId(), _tab_pos, _tab_heading, request, _universal);
				// send them back
				pso.forward_on = true;
			}

		} // End of if this is the make_read_document_page

		return;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleMakeCaucusPage(HttpServletRequest request) {

		// ///////////////////////////////////////////////////////////////
		// Read in possible parameters
		getSimSectionsInternalVariables(request);

		// ////////////////////////////////////////////////////
		// Get the simulation we are working on
		Simulation sim = new Simulation();
		if (pso.sim_id != null) {
			sim = pso.giveMeSim();
		}

		// /////////////////////////////////////////////////////////
		// Pull this custom page out of the database based on its id.
		_custom_section_id = request.getParameter("custom_page");
		MultiSchemaHibernateUtil.beginTransaction(pso.schema);
		customizableSectionOnScratchPad = (CustomizeableSection) MultiSchemaHibernateUtil
				.getSession(pso.schema).get(CustomizeableSection.class,
						new Long(_custom_section_id));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);

		// ////////////////////////////////////////////////////////////
		// if we are saving this page
		if ((sending_page != null)
				&& ((save_page != null) || (save_and_add != null))
				&& (sending_page.equalsIgnoreCase("make_caucus_page"))) {

			// If this is the original custom page, make a new page
			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				System.out.println("making copy");
				customizableSectionOnScratchPad = customizableSectionOnScratchPad
						.makeCopy(pso.schema);

				Conversation conv = new Conversation();
				conv.setSim_id(pso.sim_id);
				conv.setConversation_name(_tab_heading);
				conv.save(pso.schema, pso.sim_id);
				customizableSectionOnScratchPad.getContents().put(
						"sim_conv_id", conv.getId());

				sim_conv_id = conv.getId();

				_custom_section_id = customizableSectionOnScratchPad.getId()
						+ "";

			} else { // This must not be the original template sim section.

				sim_conv_id = (Long) customizableSectionOnScratchPad
						.getContents().get("sim_conv_id");

				// If this is a customized page, but belongs to a different sim,
				// then make a copy
				try {

					MultiSchemaHibernateUtil.beginTransaction(pso.schema);
					Conversation conv = (Conversation) MultiSchemaHibernateUtil
							.getSession(pso.schema).get(Conversation.class,
									sim_conv_id);

					if (!(conv.getSim_id().equals(pso.sim_id))) {
						customizableSectionOnScratchPad = customizableSectionOnScratchPad
								.makeCopy(pso.schema);

						conv = new Conversation();
						conv.setSim_id(pso.sim_id);

						conv.save(pso.schema, pso.sim_id);

						customizableSectionOnScratchPad.getContents().put(
								"sim_conv_id", conv.getId());

						_custom_section_id = customizableSectionOnScratchPad
								.getId()
								+ "";
					}

					conv.setConversation_name(_tab_heading);
					conv.save(pso.schema, pso.sim_id);
					sim_conv_id = conv.getId();

					MultiSchemaHibernateUtil
							.commitAndCloseTransaction(pso.schema);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////////////////////////////////////////////////////////////////
			// Update page values
			String text_page_text = (String) request
					.getParameter("text_page_text");
			customizableSectionOnScratchPad.setBigString(text_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);

			sim_conv_id = (Long) customizableSectionOnScratchPad.getContents()
					.get("sim_conv_id");

			MultiSchemaHibernateUtil.beginTransaction(pso.schema);
			Conversation conv = (Conversation) MultiSchemaHibernateUtil
					.getSession(pso.schema)
					.get(Conversation.class, sim_conv_id);
			conv.setConv_actor_assigns(new ArrayList());
			MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);

			for (Enumeration e = request.getParameterNames(); e
					.hasMoreElements();) {
				String param_name = (String) e.nextElement();

				if (param_name.startsWith("actor_cb_")) {
					if ((request.getParameter(param_name) != null)
							&& (request.getParameter(param_name)
									.equalsIgnoreCase("true"))) {
						String this_a_id = param_name.replaceFirst("actor_cb_",
								"");
						System.out.println("adding " + this_a_id + " in schema"
								+ pso.schema + " to sim_id " + pso.sim_id);
						conv.addActor(this_a_id, pso.schema, pso.sim_id);
					}
				}
			}

			MultiSchemaHibernateUtil.beginTransaction(pso.schema);
			MultiSchemaHibernateUtil.getSession(pso.schema).saveOrUpdate(conv);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);

			customizableSectionOnScratchPad.save(pso.schema);

			if (save_and_add != null) {

				// add section to the applicable actors
				SimulationSection.applySectionsToSomeActors(pso.schema, sim,
						this.phase_being_worked_on_id,
						customizableSectionOnScratchPad.getId(), _tab_heading,
						conv.getConv_actor_assigns());
				// send them back
				pso.forward_on = true;
				return null;
			}
		}

		return sim;

	}

	public CustomizeableSection handleMekeImagePage(HttpServletRequest request) {

		getSimSectionsInternalVariables(request);

		String page_title = "";
		String image_file_name = "";

		CustomizeableSection cs = null;

		if (_custom_section_id != null) {
			MultiSchemaHibernateUtil.beginTransaction(pso.schema);
			cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(
					pso.schema).get(CustomizeableSection.class,
					new Long(_custom_section_id));
			page_title = (String) cs.getContents().get("page_title");
			image_file_name = (String) cs.getContents().get("image_file_name");
			MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
		}

		try {
			MultipartRequest mpr = new MultipartRequest(request,
					USIP_OSP_Properties.getValue("uploads"));

			if (mpr != null) {

				String sending_page = (String) mpr.getParameter("sending_page");
				String submit_new_image_page = (String) mpr
						.getParameter("submit_new_image_page");

				String upload_and_add = (String) mpr
						.getParameter("upload_and_add");

				_custom_section_id = mpr.getParameter("custom_page");

				if (_custom_section_id != null) {
					MultiSchemaHibernateUtil.beginTransaction(pso.schema);
					cs = (CustomizeableSection) MultiSchemaHibernateUtil
							.getSession(pso.schema).get(
									CustomizeableSection.class,
									new Long(_custom_section_id));
					System.out.println("from db it was "
							+ cs.getContents().get("page_title"));
					MultiSchemaHibernateUtil
							.commitAndCloseTransaction(pso.schema);

				}

				if ((sending_page != null) && (upload_and_add != null)
						&& (sending_page.equalsIgnoreCase("add_image_page"))) {
					// If this is the original custom page, make a new page
					if (!(cs.isThisIsACustomizedSection())) {
						System.out.println("making copy");
						cs = cs.makeCopy(pso.schema);
						_custom_section_id = cs.getId() + "";
					}

					page_title = (String) mpr.getParameter("page_title");

					cs.setRec_tab_heading(_tab_heading);
					cs.getContents().put("page_title", page_title);

					String page_description = (String) mpr
							.getParameter("page_description");
					cs.setDescription(page_description);

					// ///////////////////////////////////////
					// Do file upload piece
					pso.makeUploadDir();
					String initFileName = mpr
							.getOriginalFileName("uploadedfile");

					System.out.println("init file was: " + initFileName);

					if ((initFileName != null)
							&& (initFileName.trim().length() > 0)) {
						cs.getContents().put("image_file_name", initFileName);

						for (Enumeration e = mpr.getFileNames(); e
								.hasMoreElements();) {
							String fn = (String) e.nextElement();

							FileIO.saveImageFile("simImage", initFileName, mpr
									.getFile(fn));
						}
					}
					// End of file upload piece
					// /////////////////////////////////

					cs.save(pso.schema);

					// add section
					addSectionFromProcessCustomPage(cs.getId(), _tab_pos,
							_tab_heading, request, _universal);
					// send them back
					pso.forward_on = true;

				} // End of if user took action

			} // End of if mpr != null
		} catch (Exception mpr_e) {
			System.out.println("error : " + mpr_e.getMessage());
		}

		return cs;

		/*
		 * //Update page values String text_page_text = (String)
		 * request.getParameter("text_page_text");
		 * cs.setBigString(text_page_text);
		 */

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleCreateSchedulePage(HttpServletRequest request) {

		Simulation simulation = pso.giveMeSim();

		// Determine if setting sim to edit.
		String sending_page = (String) request.getParameter("sending_page");

		String command = (String) request.getParameter("command");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("create_schedule"))) {
			System.out.println("good to here.");

			if ((command != null) && (command.equalsIgnoreCase("Save"))) {
				System.out.println("good to here now.");
				RunningSimulation rs = pso.giveMeRunningSim();

				SharedDocument sd = SharedDocument.getScheduleDocument(
						pso.schema, simulation.getId(), rs.getId());
				
				System.out.println("shared doc id is : " + sd.getId() + "");
				String sim_schedule = (String) request
						.getParameter("sim_schedule");
				sd.setBigString(sim_schedule);
				sd.save(pso.schema);

			}
		}

		return simulation;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakePlayerDiscreteChoice(
			HttpServletRequest request) {
		
		// Read in possible parameters
		getSimSectionsInternalVariables(request);

		customizableSectionOnScratchPad = CustomizeableSection.getMe(
				pso.schema, _custom_section_id);

		// If making changes, do the following.
		if ((sending_page != null)
				&& ((save_page != null) || (save_and_add != null))
				&& (sending_page.equalsIgnoreCase("make_player_discrete_choice"))) {

			// If this is the original custom page, make a new page
			makeCopyOfCustomizedSectionIfNeeded();
			
			addGenericVariableIfNeeded();
			
			// Update values based on those passed in
			customizableSectionOnScratchPad.setBigString((String) request.getParameter("make_player_discrete_choice_text"));
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(pso.schema);
			
			handleCreationOrUpdateOfAllowableResponse(customizableSectionOnScratchPad.getId(), request, pso.schema);
			

		}
		
		// If adding page, then add it and forward them on.
		if (save_and_add != null) {
			addSectionFromProcessCustomPage(customizableSectionOnScratchPad.getId(), _tab_pos, _tab_heading, request, _universal);
			pso.forward_on = true;
		}
		
		return customizableSectionOnScratchPad;
	}
	
	/**
	 * Need to get allowable responses passed in from form.
	 * Check to see if they have ids
	 * 		If ids found, pull out the allowable responses to update them.
	 * 		If no ids, create new responses
	 * Update values of the allowable responses
	 * Store the values of the allowable response ids in the contents hashtable.
	 */	
	public void handleCreationOrUpdateOfAllowableResponse(Long cust_id, HttpServletRequest request, String schema){
		
		String num_ars = (String) request.getParameter("num_ars");
		
		int numb_ars = new Integer(num_ars).intValue();
		
		for (int ii = 1; ii <= numb_ars; ++ii){
			String ar_text = (String) request.getParameter("ar_text_" + ii);
			String ar_id = (String) request.getParameter("ar_id_" + ii);
			
			AllowableResponse thisResponse = new AllowableResponse();
			
			if ((ar_id != null) && (!(ar_id.equalsIgnoreCase("null"))) && (ar_id.length() > 0)){
				try {
					Long thisResponseID = new Long(ar_id);
					thisResponse = AllowableResponse.getMe(schema, thisResponseID);
					
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			thisResponse.setCust_id(cust_id);
			thisResponse.setResponseText(ar_text);
			thisResponse.setIndex(ii);
			thisResponse.saveMe(schema);
			
		}
		/**

			// ar_selected_1	indicates if response 1 starts out selected.
		*/
		
	}
	

	/**
	 * If this is the original template, then make a copy of it for customization.
	 */
	public void makeCopyOfCustomizedSectionIfNeeded(){
		
		if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
			customizableSectionOnScratchPad = customizableSectionOnScratchPad.makeCopy(pso.schema);
			_custom_section_id = customizableSectionOnScratchPad.getId() + "";

		}
	}
	
	/** Adds a generic variable to this custom section if it does not already have one created. */
	public void addGenericVariableIfNeeded(){
		
		String currentVarId = (String) customizableSectionOnScratchPad.getContents().get(GEN_VAR_KEY);
		
		if (currentVarId == null){
			GenericVariable gv = new GenericVariable();
			gv.setSim_id(pso.sim_id);
			gv.saveMe(pso.schema);
			
			customizableSectionOnScratchPad.getContents().put(GEN_VAR_KEY, gv.getId());
			
		}
		
		
	}

}
