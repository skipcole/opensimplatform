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
import org.usip.osp.communications.Inject;
import org.usip.osp.communications.InjectGroup;
import org.usip.osp.communications.SharedDocument;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Packages and unpackages objects to XML using the opensource software library
 * XStream.
 *
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

	public static final String lineTerminator = "\r\n";

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

		String returnString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<SIM_PACKAGE_OBJECT>" + lineTerminator;

		returnString += "<OSP_VERSION>" + USIP_OSP_Properties.getRawValue("release") + "</OSP_VERSION>"
				+ lineTerminator;

		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat();

		returnString += "<EXPORT_DATE>" + sdf.format(today) + "</EXPORT_DATE>" + lineTerminator;

		returnString += xstream.toXML(sim);

		returnString += packageActors(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packagePhases(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageInjects(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageBaseSimSectionInformation(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageSimSectionAssignmentInformation(schema, sim.getTransit_id(), xstream) + lineTerminator;
		returnString += packageSimObjectInformation(schema, sim.getTransit_id(), xstream) + lineTerminator;

		returnString += "</SIM_PACKAGE_OBJECT>";

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

		String returnString = "";

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

		String returnString = "";

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
			SimSectionDependentObject depObj = bssdoa.pullOutObject(schema, bssdoa);
			System.out.println(depObj.getClass());
			System.out.println(depObj.getId());
			
			String hash_key_string = depObj.getClass() + "_" + depObj.getId();

			String checkString = (String) previouslyStoredObjects.get(hash_key_string);
			
			System.out.println("hash_key_string was: " + hash_key_string);
			System.out.println("check string was: " + checkString);

			if (checkString == null) {
				
				previouslyStoredObjects.put(hash_key_string, "set");
				System.out.println("put hash_key_string: " + hash_key_string);

				depObj.setTransit_id(depObj.getId());
				depObj.setId(null);

				returnString += xstream.toXML(depObj) + lineTerminator;
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

		String returnString = "";
		// Get list of base section ids
		List setOfBaseSections = SimulationSectionAssignment.getBaseIdsBySim(schema, sim_id);

		for (ListIterator<Long> li = setOfBaseSections.listIterator(); li.hasNext();) {
			Long thisBaseId = li.next();

			BaseSimSection bss = BaseSimSection.getMe(schema, thisBaseId.toString());

			if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.BaseSimSection")) {
				bss.setTransit_id(bss.getId());
				bss.setId(null);
				returnString += xstream.toXML(bss) + lineTerminator;
			} else if (bss.getClass().getName().equalsIgnoreCase("org.usip.osp.baseobjects.CustomizeableSection")) {

				bss = null;
				CustomizeableSection cbss = CustomizeableSection.getMe(schema, thisBaseId.toString());
				cbss.setTransit_id(cbss.getId());
				cbss.setId(null);
				returnString += xstream.toXML(cbss) + lineTerminator;
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
		String returnString = "";
		List<Actor> allActors = SimActorAssignment.getActorsForSim(schema, sim_id);
		for (ListIterator<Actor> li = allActors.listIterator(); li.hasNext();) {
			Actor thisActor = (Actor) li.next();

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

		String returnString = "";
		List<SimulationPhase> allPhases = SimPhaseAssignment.getPhasesForSim(schema, sim_id);
		for (ListIterator<SimulationPhase> li = allPhases.listIterator(); li.hasNext();) {
			SimulationPhase thisPhase = (SimulationPhase) li.next();

			thisPhase.setTransit_id(thisPhase.getId());
			thisPhase.setId(null);

			returnString += xstream.toXML(thisPhase) + lineTerminator;
		}
		return returnString;
	}

	/**
	 * 
	 * @param fileloc
	 * @param schema
	 * @return
	 */
	public static Simulation unpackSimDetails(String fileloc, String schema) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("sim", Simulation.class);

		Hashtable actorIdMappings = new Hashtable();
		// We use the actor id of 0 in the sections table to indicate that it is
		// a 'universal' section
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
		// We use the actor id of 0 in the sections table to indicate that it is
		// a 'universal' section
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
		unpackInformationString += unpackageInjects(schema, fullString, simRead.getId(), xstream);
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
		unpackInformationString += unpackageCustomizeableSimSections(schema, fullString, simRead.getId(), xstream,
				bssIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Customizeable Sections Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking Customized Sim Sections</b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackageCustomizedSimSections(schema, fullString, simRead.getId(), xstream,
				bssIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Customizeable Sections Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking Simulation Sections</b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackageSimSectionAssignmentss(schema, fullString, simRead.getId(), xstream,
				actorIdMappings, phaseIdMappings, bssIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Simulation Sections Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";
		unpackInformationString += "<b>Unpacking Simulation Objects</b><br />";
		unpackInformationString += "<blockquote>";
		unpackInformationString += unpackageSimObjects(schema, fullString, simRead.getId(), xstream, bssIdMappings);
		unpackInformationString += "</blockquote>";
		unpackInformationString += "<b>Simulation Sections Unpacked</b><br />";
		unpackInformationString += "--------------------------------------------------------------------<br />";

		// ? documents, variables, conversations, etc.

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
			Hashtable bssIdMappings) {

		// Get object dependencies
		String returnString = "Getting Dependent Object Assignments <BR />";

		// Fill this up to import objects first, and then remap the bssdoa to
		// them and the base sim section
		Hashtable<String, String> setOfObjectClassesToGet = new Hashtable();

		List<String> bssdoa_list = getSetOfObjectFromFile(fullString,
				"<org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment>",
				"</org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment>");
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

				// Save object, map its new id to the transit id
				this_dos.saveMe(schema);
				dependentObjectMappings.put(this_dos.getTransit_id(), this_dos.getId());

				returnString += "Found Dependent Object of class " + key + " and it had a transit id of "
						+ this_dos.getTransit_id() + " which was mapped to an id of " + this_dos.getId() + "<BR />";

				System.out.println("Found Dependent Object of class " + key + " and it had a transit id of "
						+ this_dos.getTransit_id() + " which was mapped to an id of " + this_dos.getId() + "<BR />");
			}
		}

		// Now go back through the bssdoas, remap the values of bss id and dep
		// obj. id, and then save them.
		for (ListIterator<String> li_i = bssdoa_list.listIterator(); li_i.hasNext();) {
			String sd_string = li_i.next();

			BaseSimSectionDepObjectAssignment this_bssdoa = (BaseSimSectionDepObjectAssignment) xstream
					.fromXML(sd_string);

			System.out.println("this_bssdoa.getBss_id() was " + this_bssdoa.getBss_id());
			System.out.flush();

			this_bssdoa.setSim_id(sim_id);

			// This is the line that is dying
			// TODO
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
		
		// Sub section ids will change, but since this set of ids point back into the same table
		// its a bit more difficult to get them right the first time. So we keep a list of all the ones that
		// will need changed after the first pass.
		List subSectionIdsToClean = new ArrayList();
		Hashtable ssidsHash = new Hashtable();
		
		List bsss = getSetOfObjectFromFile(fullString, "<org.usip.osp.baseobjects.SimulationSectionAssignment>",
				"</org.usip.osp.baseobjects.SimulationSectionAssignment>");
		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			SimulationSectionAssignment this_ss = (SimulationSectionAssignment) xstream.fromXML(act_string);

			this_ss.setSim_id(sim_id);
			this_ss.setActor_id((Long) actorIdMappings.get(this_ss.getActor_id()));
			this_ss.setPhase_id((Long) phaseIdMappings.get(this_ss.getPhase_id()));
			this_ss.setBase_section_id((Long) bssIdMappings.get(this_ss.getBase_section_id()));
			
			this_ss.save(schema);
			
			if (this_ss.isSimSubSection()){
				subSectionIdsToClean.add(this_ss.getId());
			}
			
			ssidsHash.put(this_ss.getTransit_id(), this_ss.getId());

			returnString += "Found " + this_ss.getTab_heading() + " and it had id " + this_ss.getId() + "<br />";

		}
		
		// Loop over the saved sim section assignments and correct the sim sub section pointers to the ids in
		// the new database.
		for (ListIterator<Long> li_i = subSectionIdsToClean.listIterator(); li_i.hasNext();){
			Long li_id = li_i.next();
			
			SimulationSectionAssignment this_ssa = SimulationSectionAssignment.getMe(schema, li_id);
			
			returnString += "<font color=green>Remapped " +  this_ssa.getDisplaySectionId() + " to " + 
				(Long) ssidsHash.get(this_ssa.getDisplaySectionId()) + "</font><br />";
			
			this_ssa.setDisplaySectionId((Long) ssidsHash.get(this_ssa.getDisplaySectionId()));
			this_ssa.save(schema);
			
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
					String warnString = "<font color=\"red\"> Warning. CustomizeableSection simulation section "
							+ this_bss.getVersionInformation() + " not found.<br /></font>";
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
	public static String unpackageCustomizedSimSections(String schema, String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings) {

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
				String warnString = "<font color=\"red\"> Warning. Base simulation section "
						+ this_bss.getVersionInformation() + " ( id : " + this_bss.getTransit_id()
						+ ") not found.<br /></font>";
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
	public static String unpackageInjects(String schema, String fullString, Long sim_id, XStream xstream) {

		String returnString = "Unpackaging Inject Groups and Injects.";
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

		return returnString;
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

		// Since the above get things from the end of the file and works
		// backwards, we reverse the order
		// to make the imported objects come in in the same order in which they
		// were exported.
		Collections.reverse(returnList);

		return returnList;

	}
}
