package org.usip.osp.networking;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.*;
import org.usip.osp.baseobjects.core.Customizer;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.communications.Conversation;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.specialfeatures.AllowableResponse;
import org.usip.osp.specialfeatures.GenericVariable;
import org.usip.osp.specialfeatures.Trigger;

import com.oreilly.servlet.MultipartRequest;

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
public class PSO_SectionMgmt {

	private Long phase_being_worked_on_id;

	/** Index of actor being worked on in simulation creation wizard */
	private int currentActorIndex = 0;

	public int getCurrentActorIndex() {
		return this.currentActorIndex;
	}

	public void setCurrentActorIndex(int currentActorIndex) {
		this.currentActorIndex = currentActorIndex;
	}

	AuthorFacilitatorSessionObject afso;

	PSO_SectionMgmt(AuthorFacilitatorSessionObject pso) {
		this.afso = pso;
		this.phase_being_worked_on_id = pso.phase_id;
	}

	/** Index of the actor, 1 to n, that we are working on. */
	private String _actor_index = ""; //$NON-NLS-1$

	/** Id of the base simulation section. */
	private String _bss_id = ""; //$NON-NLS-1$

	/** Id of the custom simulation section. */
	private String _custom_section_id = ""; //$NON-NLS-1$

	public String get_custom_section_id() {
		return this._custom_section_id;
	}

	public void set_custom_section_id(String _custom_section_id) {
		this._custom_section_id = _custom_section_id;
	}

	/** If a command button was entered, this was the command. */
	private String command = ""; //$NON-NLS-1$

	/** id of the page being sought. */
	private String _page_id = ""; //$NON-NLS-1$

	/** Value for phase id passed in from form. */
	private String _phase_id = ""; //$NON-NLS-1$

	/** Tab heading of the simulation section being added. */
	public String _tab_heading = ""; //$NON-NLS-1$

	public String get_tab_heading() {
		return this._tab_heading;
	}

	public void set_tab_heading(String _tab_heading) {
		this._tab_heading = _tab_heading;
	}

	/** Tab position of the simulation section being added. */
	private String _tab_pos = ""; //$NON-NLS-1$

	/**
	 * Whether or not this is a 'universal' section, that is one applied to all
	 * users.
	 */
	private String _universal = ""; //$NON-NLS-1$

	public String get_universal() {
		return this._universal;
	}

	public void set_universal(String _universal) {
		this._universal = _universal;
	}

	private String sending_page = ""; //$NON-NLS-1$
	private String save_page = ""; //$NON-NLS-1$
	private String save_and_add = ""; //$NON-NLS-1$

	/**
	 * 
	 * @param request
	 */
	public void getSimSectionsInternalVariables(HttpServletRequest request) {

		this._actor_index = setIfPassedIn(this._actor_index, request, "actor_index"); //$NON-NLS-1$
		this._bss_id = setIfPassedIn(this._bss_id, request, "bss_id"); //$NON-NLS-1$
		this._custom_section_id = setIfPassedIn(this._custom_section_id, request, "custom_page"); //$NON-NLS-1$
		this._page_id = setIfPassedIn(this._page_id, request, "page_id"); //$NON-NLS-1$
		this._phase_id = setIfPassedIn(this._phase_id, request, "phase_id"); //$NON-NLS-1$
		this._tab_heading = setIfPassedIn(this._tab_heading, request, "tab_heading"); //$NON-NLS-1$
		this._tab_pos = setIfPassedIn(this._tab_pos, request, "tab_pos"); //$NON-NLS-1$
		this._universal = setIfPassedIn(this._universal, request, "universal"); //$NON-NLS-1$

		// Don't store these, just get them if passed in.
		this.command = request.getParameter("command"); //$NON-NLS-1$
		this.sending_page = request.getParameter("sending_page"); //$NON-NLS-1$
		this.save_page = request.getParameter("save_page"); //$NON-NLS-1$
		this.save_and_add = request.getParameter("save_and_add"); //$NON-NLS-1$

	}

	/**
	 * 
	 * @param original
	 * @param request
	 * @param parameter_name
	 * @return
	 */
	public String setIfPassedIn(String original, HttpServletRequest request, String parameter_name) {

		String new_string = request.getParameter(parameter_name);

		if ((new_string != null) && (new_string.length() > 0) && (!(new_string.equalsIgnoreCase("null")))) { //$NON-NLS-1$
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

		Logger.getRootLogger().debug("Determining Phase and _phase_id is " + this._phase_id); //$NON-NLS-1$

		if ((this._phase_id != null) && (this._phase_id.length() > 0)) {
			this.phase_being_worked_on_id = new Long(this._phase_id);
		} else {
			if (this.phase_being_worked_on_id == null) {
				this.phase_being_worked_on_id = simulation.getFirstPhaseId(this.afso.schema);
			}
		}

		this.afso.phase_id = this.phase_being_worked_on_id;
		this.afso.phaseSelected = true;

	}

	/**
	 * This handles
	 * 
	 * @param section_tag
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleCustomizeSection(HttpServletRequest request) {

		getSimSectionsInternalVariables(request);

		this.customizableSectionOnScratchPad = CustomizeableSection.getMe(this.afso.schema, this._custom_section_id);

		if ((this.sending_page != null) && ((this.save_page != null) || (this.save_and_add != null))) {
			// If this is the original custom page, make a new page

			makeCopyOfCustomizedSectionIfNeeded();

			if (this.customizableSectionOnScratchPad.isHasCustomizer()) {
				try {
					System.out.println("getting class for " + this.customizableSectionOnScratchPad.getCustomizerClassName());
					
					Class classDefinition = Class.forName(this.customizableSectionOnScratchPad.getCustomizerClassName());
					this.customizableSectionOnScratchPad.setMyCustomizer((Customizer) classDefinition.newInstance());
					
				} catch (InstantiationException er) {
					Logger.getRootLogger().debug(er);
				} catch (IllegalAccessException er) {
					Logger.getRootLogger().debug(er);
				} catch (ClassNotFoundException er) {
					Logger.getRootLogger().debug(er);
				}

				this.customizableSectionOnScratchPad.getMyCustomizer().handleCustomizeSection(request, this.afso,
						this.customizableSectionOnScratchPad);

			}

			// Update page values
			this.customizableSectionOnScratchPad.setRec_tab_heading(this._tab_heading);
			this.customizableSectionOnScratchPad.save(this.afso.schema);

			if (this.save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(this.customizableSectionOnScratchPad.getId(), this._tab_pos, this._tab_heading,
						request, this._universal);
				// send them back
				this.afso.forward_on = true;
				return this.customizableSectionOnScratchPad;

			}

		} // End of if this is the make_write_news_page

		return this.customizableSectionOnScratchPad;

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
	public Simulation handleSetUniversalSimSectionsPage(HttpServletRequest request) {

		getSimSectionsInternalVariables(request);

		Simulation simulation = this.afso.giveMeSim();

		determinePhase(simulation);

		this.afso.actor_being_worked_on_id = new Long(0);

		this.afso.tempSimSecList = SimulationSectionAssignment.getBySimAndActorAndPhase(this.afso.schema, this.afso.sim_id,
				this.afso.actor_being_worked_on_id, this.phase_being_worked_on_id);

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

		Logger.getRootLogger().warn("_actor_index was: |" + this._actor_index + "|"); //$NON-NLS-1$ //$NON-NLS-2$
		// //////////////////////////////////////////////////////////////
		// Determine the actor we are working on.
		if ((this._actor_index != null) && (this._actor_index.trim().length() > 0)) {
			this.currentActorIndex = new Integer(this._actor_index).intValue();
		} else {
			this.currentActorIndex = 1;
		}

		Logger.getRootLogger().warn("Current actor index = " + this.currentActorIndex); //$NON-NLS-1$

		Simulation simulation = this.afso.giveMeSim();

		Actor this_actor;

		if (simulation.getActors(this.afso.schema).size() > 0) {
			this_actor = simulation.getActors(this.afso.schema).get(this.currentActorIndex - 1);
			this.afso.actor_being_worked_on_id = this_actor.getId();
		} else {
			Logger.getRootLogger().debug("Warning! This simulation appears to have no actors."); //$NON-NLS-1$
		}

		Logger.getRootLogger().debug("actor id is " + this.afso.actor_being_worked_on_id); //$NON-NLS-1$
		// ////////////////////////////////////////////////////////////

		// //////////////////////////////////////////////////////////
		// Determine what phase we are working on.
		determinePhase(simulation);

		Logger.getRootLogger().debug("command is = " + this.command); //$NON-NLS-1$

		if (this.command != null) {
			if (this.command.equalsIgnoreCase("Change Phase")) { //$NON-NLS-1$
				// This has been handled in the phase id section above.
			} else if (this.command.equalsIgnoreCase("move_right")) { //$NON-NLS-1$
				String m_index = request.getParameter("m_index"); //$NON-NLS-1$
				Logger.getRootLogger().debug("doing something on index = " + m_index); //$NON-NLS-1$

				int int_tab_pos = new Long(m_index).intValue() + 1;
				SimulationSectionAssignment ss0 = SimulationSectionAssignment.getBySimAndActorAndPhaseAndPos(
						this.afso.schema, this.afso.sim_id, new Long(this.afso.actor_being_worked_on_id), new Long(
								this.phase_being_worked_on_id), int_tab_pos);

				SimulationSectionAssignment ss1 = SimulationSectionAssignment.getBySimAndActorAndPhaseAndPos(
						this.afso.schema, this.afso.sim_id, new Long(this.afso.actor_being_worked_on_id), new Long(
								this.phase_being_worked_on_id), int_tab_pos + 1);

				ss0.setTab_position(int_tab_pos + 1);
				ss1.setTab_position(int_tab_pos);

				ss0.save(this.afso.schema);
				ss1.save(this.afso.schema);

				if (ss0 != null) {
					Logger.getRootLogger().debug(ss0.getTab_heading());
				} else {
					Logger.getRootLogger().debug("warning ss0 is null"); //$NON-NLS-1$
				}

			} else if (this.command.equalsIgnoreCase("move_left")) { //$NON-NLS-1$

				String m_index = request.getParameter("m_index"); //$NON-NLS-1$
				Logger.getRootLogger().debug("doing something on index = " + m_index); //$NON-NLS-1$

				int int_tab_pos = new Long(m_index).intValue() + 1;
				SimulationSectionAssignment ss0 = SimulationSectionAssignment.getBySimAndActorAndPhaseAndPos(
						this.afso.schema, this.afso.sim_id, new Long(this.afso.actor_being_worked_on_id), new Long(
								this.phase_being_worked_on_id), int_tab_pos);

				SimulationSectionAssignment ss1 = SimulationSectionAssignment.getBySimAndActorAndPhaseAndPos(
						this.afso.schema, this.afso.sim_id, new Long(this.afso.actor_being_worked_on_id), new Long(
								this.phase_being_worked_on_id), int_tab_pos - 1);

				ss0.setTab_position(int_tab_pos - 1);
				ss1.setTab_position(int_tab_pos);

				ss0.save(this.afso.schema);
				ss1.save(this.afso.schema);

				if (ss0 != null) {
					Logger.getRootLogger().debug(ss0.getTab_heading());
				} else {
					Logger.getRootLogger().debug("warning ss0 is null"); //$NON-NLS-1$
				}

			}
		}

		Logger.getRootLogger().debug("getting simSecList for s/a/p:   " + this.afso.sim_id + "/" + this.afso.actor_being_worked_on_id + "/" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ this.phase_being_worked_on_id);
		this.afso.tempSimSecList = SimulationSectionAssignment.getBySimAndActorAndPhase(this.afso.schema, this.afso.sim_id,
				this.afso.actor_being_worked_on_id, this.phase_being_worked_on_id);

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
		if (this._bss_id.equalsIgnoreCase("new_section")) {
			return "create_simulation_section.jsp";
		}

		// Base sim section is retrieved by ID
		BaseSimSection bss = BaseSimSection.getMe(this.afso.schema, this._bss_id);

		if (this.command.equalsIgnoreCase("Add Section")) {

			if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.BaseSimSection")) {
				// Here we add the class straight away.
				addSectionFromRouter(request);

				return this.afso.backPage;

			} else if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.CustomizeableSection")) {

				this._custom_section_id = this._bss_id;

				bss = null;

				CustomizeableSection cbss = CustomizeableSection.getMe(this.afso.schema, this._bss_id);

				return cbss.getMakePage();
				

			}
		}

		Logger.getRootLogger().debug("Router Accidentally called");
		return this.afso.backPage;

	}
	
	/**
	 * 
	 * @param request
	 * @param _universal
	 */
	private void addSectionFromRouter(HttpServletRequest request) {

		boolean universal = false;

		if ((this._universal != null) && (this._universal.equalsIgnoreCase("true"))) {
			universal = true;
		}

		// Read in possible parameters
		getSimSectionsInternalVariables(request);

		Logger.getRootLogger().debug("schema: " + this.afso.schema + ", sim_id: " + this.afso.sim_id + ", a_id: "
				+ this.afso.actor_being_worked_on_id + ", phase_id:" + this.phase_being_worked_on_id + ", bss_id: " + this._bss_id
				+ ", tab heading: " + this._tab_heading + ", tab pos: " + this._tab_pos);

		System.out.flush();

		Long this_tab_pos = getTabPos();

		@SuppressWarnings("unused")
		SimulationSectionAssignment ss0 = new SimulationSectionAssignment(this.afso.schema, this.afso.sim_id, new Long(
				this.afso.actor_being_worked_on_id), new Long(this.phase_being_worked_on_id), new Long(this._bss_id), this._tab_heading,
				this_tab_pos.intValue());

		if (universal) {
			Logger.getRootLogger().debug("applying universal page");
			Simulation simulation = this.afso.giveMeSim();
			SimulationSectionAssignment.applyUniversalSectionsToAllActorsForPhase(this.afso.schema, simulation.getId(),
					this.phase_being_worked_on_id);
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
			this_tab_pos = new Long(this._tab_pos);
		} catch (NumberFormatException nfe) {

			this_tab_pos = SimulationSectionAssignment.getHighestBySimAndActorAndPhase(this.afso.schema, this.afso.sim_id,
					new Long(this.afso.actor_being_worked_on_id), new Long(this.phase_being_worked_on_id));
			Logger.getRootLogger().debug("problem converting tab position: " + nfe.getMessage());
		}

		return this_tab_pos;
	}

	/**
	 * Adds a section coming from a 'customize' page.
	 * 
	 * @param bss_id
	 * @param string_tab_pos
	 * @param tab_heading
	 * @param request
	 * @param universal
	 */
	public void addSectionFromProcessCustomPage(Long bss_id, String string_tab_pos, String tab_heading,
			HttpServletRequest request, String universal) {

		Logger.getRootLogger().debug("bss_id " + bss_id);
		Logger.getRootLogger().debug("tabhead " + tab_heading);
		Logger.getRootLogger().debug("universal " + universal);

		@SuppressWarnings("unused")
		SimulationSectionAssignment ss0 = new SimulationSectionAssignment(this.afso.schema, this.afso.sim_id, new Long(
				this.afso.actor_being_worked_on_id), new Long(this.phase_being_worked_on_id), bss_id, tab_heading, getTabPos()
				.intValue());

		if ((universal != null) && (universal.equalsIgnoreCase("true"))) {
			Logger.getRootLogger().debug("applying sim sections on phase: " + this.phase_being_worked_on_id);
			SimulationSectionAssignment.applyUniversalSectionsToAllActorsForPhase(this.afso.schema, this.afso.sim_id,
					this.phase_being_worked_on_id);
		}

	}

	private CustomizeableSection customizableSectionOnScratchPad;

	public CustomizeableSection getCustomizableSectionOnScratchPad() {
		return this.customizableSectionOnScratchPad;
	}

	public void setCustomizableSectionOnScratchPad(CustomizeableSection customizableSectionOnScratchPad) {
		this.customizableSectionOnScratchPad = customizableSectionOnScratchPad;
	}

	/**
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeReflectionPage(HttpServletRequest request) {

		getSimSectionsInternalVariables(request);

		MultiSchemaHibernateUtil.beginTransaction(this.afso.schema);
		this.customizableSectionOnScratchPad = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(this.afso.schema).get(
				CustomizeableSection.class, new Long(this._custom_section_id));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(this.afso.schema);

		if ((this.sending_page != null) && ((this.save_page != null) || (this.save_and_add != null))

		&& (this.sending_page.equalsIgnoreCase("make_reflection_page"))) {
			// If this is the original custom page, make a new page

			makeCopyOfCustomizedSectionIfNeeded();

			// Update page values
			String make_reflection_page_text = request.getParameter("make_reflection_page_text");
			this.customizableSectionOnScratchPad.setBigString(make_reflection_page_text);
			this.customizableSectionOnScratchPad.setRec_tab_heading(this._tab_heading);
			this.customizableSectionOnScratchPad.save(this.afso.schema);

			if (this.save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(this.customizableSectionOnScratchPad.getId(), this._tab_pos, this._tab_heading,
						request, this._universal);
				// send them back
				this.afso.forward_on = true;
				return this.customizableSectionOnScratchPad;

			}

		} // End of if this is coming from the reflections page

		return this.customizableSectionOnScratchPad;
	}

	private SharedDocument sharedDocument = new SharedDocument();

	public SharedDocument getSharedDocument() {
		return this.sharedDocument;
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
	public CustomizeableSection handleMakeWriteDocumentPage(HttpServletRequest request) {

		this.getSimSectionsInternalVariables(request);

		this.customizableSectionOnScratchPad = CustomizeableSection.getMe(this.afso.schema, this._custom_section_id);

		if ((this.sending_page != null) && ((this.save_page != null) || (this.save_and_add != null))
				&& (this.sending_page.equalsIgnoreCase("make_write_document_page"))) {

			// If this is the original custom page, make a new page
			if (!(this.customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				Logger.getRootLogger().debug("making copy");
				this.customizableSectionOnScratchPad = this.customizableSectionOnScratchPad.makeCopy(afso.schema);
				_custom_section_id = customizableSectionOnScratchPad.getId() + "";
				sharedDocument = new SharedDocument();
			}

			// Update values based on those passed in
			String make_write_document_page_text = request.getParameter("make_write_document_page_text");
			customizableSectionOnScratchPad.setBigString(make_write_document_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(afso.schema);

			String _doc_string = request.getParameter("doc_id");

			// Get the document associated with this customized section
			try {
				Long doc_id = new Long(_doc_string);

				BaseSimSectionDepObjectAssignment bssdoa = BaseSimSectionDepObjectAssignment.getIfExistsElseCreateIt(
						afso.schema, customizableSectionOnScratchPad.getId(),
						"org.usip.osp.communications.SharedDocument", doc_id, afso.sim_id);

				bssdoa.setDepObjIndex(1);

				bssdoa.saveMe(afso.schema);

			} catch (Exception e) {
				e.printStackTrace();
			}

			customizableSectionOnScratchPad.save(afso.schema);

			if (save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(customizableSectionOnScratchPad.getId(), _tab_pos, _tab_heading,
						request, _universal);
				// send them back
				afso.forward_on = true;
			}

		} // End of if we are coming from the make_write_document_page

		return customizableSectionOnScratchPad;
	}

	/**
	 * This method handles the creation of the page to allow player access to
	 * read a document or documents.
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeMemosPage(HttpServletRequest request) {

		this.getSimSectionsInternalVariables(request);

		customizableSectionOnScratchPad = CustomizeableSection.getMe(afso.schema, _custom_section_id);

		// Ever read document page should have one (and only one) document associated with it.
		if (customizableSectionOnScratchPad.getNumDependentObjects() < 1) {
			customizableSectionOnScratchPad.setNumDependentObjects(1);
		}

		Logger.getRootLogger().debug(" sending_page: " + sending_page);
		
		if ((sending_page != null) && ((save_page != null) || (save_and_add != null))
				&& (sending_page.equalsIgnoreCase("make_memos_page"))) {
			
			Logger.getRootLogger().debug("saving stuff");

			// If this is the original custom page, make a new page
			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				customizableSectionOnScratchPad = customizableSectionOnScratchPad.makeCopy(afso.schema);
				_custom_section_id = customizableSectionOnScratchPad.getId() + "";
				sharedDocument = new SharedDocument();
			}

			// Remove all dependent object assignments currently associated with this page.
			BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, customizableSectionOnScratchPad.getId());

			// Loop to the number of documents (which are dependent object) expected to be found
			for (int ii = 1; ii <= customizableSectionOnScratchPad.getNumDependentObjects(); ++ii) {
				
				String req_key = "doc_" + ii;
				String doc_id = (String) request.getParameter(req_key);
				
				Logger.getRootLogger().debug("doc_id is " + doc_id);

				// Create and save the assignment object
				BaseSimSectionDepObjectAssignment bssdoa = new BaseSimSectionDepObjectAssignment(
						customizableSectionOnScratchPad.getId(), "org.usip.osp.communications.SharedDocument", ii,
						new Long(doc_id), afso.sim_id, afso.schema);

				bssdoa.saveMe(afso.schema);
			}

			// Update page values
			String make_memos_page_text = (String) request.getParameter("make_memos_page_text");
			customizableSectionOnScratchPad.setBigString(make_memos_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(afso.schema);

			if (save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(customizableSectionOnScratchPad.getId(), _tab_pos, _tab_heading,
						request, _universal);
				// send them back
				afso.forward_on = true;
			}

		} // End of if this is the make__page

		return customizableSectionOnScratchPad;
	}

	/**
	 * This method handles the creation of the page to allow player access to
	 * read a document or documents.
	 * 
	 * Entry into this section comes in several different ways: when user adds document,
	 * when user add edits, etc.
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeReadDocumentPage(HttpServletRequest request) {

		this.getSimSectionsInternalVariables(request);
		Logger.getRootLogger().debug("making read document page");

		customizableSectionOnScratchPad = CustomizeableSection.getMe(afso.schema, _custom_section_id);

		// Ever read document page should have at least one document associated with it.
		if (customizableSectionOnScratchPad.getNumDependentObjects() < 1) {
			customizableSectionOnScratchPad.setNumDependentObjects(1);
		}

		String add_document = (String) request.getParameter("add_document");
		
		// If adding a document, just increase the number and return.
		if (add_document != null) {
			Logger.getRootLogger().debug("adding document!");

			// If this is the original custom page, make a new page
			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				Logger.getRootLogger().debug("making copy");
				customizableSectionOnScratchPad = customizableSectionOnScratchPad.makeCopy(afso.schema);
				_custom_section_id = customizableSectionOnScratchPad.getId() + "";
				sharedDocument = new SharedDocument();
			}

			int numDocs = customizableSectionOnScratchPad.getNumDependentObjects() + 1;
			customizableSectionOnScratchPad.setNumDependentObjects(numDocs);

			Logger.getRootLogger().debug("now has num docs: " + numDocs);
			// Update page values
			String make_read_document_page_text = (String) request.getParameter("make_read_document_page_text");
			customizableSectionOnScratchPad.setBigString(make_read_document_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(afso.schema);

			return customizableSectionOnScratchPad;
		}

		if ((sending_page != null) && ((save_page != null) || (save_and_add != null))

		&& (sending_page.equalsIgnoreCase("make_read_document_page"))) {

			// If this is the original custom page, make a new page
			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				Logger.getRootLogger().debug("making copy");
				customizableSectionOnScratchPad = customizableSectionOnScratchPad.makeCopy(afso.schema);
				_custom_section_id = customizableSectionOnScratchPad.getId() + "";
				sharedDocument = new SharedDocument();
			}

			// Remove all dependent object assignments currently associated with
			// this page.
			BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, customizableSectionOnScratchPad.getId());

			// Loop to the number of documents (which are dependent object)
			// expected to be found
			for (int ii = 1; ii <= customizableSectionOnScratchPad.getNumDependentObjects(); ++ii) {
				String req_key = "doc_" + ii;
				String doc_id = (String) request.getParameter(req_key);

				Logger.getRootLogger().debug("adding doc: " + doc_id);

				// Create and save the assignment object
				BaseSimSectionDepObjectAssignment bssdoa = new BaseSimSectionDepObjectAssignment(
						customizableSectionOnScratchPad.getId(), "org.usip.osp.communications.SharedDocument", ii,
						new Long(doc_id), afso.sim_id, afso.schema);

				bssdoa.saveMe(afso.schema);
			}

			// Update page values
			String make_read_document_page_text = (String) request.getParameter("make_read_document_page_text");
			customizableSectionOnScratchPad.setBigString(make_read_document_page_text);
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(afso.schema);

			if (save_and_add != null) {
				// add section
				addSectionFromProcessCustomPage(customizableSectionOnScratchPad.getId(), _tab_pos, _tab_heading,
						request, _universal);
				// send them back
				afso.forward_on = true;
			}

		} // End of if this is the make_read_document_page

		return customizableSectionOnScratchPad;
	}

	/**
	 * This method handles the creation of the page to allow player access to
	 * read a document or documents.
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakeSplitPageVertical(HttpServletRequest request) {

		this.getSimSectionsInternalVariables(request);

		customizableSectionOnScratchPad = CustomizeableSection.getMe(afso.schema, _custom_section_id);

		if ((sending_page != null) && ((save_page != null) || (save_and_add != null))

		&& (sending_page.equalsIgnoreCase("make_split_page"))) {

			// If this is the original custom page, make a new page
			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				Logger.getRootLogger().debug("making copy");
				customizableSectionOnScratchPad = customizableSectionOnScratchPad.makeCopy(afso.schema);
				_custom_section_id = customizableSectionOnScratchPad.getId() + "";
			}

			String select_left = (String) request.getParameter("select_left");
			String select_right = (String) request.getParameter("select_right");

			Logger.getRootLogger().debug("select left is " + select_left);
			Logger.getRootLogger().debug("select right is " + select_right);

			// Need to get the simulation section referenced, mark it as a
			// subsection

			SimulationSectionAssignment leftSect = SimulationSectionAssignment.getMe(afso.schema, new Long(select_left));
			SimulationSectionAssignment rightSect = SimulationSectionAssignment.getMe(afso.schema,
					new Long(select_right));

			// Need to set them as sub sections
			leftSect.setSimSubSection(true);
			leftSect.setSimSubSectionIndex(1);
			leftSect.setDisplaySectionId(customizableSectionOnScratchPad.getId());
			leftSect.save(afso.schema);

			rightSect.setSimSubSection(true);
			rightSect.setSimSubSectionIndex(2);
			rightSect.setDisplaySectionId(customizableSectionOnScratchPad.getId());
			rightSect.save(afso.schema);

			// Need to reorder the list
			SimulationSectionAssignment.reorder(afso.schema, afso.sim_id, afso.actor_being_worked_on_id, afso.phase_id);

			// Update page values
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(afso.schema);

			if (save_and_add != null) {
				@SuppressWarnings("unused")
				SimulationSectionAssignment ss0 = new SimulationSectionAssignment(afso.schema, afso.sim_id, new Long(
						afso.actor_being_worked_on_id), new Long(phase_being_worked_on_id),
						customizableSectionOnScratchPad.getId(), _tab_heading, getTabPos().intValue());

				// send them back
				afso.forward_on = true;
			}

		} // End of if this is the make_read_document_page

		return customizableSectionOnScratchPad;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public Conversation handleMakeMeetingRoomPage(HttpServletRequest request) {

		Conversation conv = new Conversation();

		// ///////////////////////////////////////////////////////////////
		// Read in possible parameters
		getSimSectionsInternalVariables(request);

		// ////////////////////////////////////////////////////
		// Get the simulation we are working on
		Simulation sim = new Simulation();
		if (afso.sim_id != null) {
			sim = afso.giveMeSim();
		}

		// /////////////////////////////////////////////////////////
		// Pull this custom page out of the database based on its id.
		_custom_section_id = request.getParameter("custom_page");
		customizableSectionOnScratchPad = CustomizeableSection.getMe(afso.schema, _custom_section_id);

		// ////////////////////////////////////////////////////////////
		// if we are saving this page
		if ((sending_page != null) && ((save_page != null) || (save_and_add != null))
				&& (sending_page.equalsIgnoreCase("make_caucus_page"))) {

			// If this is the original custom page, make a new page
			if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
				Logger.getRootLogger().debug("making copy");
				customizableSectionOnScratchPad = customizableSectionOnScratchPad.makeCopy(afso.schema);

				conv = new Conversation();
				conv.setSim_id(afso.sim_id);
				conv.setConversation_name(_tab_heading);
				conv.save(afso.schema, afso.sim_id);

				// Create and save the assignment obect
				@SuppressWarnings("unused")
				BaseSimSectionDepObjectAssignment bssdoa = new BaseSimSectionDepObjectAssignment(
						customizableSectionOnScratchPad.getId(), "org.usip.osp.communications.Conversation", 1, conv
								.getId(), afso.sim_id, afso.schema);

				_custom_section_id = customizableSectionOnScratchPad.getId() + "";

			} else { // This must not be the original template sim section.

				List convForThisSection = BaseSimSectionDepObjectAssignment.getObjectsForSection(afso.schema,
						customizableSectionOnScratchPad.getId());

				BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) convForThisSection
						.get(0);

				conv = Conversation.getMe(afso.schema, bssdoa.getObjectId());
				// If this is a customized page, but belongs to a different sim,
				// then make a copy ?

			}

			// /////////////////////////////////////////////////////////////////
			// Update page values
			String text_page_text = (String) request.getParameter("text_page_text");
			customizableSectionOnScratchPad.setBigString(text_page_text);

			String page_title = (String) request.getParameter("page_title");
			customizableSectionOnScratchPad.setPageTitle(page_title);

			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);

			// Need to clean out current actor assignments for this conversation
			ConvActorAssignment.removeAllForConversation(afso.schema, conv.getId());

			Hashtable setOfUserRoles = new Hashtable();

			for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
				String param_name = (String) e.nextElement();

				if (param_name.startsWith("role_")) {
					String this_a_id = param_name.replaceFirst("role_", "");

					setOfUserRoles.put(this_a_id, (String) request.getParameter(param_name));

				}
			}

			for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
				String param_name = (String) e.nextElement();

				if (param_name.startsWith("actor_cb_")) {
					if ((request.getParameter(param_name) != null)
							&& (request.getParameter(param_name).equalsIgnoreCase("true"))) {
						String this_a_id = param_name.replaceFirst("actor_cb_", "");
						Logger.getRootLogger().debug("adding " + this_a_id + " in schema" + afso.schema + " to sim_id "
								+ afso.sim_id);
						conv.addActor(this_a_id, afso.schema, afso.sim_id, (String) setOfUserRoles.get(this_a_id));
					}
				}

			}

			conv.saveMe(afso.schema);

			customizableSectionOnScratchPad.save(afso.schema);

			if (save_and_add != null) {

				// add section to the applicable actors
				SimulationSectionAssignment.applySectionsToSomeActors(afso.schema, sim, this.phase_being_worked_on_id,
						customizableSectionOnScratchPad.getId(), _tab_heading, conv.getConv_actor_assigns(afso.schema));
				// send them back
				afso.forward_on = true;
				return null;
			}
		} else { // We are not saving. Get the conversation for this section, or
			// created it.
			List convForThisSection = BaseSimSectionDepObjectAssignment.getObjectsForSection(afso.schema,
					customizableSectionOnScratchPad.getId());

			if ((convForThisSection != null) && (convForThisSection.size() > 0)) {

				BaseSimSectionDepObjectAssignment bssdoa = (BaseSimSectionDepObjectAssignment) convForThisSection
						.get(0);

				conv = Conversation.getMe(afso.schema, bssdoa.getObjectId());
			} else {
				conv = new Conversation();
			}
			// If this is a customized page, but belongs to a different sim,
			// then make a copy ?
		}

		return conv;

	}

	/**
	 * Gets the variables passed in and uses them to create the chat page and
	 * all of the conversations on it.
	 * 
	 * @param request
	 */
	public CustomizeableSection handleMakePrivateChatPage(HttpServletRequest request) {

		// ///////////////////////////////////////////////////////////////
		// Read in possible parameters
		getSimSectionsInternalVariables(request);

		// /////////////////////////////////////////////////////////
		// Pull this custom page out of the database based on its id.
		_custom_section_id = request.getParameter("custom_page");
		customizableSectionOnScratchPad = CustomizeableSection.getMe(afso.schema, _custom_section_id);

		// If this is the original custom page, make a new section for this
		// simulation
		if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {

			customizableSectionOnScratchPad = customizableSectionOnScratchPad.makeCopy(afso.schema);

		}

		customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);

		String sending_page = (String) request.getParameter("sending_page");

		ArrayList<Long> playersWithChat = new ArrayList<Long>();

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("make_private_chat_page"))) {

			// Delete all private conversations for this section since we
			// recreate them below.
			BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema, customizableSectionOnScratchPad.getId());

			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
				String pname = (String) e.nextElement();

				String vname = (String) request.getParameter(pname);
				Logger.getRootLogger().debug(pname + " " + vname);

				if (pname.startsWith("act_cb_")) {
					pname = pname.replaceAll("act_cb_", "");

					StringTokenizer str = new StringTokenizer(pname, "_");

					String f_actor = str.nextToken();
					String s_actor = str.nextToken();
					Logger.getRootLogger().debug("setting up actors " + f_actor + " and " + s_actor);

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
						conv.setSim_id(afso.sim_id);
						conv.setConversation_type(Conversation.TYPE_PRIVATE);
						conv.setConversation_name("One on One");
						conv.saveMe(afso.schema);

						ConvActorAssignment caa = new ConvActorAssignment();
						caa.setActor_id(actorWithChat);
						caa.setConv_id(conv.getId());
						caa.save(afso.schema);

						ConvActorAssignment caa2 = new ConvActorAssignment();
						caa2.setActor_id(actorWithChat2);
						caa2.setConv_id(conv.getId());
						caa2.save(afso.schema);

						// Create and save the assignment obect
						@SuppressWarnings("unused")
						BaseSimSectionDepObjectAssignment bssdoa = new BaseSimSectionDepObjectAssignment(
								customizableSectionOnScratchPad.getId(), "org.usip.osp.communications.Conversation", 1,
								conv.getId(), afso.sim_id, afso.schema);

					} catch (Exception er) {
						er.printStackTrace();
					}
				}
			}

			String save_and_add = (String) request.getParameter("save_and_add");

			if (save_and_add != null) {

				// add section to the applicable actors
				SimulationSectionAssignment.applySectionToSpecificActors(afso.schema, afso.sim_id,
						this.phase_being_worked_on_id, customizableSectionOnScratchPad.getId(), _tab_heading,
						playersWithChat);
				// send them back
				afso.forward_on = true;
			}
		}

		return customizableSectionOnScratchPad;

	}

	/**
	 * Handles the making of a page to display a simple image.
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakeImagePage(HttpServletRequest request) {

		customizableSectionOnScratchPad = new CustomizeableSection();

		try {
			MultipartRequest mpr = new MultipartRequest(request, USIP_OSP_Properties.getValue("uploads"));

			if (mpr != null) {
				_custom_section_id = (String) mpr.getParameter("custom_page");
				customizableSectionOnScratchPad = CustomizeableSection.getMe(afso.schema, _custom_section_id);

				_tab_heading = (String) mpr.getParameter("tab_heading");

				sending_page = (String) mpr.getParameter("sending_page");
				save_page = (String) mpr.getParameter("save_page");
				save_and_add = (String) mpr.getParameter("save_and_add");

				if ((sending_page != null) && ((save_page != null) || (save_and_add != null))) {

					// If this is the original custom page, make a new page
					makeCopyOfCustomizedSectionIfNeeded();

					customizableSectionOnScratchPad.setBigString((String) mpr.getParameter("page_text"));

					customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);

					customizableSectionOnScratchPad.setDescription((String) mpr.getParameter("page_description"));

					// ///////////////////////////////////////
					// Do file upload piece
					afso.makeUploadDir();
					String initFileName = mpr.getOriginalFileName("uploadedfile");

					Logger.getRootLogger().debug("init file was: " + initFileName);

					if ((initFileName != null) && (initFileName.trim().length() > 0)) {
						customizableSectionOnScratchPad.getContents().put("image_file_name", initFileName);

						for (Enumeration e = mpr.getFileNames(); e.hasMoreElements();) {
							String fn = (String) e.nextElement();

							FileIO.saveImageFile("simImage", initFileName, mpr.getFile(fn));
						}
					}
					// End of file upload piece
					// /////////////////////////////////

					customizableSectionOnScratchPad.save(afso.schema);

				} // End of if user took action

				if (save_and_add != null) {
					// add section
					addSectionFromProcessCustomPage(customizableSectionOnScratchPad.getId(), _tab_pos, _tab_heading,
							request, _universal);
					// send them back
					afso.forward_on = true;
				}

			} // End of if mpr != null
		} catch (Exception mpr_e) {
			Logger.getRootLogger().debug("error : " + mpr_e.getMessage());
			_custom_section_id = (String) request.getParameter("custom_page");
			customizableSectionOnScratchPad = CustomizeableSection.getMe(afso.schema, _custom_section_id);

		}

		return customizableSectionOnScratchPad;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public Simulation handleCreateSchedulePage(HttpServletRequest request) {

		Simulation simulation = afso.giveMeSim();

		// Determine if setting sim to edit.
		String sending_page = (String) request.getParameter("sending_page");

		String command = (String) request.getParameter("command");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("create_schedule"))) {
			Logger.getRootLogger().debug("good to here.");

			if ((command != null) && (command.equalsIgnoreCase("Save"))) {
				Logger.getRootLogger().debug("good to here now.");
				RunningSimulation rs = afso.giveMeRunningSim();

				SharedDocument sd = SharedDocument.getScheduleDocument(afso.schema, simulation.getId(), rs.getId());

				Logger.getRootLogger().debug("shared doc id is : " + sd.getId() + "");
				String sim_schedule = (String) request.getParameter("sim_schedule");
				sd.setBigString(sim_schedule);
				sd.saveMe(afso.schema);

			}
		}

		return simulation;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public CustomizeableSection handleMakePlayerDiscreteChoice(HttpServletRequest request) {

		// Read in possible parameters
		getSimSectionsInternalVariables(request);

		customizableSectionOnScratchPad = CustomizeableSection.getMe(afso.schema, _custom_section_id);

		// If adding an allowable response, do that.
		String author_adds_choice = (String) request.getParameter("author_adds_choice");

		if ((author_adds_choice != null) && (author_adds_choice.equalsIgnoreCase("true"))) {
			addGenericVariableIfNeeded();
			AllowableResponse.getAdditionalAR(customizableSectionOnScratchPad, afso.schema);
			return customizableSectionOnScratchPad;
		}

		// If making changes, do the following.
		if ((sending_page != null) && ((save_page != null) || (save_and_add != null))
				&& (sending_page.equalsIgnoreCase("make_player_discrete_choice"))) {

			// If this is the original custom page, make a new page
			makeCopyOfCustomizedSectionIfNeeded();

			addGenericVariableIfNeeded();

			// Update values based on those passed in
			customizableSectionOnScratchPad.setBigString((String) request
					.getParameter("make_player_discrete_choice_text"));
			customizableSectionOnScratchPad.setRec_tab_heading(_tab_heading);
			customizableSectionOnScratchPad.save(afso.schema);

			handleCreationOrUpdateOfAllowableResponse(customizableSectionOnScratchPad, request, afso.schema);

			handleCreationOrUpdateOfTriggers(customizableSectionOnScratchPad, request, afso.schema);

		}

		// If adding page, then add it and forward them on.
		if (save_and_add != null) {
			addSectionFromProcessCustomPage(customizableSectionOnScratchPad.getId(), _tab_pos, _tab_heading, request,
					_universal);
			afso.forward_on = true;
		}

		return customizableSectionOnScratchPad;
	}

	/**
	 * Need to get allowable responses passed in from form. Check to see if they
	 * have ids If ids found, pull out the allowable responses to update them.
	 * If no ids, create new responses Update values of the allowable responses
	 * Store the values of the allowable response ids in the contents hashtable.
	 */
	public static void handleCreationOrUpdateOfAllowableResponse(CustomizeableSection cust, HttpServletRequest request,
			String schema) {

		String num_ars = (String) request.getParameter("num_ars");

		int numb_ars = new Integer(num_ars).intValue();

		GenericVariable gv = GenericVariable.pullOutBaseGV(schema, cust);

		// Unset any previously selected answers.
		gv.setCurrentlySelectedResponse(null);

		for (int ii = 1; ii <= numb_ars; ++ii) {
			String ar_choice_text = (String) request.getParameter("ar_choice_text_" + ii);
			String ar_id = (String) request.getParameter("ar_id_" + ii);
			String ar_selected = (String) request.getParameter("ar_selected_" + ii);

			Logger.getRootLogger().debug("ii was " + ii + " and ar_id was " + ar_id + " and ar_selected was " + ar_selected);

			AllowableResponse thisResponse = new AllowableResponse();

			if ((ar_id != null) && (!(ar_id.equalsIgnoreCase("null"))) && (ar_id.length() > 0)) {
				try {
					Long thisResponseID = new Long(ar_id);
					thisResponse = AllowableResponse.getMe(schema, thisResponseID);

					if ((ar_selected != null) && (ar_selected.equalsIgnoreCase("true"))) {
						// Mark it in the generic variable
						gv.setCurrentlySelectedResponse(thisResponseID);
						Logger.getRootLogger().debug("set gv current response: " + thisResponseID);
						gv.saveMe(schema);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			String ar_aar_text = (String) request.getParameter("ar_aar_text_" + thisResponse.getId());

			thisResponse.setCust_id(cust.getId());
			thisResponse.setResponseText(ar_choice_text);
			thisResponse.setSpecificWordsForAAR(ar_aar_text);
			thisResponse.setIndex(ii);
			thisResponse.saveMe(schema);

		}

	}

	/**
	 * Creates any triggers on this variable.
	 */
	public void handleCreationOrUpdateOfTriggers(CustomizeableSection cs, HttpServletRequest request, String schema) {

		Long var_id = (Long) cs.getContents().get(GenericVariable.GEN_VAR_KEY);

		if (var_id == null) {
			return;
		}

		String add_final_value_text_to_aar = (String) request.getParameter("add_final_value_text_to_aar");
		if ((add_final_value_text_to_aar != null) && (add_final_value_text_to_aar.equalsIgnoreCase("true"))) {
			Trigger trig = Trigger.pullMeOut(schema, cs);
			if (trig == null) {
				trig = new Trigger();
			}

			trig.setSim_id(afso.sim_id);
			trig.setVar_type(Trigger.VAR_TYPE_GENERIC);
			trig.setVar_id(var_id);
			trig.setFire_on(Trigger.FIRE_ON_WHEN_CALLED);
			trig.setAction_type(Trigger.ACT_TYPE_FINAL_VALUE_TEXT_TO_AAR);
			trig.saveMe(schema);
		}

	}

	/**
	 * If this is the original template, then make a copy of it for
	 * customization.
	 */
	public void makeCopyOfCustomizedSectionIfNeeded() {

		if (!(customizableSectionOnScratchPad.isThisIsACustomizedSection())) {
			customizableSectionOnScratchPad = customizableSectionOnScratchPad.makeCopy(afso.schema);
			_custom_section_id = customizableSectionOnScratchPad.getId() + "";

		}
	}

	/**
	 * Adds a generic variable to this custom section if it does not already
	 * have one created.
	 */
	public void addGenericVariableIfNeeded() {

		Long currentVarId = (Long) customizableSectionOnScratchPad.getContents().get(GenericVariable.GEN_VAR_KEY);

		if (currentVarId == null) {
			GenericVariable gv = new GenericVariable();
			gv.setSim_id(afso.sim_id);
			gv.saveMe(afso.schema);

			customizableSectionOnScratchPad.getContents().put(GenericVariable.GEN_VAR_KEY, gv.getId());

		}

	}

}
