package com.seachangesimulations.osp.questions;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.CustomizeableSection;
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

	@Override
	public void handleCustomizeSection(HttpServletRequest request,
			SessionObjectBase afso, CustomizeableSection cs) {

		String sending_page = request.getParameter("sending_page");
		
		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("questions_view"))) {
			
			handleCustomizeQuestionsView(request, afso, cs);
			return;
		}

		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$

			Logger.getRootLogger().debug(
					"QuestionCustomizer.handleCustomizeSection.save_results"); //$NON-NLS-1$

			cs.setBigString(request.getParameter("cs_bigstring"));

			cs.getContents().put(KEY_FOR_PAGETITLE,
					request.getParameter(KEY_FOR_PAGETITLE));
			
			cs.getContents().put(KEY_FOR_POSTANSWERTEXT,
					request.getParameter(KEY_FOR_POSTANSWERTEXT));

			cs.saveMe(afso.schema);

			Hashtable questions = new Hashtable();
			Hashtable answers = new Hashtable();
			Hashtable q_tags = new Hashtable();

			// Loop over the request passed in and pull out questions and
			// response.
			for (Enumeration<String> e = request.getParameterNames(); e
					.hasMoreElements();) {
				String param_name = (String) e.nextElement();
				String value_name = (String) request.getParameter(param_name);

				if (param_name.startsWith("question_")) {
					param_name = param_name.replaceAll("question_", "");

					questions.put(param_name, value_name);

					String answer_value_name = (String) request
							.getParameter("answer_" + param_name);

					answers.put(param_name, answer_value_name);

					String qtag_value_name = (String) request
							.getParameter("qtag_" + param_name);

					q_tags.put(param_name, qtag_value_name);

				}
			} // end of loop over parameter names

			// Need to create Base Sim Section Dependent Objects to signal these
			// QandRs are for this one.
			BaseSimSectionDepObjectAssignment.removeAllForSection(afso.schema,
					cs.getId());

			// Need to remove any previous q and rs for this section also.
			QuestionAndResponse.deleteAllForSimAndCustomSection(afso.schema,
					afso.sim_id, cs.getId());

			for (Enumeration e = questions.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String value = (String) questions.get(key);
				String answer_value = (String) answers.get(key);
				String qtag_value = (String) q_tags.get(key);

				int qIndex = new Long(key).intValue();

				QuestionAndResponse qAndR = new QuestionAndResponse(
						afso.schema, afso.sim_id, cs.getId(), qIndex,
						qtag_value, value, answer_value);

				@SuppressWarnings("unused")
				BaseSimSectionDepObjectAssignment bssdoaQandR = new BaseSimSectionDepObjectAssignment(
						cs.getId(), "com.seachangesimulations.osp.questions",
						qIndex, qAndR.getId(), afso.sim_id, afso.schema);
			}

		}

	}
	
	public void handleCustomizeQuestionsView(HttpServletRequest request,
			SessionObjectBase afso, CustomizeableSection cs) {
		
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
			CustomizeableSection cs, String schema, Long simId, Long rsId, Long actorId, Long userId) {

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

						// Setting these values to what was passed in, since if this is new they will be blank.
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

}
