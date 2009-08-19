package org.usip.osp.communications;

/**
 * This interface ensures that there are places to store all of the information surrounding a
 * typical event that can happen to a player to help drive them toward meaningful 
 * learning outcomes.
 *
 *
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
public interface LearningEvent {

	public String getLearningObjectives();

	public void setLearningObjectives(String learningObjectives);

	public String getTypicalQuestionsAndResponses();

	public void setTypicalQuestionsAndResponses(String typicalQuestionsAndResponses);
	
}
