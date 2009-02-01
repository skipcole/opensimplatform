package org.usip.osp.networking;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import org.usip.osp.baseobjects.*;
import org.usip.osp.communications.Inject;
import org.usip.osp.communications.InjectGroup;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Packages and unpackages objects to XML using the opensource software library
 * XStream.
 * 
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
public class ObjectPackager {

	public static final String ObjectDelimiter = "<------------------------------------------>\r\n";

	public static void main(String[] args) {

		// System.out.println(packageSimulation("test", new Long(1)));

		play();

	}

	public static void play() {
		BaseSimSection bss_intro = new BaseSimSection("test", "", "../osp_core", "introduction.jsp", "Introduction",
				"Your introductory text will be shown here.");

		bss_intro.setCreatingOrganization("org.usip.osp.");
		bss_intro.setUniqueName("Cast");
		bss_intro.setVersion("1");

		XStream xstream = new XStream();

		xstream.alias("bss", BaseSimSection.class);

		System.out.println(xstream.toXML(bss_intro));

	}

	public static Object unpackageXML() {

		return null;
	}
	
	/**
	 * Returns the xml for an object. This can be used to inspect the xml and see how it is 
	 * expected to be formatted.
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

		String returnString = xstream.toXML(sim);

		returnString += "<------------------------------------------------------->";
		returnString += packageActors(schema, sim.getTransit_id(), xstream);
		returnString += "<------------------------------------------------------->";
		returnString += packagePhases(schema, sim.getTransit_id(), xstream);
		returnString += "<------------------------------------------------------->";
		returnString += packageInjects(schema, sim.getTransit_id(), xstream);
		returnString += "<------------------------------------------------------->";
		returnString += packageBaseSimSectionInformation(schema, sim.getTransit_id(), xstream);
		returnString += "<------------------------------------------------------->";
		returnString += packageSimSectionInformation(schema, sim.getTransit_id(), xstream);
		returnString += "<------------------------------------------------------->";

		return returnString;

	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packageSimSectionInformation(String schema, long sim_id, XStream xstream) {

		String returnString = "";

		for (ListIterator<SimulationSection> li = SimulationSection.getBySim(schema, sim_id).listIterator(); li
				.hasNext();) {
			SimulationSection thisSection = li.next();

			thisSection.setTransit_id(thisSection.getId());
			thisSection.setId(null);

			returnString += xstream.toXML(thisSection);
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

		String returnString = "";
		// Get list of base section ids
		List setOfBaseSections = SimulationSection.getBaseIdsBySim(schema, sim_id);

		for (ListIterator<Long> li = setOfBaseSections.listIterator(); li.hasNext();) {
			Long thisBaseId = li.next();

			BaseSimSection bss = BaseSimSection.getMe(schema, thisBaseId.toString());

			if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.BaseSimSection")) {
				bss.setTransit_id(bss.getId());
				bss.setId(null);
				returnString += xstream.toXML(bss);
			} else if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.CustomizeableSection")) {

				bss = null;
				CustomizeableSection cbss = CustomizeableSection.getMe(schema, thisBaseId.toString());
				cbss.setTransit_id(cbss.getId());
				cbss.setId(null);
				returnString += xstream.toXML(cbss);
			} else {
				System.out.println("Warning in Object Packager. Unknown object.");
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

		String returnString = "";

		List<InjectGroup> allInjectGroups = InjectGroup.getAllForSim(schema, sim_id);
		for (ListIterator<InjectGroup> li = allInjectGroups.listIterator(); li.hasNext();) {
			InjectGroup thisInjectGroup = li.next();

			thisInjectGroup.setTransit_id(thisInjectGroup.getId());
			thisInjectGroup.setId(null);

			returnString += xstream.toXML(thisInjectGroup);

			List<Inject> allInjects = Inject.getAllForSimAndGroup(schema, sim_id, thisInjectGroup.getTransit_id());

			for (ListIterator<Inject> li_i = allInjects.listIterator(); li_i.hasNext();) {
				Inject thisInject = li_i.next();

				thisInject.setTransit_id(thisInject.getId());
				thisInject.setId(null);

				returnString += xstream.toXML(thisInject);

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
		String returnString = "";
		List<Actor> allActors = SimActorAssignment.getActorsForSim(schema, sim_id);
		for (ListIterator<Actor> li = allActors.listIterator(); li.hasNext();) {
			Actor thisActor = (Actor) li.next();

			thisActor.setTransit_id(thisActor.getId());
			thisActor.setId(null);

			returnString += xstream.toXML(thisActor);
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

		String returnString = "";
		List<SimulationPhase> allPhases = SimPhaseAssignment.getPhasesForSim(schema, sim_id);
		for (ListIterator<SimulationPhase> li = allPhases.listIterator(); li.hasNext();) {
			SimulationPhase thisPhase = (SimulationPhase) li.next();

			thisPhase.setTransit_id(thisPhase.getId());
			thisPhase.setId(null);

			returnString += xstream.toXML(thisPhase);
		}
		return returnString;
	}
	
	/**
	 * 
	 * @param fileloc
	 * @param schema
	 * @return
	 */
	public static Simulation unpackSimDetails(String fileloc, String schema){
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("sim", Simulation.class);

		Hashtable actorIdMappings = new Hashtable();
		// We use the actor id of 0 in the sections table to indicate that it is a 'universal' section
		// TODO we might want to revisit that practice.
		actorIdMappings.put(new Long(0), new Long(0));
		
		Hashtable phaseIdMappings = new Hashtable();
		Hashtable bssIdMappings = new Hashtable();

		String fileLocation = FileIO.packaged_sim_dir + File.separator + fileloc;

		System.out.println("looking for file to unpack at " + fileLocation);

		String fullString = FileIO.getFileContents(new File(fileLocation));

		String simString = getObjectFromFile(fullString, "<org.usip.osp.baseobjects.Simulation>",
				"</org.usip.osp.baseobjects.Simulation>");

		Simulation simRead = (Simulation) xstream.fromXML(simString);
		
		return simRead;
	}

	public static String unpackInformationString = "";

	/**
	 * 
	 * @param fileloc
	 * @param schema
	 */
	public static void unpackSim(String fileloc, String schema, String sim_name, String sim_version) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("sim", Simulation.class);

		Hashtable actorIdMappings = new Hashtable();
		// We use the actor id of 0 in the sections table to indicate that it is a 'universal' section
		// TODO we might want to revisit that practice.
		actorIdMappings.put(new Long(0), new Long(0));
		
		Hashtable phaseIdMappings = new Hashtable();
		Hashtable bssIdMappings = new Hashtable();

		String fileLocation = FileIO.packaged_sim_dir + File.separator + fileloc;

		System.out.println("looking for file to unpack at " + fileLocation);

		String fullString = FileIO.getFileContents(new File(fileLocation));

		String simString = getObjectFromFile(fullString, "<org.usip.osp.baseobjects.Simulation>",
				"</org.usip.osp.baseobjects.Simulation>");

		Simulation simRead = (Simulation) xstream.fromXML(simString);
		
		simRead.setName(sim_name);
		simRead.setVersion(sim_version);

		simRead.saveMe(schema);

		unpackInformationString += "<b>Unpacking Actors</b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackageActors(schema, fullString, simRead.getId(), xstream, actorIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Actors Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking Phases</b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackagePhases(schema, fullString, simRead.getId(), xstream, phaseIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Phases Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking Injects</b><br />";
		unpackInformationString += "<blockquote>";
		unpackageInjects(schema, fullString, simRead.getId(), xstream);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Injects Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking Base Simulation Sections</b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackageBaseSimSections(schema, fullString, simRead.getId(), xstream, bssIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Base Sim Sections Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking Customizeable Sim Sections</b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackageCustomizeableSimSections(schema, fullString,simRead.getId(), xstream, bssIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Customizeable Sections Unpacked</b><br />";
		unpackInformationString +="--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking Customized Sim Sections</b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackageCustomizedSimSections(schema, fullString, simRead.getId(), xstream, bssIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Customizeable Sections Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking </b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackageSimSections(schema, fullString, simRead.getId(), xstream, actorIdMappings, 
				phaseIdMappings, bssIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Simulation Sections Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";

		// ? documents, variables, conversations, etc.

	}

	/**
	 * Pulls the base sim sections out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static String unpackageSimSections(String schema, String fullString, Long sim_id,
			XStream xstream, Hashtable actorIdMappings, Hashtable phaseIdMappings, Hashtable bssIdMappings) {

		String returnString = "";
		
		List bsss = getSetOfObjectFromFile(fullString, "<org.usip.osp.baseobjects.SimulationSection>",
				"</org.usip.osp.baseobjects.SimulationSection>");
		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			SimulationSection this_ss = (SimulationSection) xstream.fromXML(act_string);
			
			this_ss.setSim_id(sim_id);
			this_ss.setActor_id((Long) actorIdMappings.get(this_ss.getActor_id()));
			this_ss.setPhase_id((Long) phaseIdMappings.get(this_ss.getPhase_id()));
			this_ss.setBase_section_id((Long) bssIdMappings.get(this_ss.getBase_section_id()));
				
			this_ss.save(schema);
			
			returnString += "Found " + this_ss.getTab_heading() + " and it had id " 
			+ this_ss.getId() + "<br />";

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
		
		List bsss = getSetOfObjectFromFile(fullString, "<org.usip.osp.baseobjects.CustomizeableSection>",
				"</org.usip.osp.baseobjects.CustomizeableSection>");
		
		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();
			
			CustomizeableSection this_bss = (CustomizeableSection) xstream.fromXML(act_string);

			if (!(this_bss.isThisIsACustomizedSection())) {
				CustomizeableSection correspondingSimSection = (CustomizeableSection) CustomizeableSection.getByName(
						schema, this_bss.getCreatingOrganization(), this_bss.getUniqueName(), this_bss.getVersion());

				if (correspondingSimSection == null) {
					String warnString = "<font color=\"red\"> Warning. CustomizeableSection simulation section " + this_bss.getVersionInformation()
					+ " not found.<br /></font>";
					System.out.println(warnString);
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
	public static String unpackageCustomizedSimSections(String schema, String fullString, Long sim_id,
			XStream xstream, Hashtable bssIdMappings) {

		String returnString = "";
		
		List bsss = getSetOfObjectFromFile(fullString, "<org.usip.osp.baseobjects.CustomizeableSection>",
				"</org.usip.osp.baseobjects.CustomizeableSection>");
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
		
		List bsss = getSetOfObjectFromFile(fullString, "<org.usip.osp.baseobjects.BaseSimSection>",
				"</org.usip.osp.baseobjects.BaseSimSection>");
		
		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			BaseSimSection this_bss = (BaseSimSection) xstream.fromXML(act_string);

			BaseSimSection correspondingSimSection = BaseSimSection.getByName(schema, this_bss
					.getCreatingOrganization(), this_bss.getUniqueName(), this_bss.getVersion());

			if (correspondingSimSection == null) {
				String warnString = "<font color=\"red\"> Warning. Base simulation section " + this_bss.getVersionInformation()
				+ " not found.<br /></font>";
				System.out.println(warnString);
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

		List actors = getSetOfObjectFromFile(fullString, "<org.usip.osp.baseobjects.Actor>",
				"</org.usip.osp.baseobjects.Actor>");
		for (ListIterator<String> li_i = actors.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			Actor this_act = (Actor) xstream.fromXML(act_string);

			String originalName = this_act.getName();
			
			this_act.setName(getUniqueUsersName(actorNames, this_act.getName()));

			this_act.saveMe(schema);
			returnString += "Actor " + originalName + " addes as " + this_act.getName();

			actorIdMappings.put(this_act.getTransit_id(), this_act.getId());

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
		List phases = getSetOfObjectFromFile(fullString, "<org.usip.osp.baseobjects.SimulationPhase>",
				"</org.usip.osp.baseobjects.SimulationPhase>");
		for (ListIterator<String> li_i = phases.listIterator(); li_i.hasNext();) {
			String phase_string = li_i.next();

			SimulationPhase this_phase = (SimulationPhase) xstream.fromXML(phase_string);

			this_phase.saveMe(schema);
			
			returnString += "Phase " + this_phase.getName() + " added to simulation";

			phaseIdMappings.put(this_phase.getTransit_id(), this_phase.getId());

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
	public static void unpackageInjects(String schema, String fullString, Long sim_id, XStream xstream) {

		Hashtable injectGroupIds = new Hashtable();

		List injectGroups = getSetOfObjectFromFile(fullString, "<org.usip.osp.communications.InjectGroup>",
				"</org.usip.osp.communications.InjectGroup>");

		for (ListIterator<String> li_i = injectGroups.listIterator(); li_i.hasNext();) {
			String injectGroup_string = li_i.next();

			InjectGroup ig = (InjectGroup) xstream.fromXML(injectGroup_string);

			ig.setSim_id(sim_id);
			ig.saveMe(schema);

			injectGroupIds.put(ig.getTransit_id(), ig.getId());

		}

		List injects = getSetOfObjectFromFile(fullString, "<org.usip.osp.communications.Inject>",
				"</org.usip.osp.communications.Inject>");

		for (ListIterator<String> li_i = injects.listIterator(); li_i.hasNext();) {
			String inject_string = li_i.next();

			Inject ig = (Inject) xstream.fromXML(inject_string);

			ig.setGroup_id((Long) injectGroupIds.get(ig.getGroup_id()));
			ig.setSim_id(sim_id);
			ig.saveMe(schema);

		}

	}

	/**
	 * 
	 * @param listOfNames
	 * @param thisName
	 * @return
	 */
	public static String getUniqueUsersName(ArrayList<String> listOfNames, String thisName) {

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
		return returnList;

	}
}
