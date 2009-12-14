package org.usip.osp.networking;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import org.usip.osp.baseobjects.*;
import org.usip.osp.communications.ConvActorAssignment;
import org.usip.osp.communications.Conversation;
import org.usip.osp.communications.Event;
import org.usip.osp.communications.Inject;
import org.usip.osp.communications.InjectGroup;
import org.usip.osp.communications.TimeLine;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.log4j.*;

/**
 * Packages and unpackages objects to XML using the opensource software library
 * XStream.
 * 
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 * 
 */
public class ObjectPackager {

	public static final String lineTerminator = "\r\n"; //$NON-NLS-1$

	/** Creates an opening tag for the XML surrounding an object. */
	public static String makeOpenTag(Class thisClass) {
		return "<" + thisClass.getName() + ">";
	}

	/** Creates a closing tag for the XML surrounding an object. */
	public static String makeCloseTag(Class thisClass) {
		return "</" + thisClass.getName() + ">";
	}

	public static void main(String[] args) {

		// Logger.getRootLogger().debug(packageSimulation("test", new Long(1)));

		play();

	}

	public static void play() {
		BaseSimSection bss_intro = new BaseSimSection("test", "", "../osp_core", "introduction.jsp", "Introduction", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"Your introductory text will be shown here."); //$NON-NLS-1$

		bss_intro.setCreatingOrganization("org.usip.osp."); //$NON-NLS-1$
		bss_intro.setUniqueName("Cast"); //$NON-NLS-1$
		bss_intro.setVersion("1"); //$NON-NLS-1$

		XStream xstream = new XStream();

		xstream.alias("bss", BaseSimSection.class); //$NON-NLS-1$

		Logger.getRootLogger().debug(xstream.toXML(bss_intro));

	}

	public static Object unpackageXML() {

		return null;
	}

	/**
	 * Returns the xml for an object. This can be used to inspect the xml and
	 * see how it is expected to be formatted.
	 * 
	 * @param obj
	 * @return
	 */
	public static String getObjectXML(Object obj) {

		XStream xstream = new XStream();

		return xstream.toXML(obj);

	}

	/**
	 * This prepares a simulation for transmittal and sharing.
	 * 
	 * @param simulation
	 * @return
	 */
	public static String packageSimulation(String schema, Long sim_id) {

		XStream xstream = new XStream();

		Simulation sim = Simulation.getMe(schema, sim_id);
		sim.setTransit_id(sim.getId());
		sim.setId(null);

		String returnString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<SIM_PACKAGE_OBJECT>" + lineTerminator; //$NON-NLS-1$

		returnString += "<OSP_VERSION>" + USIP_OSP_Properties.getRelease() + "</OSP_VERSION>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ lineTerminator;

		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat();

		returnString += "<EXPORT_DATE>" + sdf.format(today) + "</EXPORT_DATE>" + lineTerminator; //$NON-NLS-1$ //$NON-NLS-2$

		// This packages the values directly associate with the simulation such
		// as objectives and audience.
		returnString += xstream.toXML(sim);

		returnString += packageActors(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packagePhases(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packagePhaseAssignments(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageInjects(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageBaseSimSectionInformation(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageSimSectionAssignmentInformation(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageSimObjectInformation(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageMiscSimObjectInformation(schema, sim.getTransit_id(), xstream) + lineTerminator;

		returnString += "</SIM_PACKAGE_OBJECT>"; //$NON-NLS-1$

		return returnString;

	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packageSimSectionAssignmentInformation(String schema, long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$

		for (ListIterator<SimulationSectionAssignment> li = SimulationSectionAssignment.getBySim(schema, sim_id)
				.listIterator(); li.hasNext();) {
			SimulationSectionAssignment thisSection = li.next();

			thisSection.setTransit_id(thisSection.getId());
			thisSection.setId(null);

			returnString += xstream.toXML(thisSection) + lineTerminator;
		}

		return returnString;
	}
	
	/**
	 * I'm adding this right now to package up the TimeLine object, which may or may not be associated with 
	 * a base sim section, and seems pertinent to only one simulation. 
	 * There will be a better way to package these things up later, I'm sure.
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packageMiscSimObjectInformation(String schema, Long sim_id, XStream xstream) {
		
		TimeLine tl = TimeLine.getMasterPlan(schema, sim_id.toString());
		
		String returnString = "";
		
		returnString += xstream.toXML(tl) + lineTerminator;
		
		List<Event> allEvents = Event.getAllForTimeLine(tl.getId(), schema);
		
		for (ListIterator<Event> li = allEvents.listIterator(); li.hasNext();) {
			Event thisEvent = li.next();
		
			returnString += xstream.toXML(thisEvent) + lineTerminator;
			
		}
		
		return returnString;
	}

	/**
	 * Right now this does documents, but we need to find a way to do it
	 * generically for all objects that have been declared as simulation object
	 * (by implementing the interface SimSectionDependentObject )
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packageSimObjectInformation(String schema, long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$

		// Keeps a list of items stored so we don't store the same item twice.
		Hashtable previouslyStoredObjects = new Hashtable();

		// Get dependency (bssdoa), Get object, add bssdoa xml, add object xml
		// (if not added already)
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = BaseSimSectionDepObjectAssignment.getSimDependencies(
				schema, sim_id).listIterator(); li.hasNext();) {

			BaseSimSectionDepObjectAssignment bssdoa = li.next();

			bssdoa.setTransit_id(bssdoa.getId());
			bssdoa.setId(null);

			returnString += xstream.toXML(bssdoa) + lineTerminator;

			// We need to make sure that an object is not saved multiple times
			// to the xml
			SimSectionDependentObject depObj = BaseSimSectionDepObjectAssignment.pullOutObject(schema, bssdoa);
			Logger.getRootLogger().debug(depObj.getClass());
			Logger.getRootLogger().debug(depObj.getId());

			String hash_key_string = depObj.getClass() + "_" + depObj.getId(); //$NON-NLS-1$

			String checkString = (String) previouslyStoredObjects.get(hash_key_string);

			Logger.getRootLogger().debug("hash_key_string was: " + hash_key_string); //$NON-NLS-1$
			Logger.getRootLogger().debug("check string was: " + checkString); //$NON-NLS-1$

			if (checkString == null) {

				previouslyStoredObjects.put(hash_key_string, "set"); //$NON-NLS-1$
				Logger.getRootLogger().debug("put hash_key_string: " + hash_key_string); //$NON-NLS-1$

				depObj.setTransit_id(depObj.getId());
				depObj.setId(null);

				returnString += xstream.toXML(depObj) + lineTerminator;

				// Some dependent objects (such as conversations) have sub
				// objects.
				if (depObj.getClass().equals(Conversation.class)) {
					// Get list of conversation actors
					for (ListIterator<ConvActorAssignment> lcaa = ConvActorAssignment.getAllForConversation(schema,
							depObj.getTransit_id()).listIterator(); lcaa.hasNext();) {
						ConvActorAssignment caa = lcaa.next();
						returnString += xstream.toXML(caa) + lineTerminator;
					}

				}
			}

		}

		return returnString;
	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packageBaseSimSectionInformation(String schema, long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$
		// Get list of base section ids
		List setOfBaseSections = SimulationSectionAssignment.getBaseIdsBySim(schema, sim_id);

		for (ListIterator<Long> li = setOfBaseSections.listIterator(); li.hasNext();) {
			Long thisBaseId = li.next();

			BaseSimSection bss = BaseSimSection.getMe(schema, thisBaseId.toString());

			if (bss.getClass().getName().equalsIgnoreCase(BaseSimSection.class.getName())) {
				bss.setTransit_id(bss.getId());
				bss.setId(null);
				returnString += xstream.toXML(bss) + lineTerminator;
			} else if (bss.getClass().getName().equalsIgnoreCase(CustomizeableSection.class.getName())) {

				bss = null;
				CustomizeableSection cbss = CustomizeableSection.getMe(schema, thisBaseId.toString());
				cbss.setTransit_id(cbss.getId());
				cbss.setId(null);
				returnString += xstream.toXML(cbss) + lineTerminator;
			} else {
				Logger.getRootLogger().debug("Warning in Object Packager. Unknown object."); //$NON-NLS-1$
			}

		}

		return returnString;
	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packageInjects(String schema, long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$

		List<InjectGroup> allInjectGroups = InjectGroup.getAllForSim(schema, sim_id);
		for (ListIterator<InjectGroup> li = allInjectGroups.listIterator(); li.hasNext();) {
			InjectGroup thisInjectGroup = li.next();

			thisInjectGroup.setTransit_id(thisInjectGroup.getId());
			thisInjectGroup.setId(null);

			returnString += xstream.toXML(thisInjectGroup) + lineTerminator;

			List<Inject> allInjects = Inject.getAllForSimAndGroup(schema, sim_id, thisInjectGroup.getTransit_id());

			for (ListIterator<Inject> li_i = allInjects.listIterator(); li_i.hasNext();) {
				Inject thisInject = li_i.next();

				thisInject.setTransit_id(thisInject.getId());
				thisInject.setId(null);

				returnString += xstream.toXML(thisInject) + lineTerminator;

			}
		}
		return returnString;

	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packageActors(String schema, long sim_id, XStream xstream) {
		String returnString = ""; //$NON-NLS-1$
		List<Actor> allActors = SimActorAssignment.getActorsForSim(schema, sim_id);
		for (ListIterator<Actor> li = allActors.listIterator(); li.hasNext();) {
			Actor thisActor = li.next();

			thisActor.setTransit_id(thisActor.getId());
			thisActor.setId(null);

			returnString += xstream.toXML(thisActor) + lineTerminator;
		}
		return returnString;
	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packagePhases(String schema, long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$
		List<SimulationPhase> allPhases = SimPhaseAssignment.getPhasesForSim(schema, sim_id);
		for (ListIterator<SimulationPhase> li = allPhases.listIterator(); li.hasNext();) {
			SimulationPhase thisPhase = li.next();

			thisPhase.setTransit_id(thisPhase.getId());
			thisPhase.setId(null);

			returnString += xstream.toXML(thisPhase) + lineTerminator;
		}
		return returnString;
	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packagePhaseAssignments(String schema, long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$
		List<SimPhaseAssignment> allPhases = SimPhaseAssignment.getPhasesAssignmentsForSim(schema, sim_id);
		for (ListIterator<SimPhaseAssignment> li = allPhases.listIterator(); li.hasNext();) {
			SimPhaseAssignment thisPhaseAssignment = li.next();

			thisPhaseAssignment.setTransit_id(thisPhaseAssignment.getId());
			thisPhaseAssignment.setId(null);

			returnString += xstream.toXML(thisPhaseAssignment) + lineTerminator;
		}
		return returnString;
	}

	/**
	 * This method pulls out a bit of information about the simulation to show
	 * to the person about to import it. Most importantly it pulls out the name
	 * and version of the simulation to be extracted to display that to the
	 * person doing the import to allow them to change it if they desire to do
	 * so.
	 * 
	 * @param fileloc
	 * @param schema
	 * @return
	 */
	public static Simulation unpackSimDetails(String fileloc, String schema) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("sim", Simulation.class); //$NON-NLS-1$

		String fileLocation = FileIO.packaged_sim_dir + File.separator + fileloc;

		Logger.getRootLogger().debug("looking for file to unpack at " + fileLocation); //$NON-NLS-1$

		String fullString = FileIO.getFileContents(new File(fileLocation));

		String simString = getObjectFromFile(fullString, makeOpenTag(Simulation.class), makeCloseTag(Simulation.class)); //$NON-NLS-1$

		Simulation simRead = (Simulation) xstream.fromXML(simString);

		return simRead;
	}

	public static String unpackInformationString = ""; //$NON-NLS-1$

	/**
	 * 
	 * @param fileloc
	 * @param schema
	 */
	public static void unpackSim(String fileloc, String schema, String sim_name, String sim_version) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("sim", Simulation.class); //$NON-NLS-1$

		Hashtable actorIdMappings = new Hashtable();
		// We use the actor id of 0 in the sections table to indicate that it is a 'universal' section
		actorIdMappings.put(new Long(0), new Long(0));

		Hashtable phaseIdMappings = new Hashtable();
		Hashtable bssIdMappings = new Hashtable();

		String fileLocation = FileIO.packaged_sim_dir + File.separator + fileloc;

		Logger.getRootLogger().debug("looking for file to unpack at " + fileLocation); //$NON-NLS-1$

		String fullString = FileIO.getFileContents(new File(fileLocation));

		String simString = getObjectFromFile(fullString, makeOpenTag(Simulation.class), //$NON-NLS-1$
				makeCloseTag(Simulation.class)); //$NON-NLS-1$

		Simulation simRead = (Simulation) xstream.fromXML(simString);

		simRead.setName(sim_name);
		simRead.setVersion(sim_version);

		simRead.saveMe(schema);

		unpackInformationString += "<b>Unpacking Actors</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackageActors(schema, fullString, simRead.getId(), xstream, actorIdMappings);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Actors Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$
		unpackInformationString += "<b>Unpacking Phases</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackagePhases(schema, fullString, simRead.getId(), xstream, phaseIdMappings);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Phases Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$
		unpackInformationString += "<b>Unpacking Injects</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackageInjects(schema, fullString, simRead.getId(), xstream);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Injects Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$
		unpackInformationString += "<b>Unpacking Base Simulation Sections</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackageBaseSimSections(schema, fullString, simRead.getId(), xstream, bssIdMappings);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Base Sim Sections Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$
		unpackInformationString += "<b>Unpacking Customizeable Sim Sections</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackageCustomizeableSimSections(schema, fullString, simRead.getId(), xstream,
				bssIdMappings);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Customizeable Sections Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$
		unpackInformationString += "<b>Unpacking Customized Sim Sections</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackageCustomizedSimSections(schema, fullString, simRead.getId(), xstream,
				bssIdMappings);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Customizeable Sections Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$
		unpackInformationString += "<b>Unpacking Simulation Sections</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackageSimSectionAssignmentss(schema, fullString, simRead.getId(), xstream,
				actorIdMappings, phaseIdMappings, bssIdMappings);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Simulation Sections Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$
		unpackInformationString += "<b>Unpacking Simulation Objects</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackageSimObjects(schema, fullString, simRead.getId(), xstream, bssIdMappings, actorIdMappings);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Simulation Objects Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$

		
		unpackInformationString += "<b>Unpacking Misc Simulation Objects</b><br />"; //$NON-NLS-1$
		unpackInformationString += "<blockquote>"; //$NON-NLS-1$
		unpackInformationString += unpackageMiscSimObjects(schema, fullString, simRead.getId(), xstream, bssIdMappings, actorIdMappings);
		unpackInformationString += "</blockquote>"; //$NON-NLS-1$
		unpackInformationString += "<b>Misc Simulation Objects Unpacked</b><br />"; //$NON-NLS-1$
		unpackInformationString += "--------------------------------------------------------------------<br />"; //$NON-NLS-1$

		
		// ? documents, variables, conversations, etc.

	}
	
	/**
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 * @param bssIdMappings
	 * @param actorIdMappings
	 * @return
	 */
	public static String unpackageMiscSimObjects(String schema, String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings, Hashtable actorIdMappings) {
		
		String returnString = "";
		
		String timelineString = getObjectFromFile(fullString,
				makeOpenTag(TimeLine.class),
				makeCloseTag(TimeLine.class));
		
		TimeLine timeline = (TimeLine) xstream.fromXML(timelineString);
		
		if (timeline == null){
			timeline = new TimeLine();
		}
		
		Long timeline_orig_id = timeline.getId();
		
		TimeLine thisMaster = TimeLine.getMasterPlan(schema, sim_id.toString());
		
		timeline.setId(thisMaster.getId());
		timeline.setSimId(sim_id);
		timeline.saveMe(schema);
		
		List<String> event_list = getSetOfObjectFromFile(fullString,
				makeOpenTag(Event.class), makeCloseTag(Event.class));
		
		for (ListIterator<String> li_i = event_list.listIterator(); li_i.hasNext();) {
			String e_string = li_i.next();
			
			Event event = (Event) xstream.fromXML(e_string);
			
			if (event.getTimelineId().equals(timeline_orig_id)){
				
				returnString += "got event: " + event.getEventTitle() + "<br />";
				
				// The id this had on the system it was exported from bears no relationship to the id where its being imported.
				event.setId(null);
				event.setTimelineId(timeline.getId());
				event.setSimId(sim_id);
				event.saveMe(schema);
			}
		}
		
		return returnString;
	}

	/**
	 * Pulls the simulation object out of the packaged file. Right now we are
	 * targeting specific types (documents, etc.), but eventually we want to be
	 * able to pull out any unspecified type that implements the
	 * SimSectionDependentObject interface.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackageSimObjects(String schema, String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings, Hashtable actorIdMappings) {

		// Get object dependencies
		String returnString = "Getting Dependent Object Assignments <BR />"; //$NON-NLS-1$

		// Fill this up to import objects first, and then remap the bssdoa to
		// them and the base sim section
		Hashtable<String, String> setOfObjectClassesToGet = new Hashtable();

		List<String> bssdoa_list = getSetOfObjectFromFile(fullString,
				makeOpenTag(BaseSimSectionDepObjectAssignment.class),
				makeCloseTag(BaseSimSectionDepObjectAssignment.class));
		for (ListIterator<String> li_i = bssdoa_list.listIterator(); li_i.hasNext();) {
			String sd_string = li_i.next();

			BaseSimSectionDepObjectAssignment this_bssdoa = (BaseSimSectionDepObjectAssignment) xstream
					.fromXML(sd_string);

			setOfObjectClassesToGet.put(this_bssdoa.getClassName(), "set");
		}

		Hashtable dependentObjectMappings = new Hashtable();
		// Get objects belonging to the classes just found
		for (Enumeration e = setOfObjectClassesToGet.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();

			String startXMLTag = "<" + key + ">";
			String endXMLTag = "</" + key + ">";
			returnString += "Looking for objects of class: " + key + "<br />";

			List dos_list = getSetOfObjectFromFile(fullString, startXMLTag, endXMLTag);

			for (ListIterator<String> li_i = dos_list.listIterator(); li_i.hasNext();) {
				String sd_string = li_i.next();

				SimSectionDependentObject this_dos = (SimSectionDependentObject) xstream.fromXML(sd_string);

				// map sim id back to the new sim id.
				this_dos.setSimId(sim_id);

				// Save object, map its new id to the transit id
				this_dos.saveMe(schema);
				
				dependentObjectMappings.put(this_dos.getTransit_id(), this_dos.getId());

				returnString += "Found Dependent Object of class " + key + " and it had a transit id of "
						+ this_dos.getTransit_id() + " which was mapped to an id of " + this_dos.getId() + "<BR />";

				Logger.getRootLogger().debug(
						"Found Dependent Object of class " + key + " and it had a transit id of "
								+ this_dos.getTransit_id() + " which was mapped to an id of " + this_dos.getId()
								+ "<BR />");
				
				// Conversations have conversation actor assignments associated with them.
				if (this_dos.getClass().equals(Conversation.class)){
					returnString += unpackConversationActorAssignments(
							schema, fullString, this_dos.getTransit_id(), this_dos.getId(), xstream, actorIdMappings);
				}
			}
		}

		// Now go back through the bssdoas, remap the values of bss id and dep
		// obj. id, and then save them.
		for (ListIterator<String> li_i = bssdoa_list.listIterator(); li_i.hasNext();) {
			String sd_string = li_i.next();

			BaseSimSectionDepObjectAssignment this_bssdoa = (BaseSimSectionDepObjectAssignment) xstream
					.fromXML(sd_string);

			Logger.getRootLogger().debug("this_bssdoa.getBss_id() was " + this_bssdoa.getBss_id());
			System.out.flush();

			this_bssdoa.setSim_id(sim_id);

			try {
				Long thisMappedId = (Long) bssIdMappings.get(this_bssdoa.getBss_id());
				this_bssdoa.setBss_id(thisMappedId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			this_bssdoa.setObjectId((Long) dependentObjectMappings.get(this_bssdoa.getObjectId()));

			this_bssdoa.saveMe(schema);

			returnString += "Found bssdoa. Sim id / bss id / obj id: " + this_bssdoa.getSim_id() + " / "
					+ this_bssdoa.getBss_id() + " / " + this_bssdoa.getObjectId() + "<BR />";
		}

		return returnString;

	}
	
	/**
	 * Unpacks these 'sub' objects. Eventually need to find a way to do this with all such objects.
	 * 
	 * @param schema
	 * @param fullString
	 * @param orig_id
	 * @param new_id
	 * @param xstream
	 * @param actorIdMappings
	 * @return
	 */
	public static String unpackConversationActorAssignments(
			String schema, String fullString, Long orig_id, Long new_id, XStream xstream, Hashtable actorIdMappings){
		
		String returnString = "... unpacking conversation actor assignments.<br />";
		
		List<String> caa_list = getSetOfObjectFromFile(fullString,
				makeOpenTag(ConvActorAssignment.class),
				makeCloseTag(ConvActorAssignment.class));
		
		/* Get full set of conversations. Only save the ones we are adding for this conversation. */
		for (ListIterator<String> li_i = caa_list.listIterator(); li_i.hasNext();) {
			String caa_string = li_i.next();

			ConvActorAssignment this_caa = (ConvActorAssignment) xstream
					.fromXML(caa_string);
			
			if (this_caa.getConv_id().equals(orig_id)){
				
				// The id this had on the system it was exported from bears no relationship to the id where its being imported.
				this_caa.setId(null);
				
				this_caa.setConv_id(new_id);
				
				Long newActorId = (Long) actorIdMappings.get(this_caa.getActor_id());
				
				this_caa.setActor_id(newActorId);
				
				returnString += "...... added actor id " + newActorId + ".<br />";
			
				System.out.println("Trying to save to schema: " + schema);
				this_caa.saveMe(schema);
				
			}
			
		}
		
		return returnString;
	}

	/**
	 * Pulls the base sim sections out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackageSimSectionAssignmentss(String schema, String fullString, Long sim_id,
			XStream xstream, Hashtable actorIdMappings, Hashtable phaseIdMappings, Hashtable bssIdMappings) {

		String returnString = "";

		List bsss = getSetOfObjectFromFile(fullString, makeOpenTag(SimulationSectionAssignment.class),
				makeCloseTag(SimulationSectionAssignment.class));
		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			SimulationSectionAssignment this_ss = (SimulationSectionAssignment) xstream.fromXML(act_string);

			this_ss.setSim_id(sim_id);
			this_ss.setActor_id((Long) actorIdMappings.get(this_ss.getActor_id()));
			this_ss.setPhase_id((Long) phaseIdMappings.get(this_ss.getPhase_id()));
			this_ss.setBase_sec_id((Long) bssIdMappings.get(this_ss.getBase_sec_id()));

			this_ss.save(schema);

			if (this_ss.isSimSubSection()) {
				Long x = this_ss.getDisplaySectionId();
				if (x == null) {
					System.out.println("The display section of a sub section should not be null.");
				} else {
					Long y = (Long) bssIdMappings.get(x);
					if (y == null) {
						System.out.println("got null back from the hash table.");
					} else {
						this_ss.setDisplaySectionId(y);
						this_ss.save(schema);
					}
				}

			}

			returnString += "Found " + this_ss.getTab_heading() + " and it had id " + this_ss.getId() + "<br />";

		}

		return returnString;

	}

	/**
	 * Pulls the base sim sections out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackageCustomizeableSimSections(String schema, String fullString, Long sim_id,
			XStream xstream, Hashtable bssIdMappings) {

		String returnString = "";

		List bsss = getSetOfObjectFromFile(fullString, makeOpenTag(CustomizeableSection.class),
				makeCloseTag(CustomizeableSection.class));

		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			CustomizeableSection this_bss = (CustomizeableSection) xstream.fromXML(act_string);

			if (!(this_bss.isThisIsACustomizedSection())) {
				CustomizeableSection correspondingSimSection = (CustomizeableSection) CustomizeableSection.getByName(
						schema, this_bss.getCreatingOrganization(), this_bss.getUniqueName(), this_bss.getVersion());

				if (correspondingSimSection == null) {
					String warnString = "<font color=\"red\"> Warning. CustomizeableSection simulation section "
							+ this_bss.getVersionInformation() + " not found.<br /></font>";
					Logger.getRootLogger().debug(warnString);
					returnString += warnString;
				} else {
					returnString += "Found " + this_bss.getUniqueName() + " and it had id "
							+ correspondingSimSection.getId() + "<br />";
					bssIdMappings.put(this_bss.getTransit_id(), correspondingSimSection.getId());
				}
			}
		}

		return returnString;

	}

	/**
	 * Pulls the base sim sections out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackageCustomizedSimSections(String schema, String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings) {

		String returnString = "";

		List bsss = getSetOfObjectFromFile(fullString, makeOpenTag(CustomizeableSection.class),
				makeCloseTag(CustomizeableSection.class));
		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			CustomizeableSection this_bss = (CustomizeableSection) xstream.fromXML(act_string);

			if (this_bss.isThisIsACustomizedSection()) {

				this_bss.saveMe(schema);
				returnString += "Found " + this_bss.getUniqueName() + " and gave it id " + this_bss.getId() + "<br />";

				bssIdMappings.put(this_bss.getTransit_id(), this_bss.getId());

			}
		}

		return returnString;

	}

	/**
	 * Pulls the base sim sections out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackageBaseSimSections(String schema, String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings) {

		String returnString = "";

		List bsss = getSetOfObjectFromFile(fullString, makeOpenTag(BaseSimSection.class),
				makeCloseTag(BaseSimSection.class));

		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			BaseSimSection this_bss = (BaseSimSection) xstream.fromXML(act_string);

			BaseSimSection correspondingSimSection = BaseSimSection.getByName(schema, this_bss
					.getCreatingOrganization(), this_bss.getUniqueName(), this_bss.getVersion());

			if (correspondingSimSection == null) {
				String warnString = "<font color=\"red\"> Warning. Base simulation section "
						+ this_bss.getVersionInformation() + " ( id : " + this_bss.getTransit_id()
						+ ") not found.<br /></font>";
				Logger.getRootLogger().debug(warnString);
				returnString += warnString;
			} else {
				returnString += "Found " + this_bss.getUniqueName() + " and it had id "
						+ correspondingSimSection.getId() + "<br />";
				bssIdMappings.put(this_bss.getTransit_id(), correspondingSimSection.getId());
			}

		}

		return returnString;

	}

	/**
	 * Pulls the actors out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackageActors(String schema, String fullString, Long sim_id, XStream xstream,
			Hashtable actorIdMappings) {

		String returnString = "";

		ArrayList actorNames = Actor.getAllActorNames(schema);

		List actors = getSetOfObjectFromFile(fullString, makeOpenTag(Actor.class), makeCloseTag(Actor.class));
		for (ListIterator<String> li_i = actors.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			Actor this_act = (Actor) xstream.fromXML(act_string);

			// Set the id of the simulation associated with this actor to be the
			// new simulation id.
			this_act.setSim_id(sim_id);

			String originalName = this_act.getName();

			this_act.setName(getUniqueUsersName(actorNames, this_act.getName()));

			this_act.saveMe(schema);
			returnString += "Actor " + originalName + " addes as " + this_act.getName();

			actorIdMappings.put(this_act.getTransit_id(), this_act.getId());

			@SuppressWarnings("unused")
			SimActorAssignment saa = new SimActorAssignment(schema, sim_id, this_act.getId());
		}

		return returnString;

	}

	/**
	 * Pulls the actors out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackagePhases(String schema, String fullString, Long sim_id, XStream xstream,
			Hashtable phaseIdMappings) {

		String returnString = "";
		List phases = getSetOfObjectFromFile(fullString, makeOpenTag(SimulationPhase.class),
				makeCloseTag(SimulationPhase.class));
		for (ListIterator<String> li_i = phases.listIterator(); li_i.hasNext();) {
			String phase_string = li_i.next();

			SimulationPhase this_phase = (SimulationPhase) xstream.fromXML(phase_string);

			this_phase.saveMe(schema);

			returnString += "Phase " + this_phase.getName() + " added to simulation";

			phaseIdMappings.put(this_phase.getTransit_id(), this_phase.getId());

			@SuppressWarnings("unused")
			SimPhaseAssignment spa = new SimPhaseAssignment(schema, sim_id, this_phase.getId());
		}

		return returnString;

	}

	/**
	 * Pulls the actors out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackageInjects(String schema, String fullString, Long sim_id, XStream xstream) {

		String returnString = "Unpackaging Inject Groups and Injects.";
		Hashtable injectGroupIds = new Hashtable();

		List injectGroups = getSetOfObjectFromFile(fullString, makeOpenTag(InjectGroup.class),
				makeCloseTag(InjectGroup.class));

		for (ListIterator<String> li_i = injectGroups.listIterator(); li_i.hasNext();) {
			String injectGroup_string = li_i.next();

			InjectGroup ig = (InjectGroup) xstream.fromXML(injectGroup_string);

			ig.setSim_id(sim_id);
			ig.saveMe(schema);

			injectGroupIds.put(ig.getTransit_id(), ig.getId());

		}

		List injects = getSetOfObjectFromFile(fullString, makeOpenTag(Inject.class), makeCloseTag(Inject.class));

		for (ListIterator<String> li_i = injects.listIterator(); li_i.hasNext();) {
			String inject_string = li_i.next();

			Inject ig = (Inject) xstream.fromXML(inject_string);

			ig.setGroup_id((Long) injectGroupIds.get(ig.getGroup_id()));
			ig.setSim_id(sim_id);
			ig.saveMe(schema);

		}

		return returnString;
	}

	/**
	 * 
	 * @param listOfNames
	 * @param thisName
	 * @return
	 */
	public static String getUniqueUsersName(List<String> listOfNames, String thisName) {

		if (!(listOfNames.contains(thisName))) {
			listOfNames.add(thisName);
			return thisName;
		} else {

			for (int ii = 1; ii <= 1000; ++ii) {
				String nameToCheck = thisName + "_" + ii;
				if (!(listOfNames.contains(nameToCheck))) {
					listOfNames.add(nameToCheck);
					return nameToCheck;
				}
			}
		}
		return "ran out of names";
	}

	/**
	 * This gets a single object out of a file based on start and end tags.
	 * 
	 * @param fullContents
	 * @param startString
	 * @param endString
	 * @return
	 */
	public static String getObjectFromFile(String fullContents, String startString, String endString) {

		int findStartOfMatch = fullContents.lastIndexOf(startString);
		int findEndOfMatch = fullContents.lastIndexOf(endString) + endString.length();

		return fullContents.substring(findStartOfMatch, findEndOfMatch);

	}

	/**
	 * This gets multiple objects out of a file based on its start and end tags.
	 * 
	 * @param thisFile
	 * @param startString
	 * @param endString
	 * @return
	 */
	public static List getSetOfObjectFromFile(String fullContents, String startString, String endString) {

		ArrayList returnList = new ArrayList();

		boolean exitLoop = false;

		while (!exitLoop) {
			int findStartOfMatch = fullContents.lastIndexOf(startString);
			int findEndOfMatch = fullContents.lastIndexOf(endString) + endString.length();

			if (findStartOfMatch > 0) {
				returnList.add(fullContents.substring(findStartOfMatch, findEndOfMatch));
				fullContents = fullContents.substring(0, findStartOfMatch);
			} else {
				exitLoop = true;
			}
		}

		// Since the above get things from the end of the file and works
		// backwards, we reverse the order
		// to make the imported objects come in in the same order in which they
		// were exported.
		Collections.reverse(returnList);

		return returnList;

	}

	/**
	 * This works with the method 'compare' to create xml return string.
	 * 
	 * @param startTag
	 * @param endTag
	 * @param value
	 * @return
	 */
	public static String addResultsToXML(String startTag, String endTag, boolean value) {

		String returnString = startTag;

		if (value) {
			returnString += "Same";
		} else {
			returnString += "Different";
		}

		returnString += endTag;

		return returnString;
	}
}
