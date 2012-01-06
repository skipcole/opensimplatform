package com.seachangesimulations.osp.questions;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.SimulationPhase;
import org.usip.osp.baseobjects.core.Customizer;
import org.usip.osp.networking.SessionObjectBase;

/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
public class QuestionCustomizer extends Customizer {

	public static final String KEY_FOR_PAGETITLE = "questions_page_title"; //$NON-NLS-1$
	public static final String KEY_FOR_POSTANSWERTEXT = "questions_post_answer_text"; //$NON-NLS-1$
	public static final String KEY_FOR_PHASE_CHANGE = "questions_allow_phase_change"; //$NON-NLS-1$
	public static final String KEY_FOR_PHASE_ID = "questions_phase_id"; //$NON-NLS-1$

	@Override
	public void handleCustomizeSection(HttpServletRequest request,
			SessionObjectBase afso, CustomizeableSection cs) {

		String sending_page = request.getParameter("sending_page");

		if (sending_page == null) {
			return;
		}
		
		// There are three pages that legitmately send one here. If not coming from 
		// one of these, send them back.
		if (!(
				
				(sending_page.equalsIgnoreCase("make_questions_page")) ||
				(sending_page.equalsIgnoreCase("instructor_questions_view")) ||
				(sending_page.equalsIgnoreCase("make_review_answers_page"))
				
		)) {

			return;
		}
		
		//if (sending_page.equalsIgnoreCase("make_review_answers_page")

		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$

			Logger.getRootLogger().debug(
					"QuestionCustomizer.handleCustomizeSection.save_results"); //$NON-NLS-1$

			cs.setBigString(request.getParameter("cs_bigstring"));

			cs.getContents().put(KEY_FOR_PAGETITLE,
					request.getParameter(KEY_FOR_PAGETITLE));


			// Need to save to make sure that cs has a valid id going forward.
			cs.saveMe(afso.schema);

			// Need to create Base Sim Section Dependent Objects to signal these
			// QandRs are for this one.
			BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema,
					cs.getId());

			// The make questions page has some additonal options.
			if ((sending_page != null)
					&& (sending_page.equalsIgnoreCase("make_questions_page"))) {
				
				cs.getContents().put(KEY_FOR_POSTANSWERTEXT,
						request.getParameter(KEY_FOR_POSTANSWERTEXT));
				
				String allow_phase_change = request
						.getParameter(KEY_FOR_PHASE_CHANGE);

				cs.getContents().put(KEY_FOR_PHASE_CHANGE, allow_phase_change);

				String phase_id = request.getParameter(KEY_FOR_PHASE_ID);

				if (allow_phase_change.equalsIgnoreCase("yes")) {
					@SuppressWarnings("unused")
					BaseSimSectionDepObjectAssignment bssdoaPhaseId = new BaseSimSectionDepObjectAssignment(
							cs.getId(), SimulationPhase.class.toString(), 1,
							new Long(phase_id), afso.sim_id, afso.schema);
				}
				
				cs.saveMe(afso.schema);
			}
			

			// Need to remove any previous questions and answers for this
			// section also.
			// QuestionAndResponse.deleteAllForSimAndCustomSection(afso.schema,
			// afso.sim_id, cs.getId());

			addQuestionsToThisSection(afso.schema, afso.sim_id, cs.getId(),
					request);

		}

	}

	/**
	 * Adds questions selected in checkboxes to this section.
	 * 
	 * @param schema
	 * @param simId
	 * @param csId
	 * @param request
	 */
	public static void addQuestionsToThisSection(String schema, Long simId,
			Long csId, HttpServletRequest request) {

		// Loop over the request passed in and pull out questions and
		// response.
		for (Enumeration<String> e = request.getParameterNames(); e
				.hasMoreElements();) {
			String param_name = (String) e.nextElement();
			String value_name = (String) request.getParameter(param_name);

			if (param_name.startsWith("question_")) {

				System.out.println(param_name + " is " + value_name);
				param_name = param_name.replaceAll("question_", "");

				String position = (String) request.getParameter("position_"
						+ param_name);

				Long questionId = null;
				Long questionPosition = null;

				try {
					questionId = new Long(param_name);
					questionPosition = new Long(position);
				} catch (Exception er) {

				}

				if ((questionId != null) && (questionPosition != null)) {
					BaseSimSectionDepObjectAssignment bssdoaQandR = new BaseSimSectionDepObjectAssignment(
							csId, QuestionAndResponse.class.toString(),
							questionPosition.intValue(), questionId, simId,
							schema);
				}
			}
		} // end of loop over parameter names
	}

	/**
	 * Gets the phase id for a question to find out what phase can next be entered.
	 * 
	 * @param schema
	 * @param csId
	 * @return
	 */
	public static Long getPhaseId(String schema, Long csId) {

		List bsdoaFound = BaseSimSectionDepObjectAssignment
				.getObjectsForSection(schema, csId,
						SimulationPhase.class.toString());

		if (bsdoaFound.size() == 0) {
			return null;
		} else if (bsdoaFound.size() > 1) {
			return null;
		} else {
			BaseSimSectionDepObjectAssignment bsdoa = (BaseSimSectionDepObjectAssignment) bsdoaFound
					.get(0);

			return bsdoa.getObjectId();
		}
	}

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request,
			SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request,
			SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub

	}

	/* Takes the answers from the player */
	public static void handleChanges(HttpServletRequest request,
			CustomizeableSection cs, String schema, Long simId, Long rsId,
			Long actorId, Long userId) {

		String sending_page = request.getParameter("sending_page");
		String command_save = request.getParameter("command_save");
		String command_submit = request.getParameter("command_submit");

		boolean finalSubmission = false;

		if (command_submit != null) {
			finalSubmission = true;
		}

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("questions"))) {

			if ((command_save != null) || (command_submit != null)) {

				for (Enumeration<String> e = request.getParameterNames(); e
						.hasMoreElements();) {

					String param_name = (String) e.nextElement();

					if (param_name.startsWith("q_id_")) { // Found a question
															// with this id
						String questionId = (String) request
								.getParameter(param_name);

						String playerAnswer = request.getParameter("answer_"
								+ questionId);

						Long qId = new Long(questionId);

						PlayerAnswer this_pa = PlayerAnswer
								.getByQuestionRunningSimAndUserIds(schema, qId,
										rsId, userId);

						// Setting these values to what was passed in, since if
						// this is new they will be blank.
						this_pa.setRunningSimId(rsId);
						this_pa.setUserId(userId);
						this_pa.setQuestionId(qId);
						this_pa.setSimId(simId);
						this_pa.setActorId(actorId);
						this_pa.setPlayerAnswer(playerAnswer);

						if (finalSubmission) {
							this_pa.setTimeSubmitted(new java.util.Date());
							this_pa.setSubmitted(true);
						}
						this_pa.saveMe(schema);
					}
				}
			}
		}

	}

	/**
	 * Handles the creation of questions to be added to the simulation. This
	 * method is called at the top of the jsp. It can be called for several
	 * reasons. 
	 * 1.) Player is just entering form. Method should return a new,
	 * 'unsaved' question. 
	 * 2.) Player hits the create button. Method should
	 * return the question created. 
	 * 3.) Player select one of the existing
	 * questions to queue it up for editing. Method should return the question selected.
	 * 4.) Player hit the clear button, so method should return a new, 'unsaved'
	 * question. 
	 * 5.) Player hit the update button, so method should update the
	 * question and then return it.
	 * 
	 * @param request
	 */
	public static QuestionAndResponse handleCreateQuestion(
			HttpServletRequest request, SessionObjectBase sob) {

		QuestionAndResponse this_question = new QuestionAndResponse();

		// If the player cleared the form, return the blank document.
		String clear_button = (String) request.getParameter("clear_button");
		if (clear_button != null) {
			return this_question;
		}

		// If we got passed in a doc id, use it to retrieve the doc we are
		// working on.
		String question_id = (String) request.getParameter("question_id");
		if ((question_id != null) && (question_id.trim().length() > 0)) {
			this_question = QuestionAndResponse.getById(sob.schema, new Long(
					question_id));
		}

		// If player just entered this page from a different form, just return
		// the blank document
		String sending_page = (String) request.getParameter("sending_page");
		if ((sending_page == null)
				|| (!(sending_page.equalsIgnoreCase("create_question")))) {
			return this_question;
		}

		// If we got down to here, we must be doing some real work on a
		// document.
		String qtag = (String) request.getParameter("qtag");
		String question = (String) request.getParameter("question");
		String answer = (String) request.getParameter("answer");

		// Do create if called.
		String create_doc = (String) request.getParameter("create_question");
		if ((create_doc != null)) {
			this_question = new QuestionAndResponse(sob.schema, sob.sim_id,
					qtag, question, answer);
			this_question.saveMe(sob.schema);

		}

		// Do update if called.
		String update_doc = (String) request.getParameter("update_question");
		if ((update_doc != null)) {
			this_question.setQuestionIdentifier(qtag);
			this_question.setQuestion(question);
			this_question.setSimId(sob.sim_id);
			this_question.setAnswer(answer);
			this_question.saveMe(sob.schema);

		}

		return this_question;

	}

	/**
	 * Returns the ids and postions for this set of questions for this section.
	 * 
	 * @param schema
	 * @param bs_id
	 * @param className
	 * @return
	 */
	public static Hashtable getMyQuestions(String schema, Long bs_id,
			String className) {

		Hashtable returnHash = new Hashtable();

		List bssdoas = BaseSimSectionDepObjectAssignment.getObjectsForSection(
				schema, bs_id, className);

		for (ListIterator<BaseSimSectionDepObjectAssignment> li = bssdoas
				.listIterator(); li.hasNext();) {
			BaseSimSectionDepObjectAssignment bssdoa = li.next();

			returnHash.put(bssdoa.getObjectId(), bssdoa.getDepObjIndex() + "");

		}

		return returnHash;

	}

}
