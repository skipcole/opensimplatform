package org.usip.osp.sharing;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import org.usip.osp.baseobjects.*;
import org.usip.osp.communications.*;
import org.usip.osp.networking.*;
import org.usip.osp.persistence.*;
import org.usip.osp.specialfeatures.*;

import sun.misc.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.log4j.*;

/**
 * Packages and unpackages objects to XML using the opensource software library
 * XStream.
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
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

		// play();

		// ObjectPackager.packageUsers("test");
		System.out.println(ObjectPackager.packageUsers("test"));
	}

	public static void play() {
		BaseSimSection bss_intro = new BaseSimSection(
				"test", "", "../osp_core", "introduction.jsp", "Introduction", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"Your introductory text will be shown here."); //$NON-NLS-1$

		bss_intro.setCreatingOrganization("org.usip.osp."); //$NON-NLS-1$
		bss_intro.setUniqueName("Cast"); //$NON-NLS-1$
		bss_intro.setVersion("1"); //$NON-NLS-1$

		XStream xstream = new XStream();

		xstream.alias("bss", BaseSimSection.class); //$NON-NLS-1$

		Logger.getRootLogger().debug(xstream.toXML(bss_intro));

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

		Simulation sim = Simulation.getById(schema, sim_id);
		sim.setTransitId(sim.getId());
		sim.setId(null);

		SimulationVersion simBase = new SimulationVersion();
		simBase.setSimulationName(sim.getSimulationName());
		simBase.setSoftwareVersion(sim.getSoftwareVersion());
		simBase.setVersion(sim.getVersion());
		simBase.setTransitId(sim.getTransitId());

		String returnString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<SIM_PACKAGE_OBJECT>" + lineTerminator; //$NON-NLS-1$

		returnString += "<OSP_VERSION>" + USIP_OSP_Properties.getRelease() + "</OSP_VERSION>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ lineTerminator;

		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat();

		returnString += "<EXPORT_DATE>" + sdf.format(today) + "</EXPORT_DATE>" + lineTerminator; //$NON-NLS-1$ //$NON-NLS-2$

		returnString += xstream.toXML(simBase) + lineTerminator;
		;

		// This packages the values directly associate with the simulation such
		// as objectives and audience.
		returnString += xstream.toXML(sim) + lineTerminator;
		;

		returnString += packageActors(schema, sim.getTransitId(), xstream)
				+ lineTerminator;
		returnString += packageMetaPhases(schema, sim.getTransitId(), xstream)
				+ lineTerminator;
		returnString += packagePhases(schema, sim.getTransitId(), xstream)
				+ lineTerminator;
		returnString += packagePhaseAssignments(schema, sim.getTransitId(),
				xstream)
				+ lineTerminator;
		returnString += packageInjects(schema, sim.getTransitId(), xstream)
				+ lineTerminator;
		returnString += packageBaseSimSectionInformation(schema, sim
				.getTransitId(), xstream)
				+ lineTerminator;
		returnString += packageSimSectionAssignmentInformation(schema, sim
				.getTransitId(), xstream)
				+ lineTerminator;
		returnString += packageSimObjectInformation(schema, sim.getTransitId(),
				xstream)
				+ lineTerminator;

		returnString += packageSimMedia(schema, sim.getTransitId(), xstream);
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
	public static String packageSimSectionAssignmentInformation(String schema,
			long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$

		for (ListIterator<SimulationSectionAssignment> li = SimulationSectionAssignment
				.getBySim(schema, sim_id).listIterator(); li.hasNext();) {
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
	public static String aaaaapackageSimObjectInformation(String schema,
			long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$

		// Keeps a list of items stored so we don't store the same item twice.
		Hashtable previouslyStoredObjects = new Hashtable();

		// Get dependency (bssdoa), Get object, add bssdoa xml, add object xml
		// (if not added already)
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = BaseSimSectionDepObjectAssignment
				.getSimDependencies(schema, sim_id).listIterator(); li
				.hasNext();) {

			BaseSimSectionDepObjectAssignment bssdoa = li.next();

			bssdoa.setTransit_id(bssdoa.getId());
			bssdoa.setId(null);

			returnString += xstream.toXML(bssdoa) + lineTerminator;

			// We need to make sure that an object is not saved multiple times
			// to the xml
			SimSectionDependentObject depObj = BaseSimSectionDepObjectAssignment
					.pullOutObject(schema, bssdoa);
			Logger.getRootLogger().debug(depObj.getClass());
			Logger.getRootLogger().debug(depObj.getId());

			String hash_key_string = depObj.getClass() + "_" + depObj.getId(); //$NON-NLS-1$

			String checkString = (String) previouslyStoredObjects
					.get(hash_key_string);

			Logger.getRootLogger().debug(
					"hash_key_string was: " + hash_key_string); //$NON-NLS-1$
			Logger.getRootLogger().debug("check string was: " + checkString); //$NON-NLS-1$

			if (checkString == null) {

				previouslyStoredObjects.put(hash_key_string, "set"); //$NON-NLS-1$
				Logger.getRootLogger().debug(
						"put hash_key_string: " + hash_key_string); //$NON-NLS-1$

				depObj.setTransit_id(depObj.getId());
				depObj.setId(null);

				returnString += xstream.toXML(depObj) + lineTerminator;

				// Some dependent objects (such as conversations) have sub
				// objects.
				if (depObj.getClass().equals(Conversation.class)) {
					// Get list of conversation actors

				} else if (depObj.getClass().equals(SharedDocument.class)) {
					// Get list of shared document actor notification objects.

				}
			}

		}

		return returnString;
	}

	/**
	 * List Objects (Conversations, Timelines, Etc.) Set transit id to id Set id
	 * to null Save XML
	 * 
	 * List BSSDOA Set transit id to id Set id to null Save XML
	 * 
	 * 
	 * @param thoseDone
	 */
	public static String packageSimObjectInformation(String schema,
			long sim_id, XStream xstream) {

		String returnString = "";

		// Copy Conversations
		List<Conversation> convList = Conversation.getAllBaseForSim(schema,
				sim_id);
		for (ListIterator<Conversation> coList = convList.listIterator(); coList
				.hasNext();) {
			Conversation caa = coList.next();
			caa.setTransit_id(caa.getId());
			caa.setId(null);
			returnString += xstream.toXML(caa) + lineTerminator;
		}

		// Copy Conversation Actor Assignments
		List<ConvActorAssignment> caaList = ConvActorAssignment
				.getAllBaseForSim(schema, sim_id);
		for (ListIterator<ConvActorAssignment> cList = caaList.listIterator(); cList
				.hasNext();) {
			ConvActorAssignment caa = cList.next();
			caa.setTransit_id(caa.getId());
			caa.setId(null);
			returnString += xstream.toXML(caa) + lineTerminator;
		}

		// Copy GenericVariables
		List<GenericVariable> genVarList = GenericVariable
				.getAllBaseGenericVariablesForSim(schema, sim_id);
		for (ListIterator<GenericVariable> gvList = genVarList.listIterator(); gvList
				.hasNext();) {
			GenericVariable gv = gvList.next();
			gv.setTransit_id(gv.getId());
			gv.setId(null);
			returnString += xstream.toXML(gv) + lineTerminator;
		}

		// Copy InventoryItems
		List<InventoryItem> invList = InventoryItem.getAllBaseForSim(schema,
				sim_id);
		for (ListIterator<InventoryItem> iList = invList.listIterator(); iList
				.hasNext();) {
			InventoryItem inv = iList.next();
			inv.setTransit_id(inv.getId());
			inv.setId(null);
			returnString += xstream.toXML(inv) + lineTerminator;
		}

		// Copy OneLinks
		List<OneLink> olList = OneLink.getAllBaseOneLinksForSim(schema, sim_id);
		for (ListIterator<OneLink> onelList = olList.listIterator(); onelList
				.hasNext();) {
			OneLink inv = onelList.next();
			inv.setTransit_id(inv.getId());
			inv.setId(null);
			returnString += xstream.toXML(inv) + lineTerminator;
		}

		// Copy SharedDocuments
		List sdList = SharedDocument.getAllBaseDocumentsForSim(schema, sim_id);
		for (ListIterator<SharedDocument> sdlList = sdList.listIterator(); sdlList
				.hasNext();) {
			SharedDocument sd = sdlList.next();
			sd.setTransit_id(sd.getId());
			sd.setId(null);
			returnString += xstream.toXML(sd) + lineTerminator;
		}

		// Shared Document Actor Notification Object
		List<SharedDocActorNotificAssignObj> sdanao = SharedDocActorNotificAssignObj
				.getAllBaseForSim(schema, sim_id);
		for (ListIterator<SharedDocActorNotificAssignObj> lcaa = sdanao
				.listIterator(); lcaa.hasNext();) {
			SharedDocActorNotificAssignObj sdano = lcaa.next();
			sdano.setTransit_id(sdano.getId());
			sdano.setId(null);
			returnString += xstream.toXML(sdano) + lineTerminator;
		}

		// Copy TimeLine
		List timeList = TimeLine.getAllBaseForSimulation(schema, sim_id);
		for (ListIterator<TimeLine> tList = timeList.listIterator(); tList
				.hasNext();) {
			TimeLine tl = tList.next();
			tl.setTransit_id(tl.getId());
			tl.setId(null);
			returnString += xstream.toXML(tl) + lineTerminator;
		}

		// Events
		List<Event> eventsList = Event.getAllBaseForSim(sim_id, schema);
		for (ListIterator<Event> li = eventsList.listIterator(); li.hasNext();) {
			Event thisEvent = li.next();
			thisEvent.setTransit_id(thisEvent.getId());
			thisEvent.setId(null);
			returnString += xstream.toXML(thisEvent) + lineTerminator;
		}

		// Tips
		List<Tips> allTips = Tips.getAllForBaseSim(sim_id, schema);
		for (ListIterator<Tips> lit = allTips.listIterator(); lit.hasNext();) {
			Tips thisTip = lit.next();
			thisTip.setTransit_id(thisTip.getId());
			thisTip.setId(null);
			returnString += xstream.toXML(thisTip) + lineTerminator;
		}

		// /////////////////////////////////////////////////////
		// Get dependency (bssdoa), add bssdoa xml
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = BaseSimSectionDepObjectAssignment
				.getSimDependencies(schema, sim_id).listIterator(); li
				.hasNext();) {

			BaseSimSectionDepObjectAssignment bssdoa = li.next();

			bssdoa.setTransit_id(bssdoa.getId());
			bssdoa.setId(null);

			returnString += xstream.toXML(bssdoa) + lineTerminator;
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
	public static String packageBaseSimSectionInformation(String schema,
			long sim_id, XStream xstream) {

		String returnString = ""; //$NON-NLS-1$
		// Get list of base section ids
		List setOfBaseSections = SimulationSectionAssignment.getBaseIdsBySim(
				schema, sim_id);

		for (ListIterator<Long> li = setOfBaseSections.listIterator(); li
				.hasNext();) {
			Long thisBaseId = li.next();

			if (thisBaseId != null) {
				BaseSimSection bss = BaseSimSection.getById(schema, thisBaseId
						.toString());

				if (bss.getClass().getName().equalsIgnoreCase(
						BaseSimSection.class.getName())) {
					bss.setTransitId(bss.getId());
					bss.setId(null);
					returnString += xstream.toXML(bss) + lineTerminator;
				} else if (bss.getClass().getName().equalsIgnoreCase(
						CustomizeableSection.class.getName())) {

					bss = null;
					CustomizeableSection cbss = CustomizeableSection.getById(
							schema, thisBaseId.toString());
					cbss.setTransitId(cbss.getId());
					cbss.setId(null);
					returnString += xstream.toXML(cbss) + lineTerminator;
				} else {
					Logger.getRootLogger().debug(
							"Warning in Object Packager. Unknown object."); //$NON-NLS-1$
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
	public static String packageInjects(String schema, long sim_id,
			XStream xstream) {

		String returnString = ""; //$NON-NLS-1$

		List<InjectGroup> allInjectGroups = InjectGroup.getAllForSim(schema,
				sim_id);
		for (ListIterator<InjectGroup> li = allInjectGroups.listIterator(); li
				.hasNext();) {
			InjectGroup thisInjectGroup = li.next();

			thisInjectGroup.setTransit_id(thisInjectGroup.getId());
			thisInjectGroup.setId(null);

			returnString += xstream.toXML(thisInjectGroup) + lineTerminator;

			List<Inject> allInjects = Inject.getAllForSimAndGroup(schema,
					sim_id, thisInjectGroup.getTransit_id());

			for (ListIterator<Inject> li_i = allInjects.listIterator(); li_i
					.hasNext();) {
				Inject thisInject = li_i.next();

				thisInject.setTransit_id(thisInject.getId());
				thisInject.setId(null);

				returnString += xstream.toXML(thisInject) + lineTerminator;

				// Package actors targeted for this inject.
				List targetRaw = InjectActorAssignments.getAllForInject(schema,
						thisInject.getTransit_id());
				for (ListIterator liInjId = targetRaw.listIterator(); liInjId
						.hasNext();) {
					InjectActorAssignments targ = (InjectActorAssignments) liInjId
							.next();
					returnString += xstream.toXML(targ) + lineTerminator;
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
	public static String packageActors(String schema, long sim_id,
			XStream xstream) {
		String returnString = ""; //$NON-NLS-1$
		List<Actor> allActors = SimActorAssignment.getActorsForSim(schema,
				sim_id);
		for (ListIterator<Actor> li = allActors.listIterator(); li.hasNext();) {
			Actor thisActor = li.next();

			thisActor.setTransitId(thisActor.getId());
			thisActor.setId(null);

			returnString += xstream.toXML(thisActor) + lineTerminator;
		}
		
		List <SimActorAssignment> actorAssignments = 
			SimActorAssignment.getActorsAssignmentsForSim(schema, sim_id);
		for (ListIterator<SimActorAssignment> li = actorAssignments.listIterator(); li.hasNext();) {
			SimActorAssignment thisSAA = li.next();
			
			thisSAA.setTransitId(thisSAA.getId());
			thisSAA.setId(null);
			
			returnString += xstream.toXML(thisSAA) + lineTerminator;
			
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
	public static String packageUsers(String schema) {

		String returnString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + lineTerminator; //$NON-NLS-1$

		XStream xstream = new XStream();

		List<User> allUsers = User.getAll(schema);

		for (ListIterator<User> li = allUsers.listIterator(); li.hasNext();) {
			User thisUser = li.next();
			BaseUser bu = BaseUser.getByUserId(thisUser.getId());

			if (bu != null) {
				thisUser.setTransit_id(thisUser.getId());
				thisUser.setId(null);

				// Set the last sim edited to null since this may be meaningless
				// upon re-import.
				thisUser.setLastSimEdited(null);

				bu.setTransit_id(bu.getId());
				bu.setId(null);

				returnString += xstream.toXML(bu) + lineTerminator;
				returnString += xstream.toXML(thisUser) + lineTerminator;
			} else {
				Logger.getRootLogger().warn(
						"Back Up User Null for " + thisUser.getUser_name());
			}
		}
		return returnString;
	}

	/**
	 * 
	 * @param fileloc
	 * @param schema
	 * @return
	 */
	public static Long unpackageUsers(String fileloc, String schema) {

		String fileLocation = FileIO.archives_dir + File.separator + fileloc;

		RestoreEvents re = new RestoreEvents();
		re.setRestoreDate(new Date());
		re.setSchema(schema);
		re.setFileName(fileloc);
		re.saveMe();

		XStream xstream = new XStream(new DomDriver());

		Logger.getRootLogger().debug(
				"looking for file to unpack at " + fileLocation); //$NON-NLS-1$

		String fullString = FileIO.getFileContents(new File(fileLocation));

		// Import base users, create a hashtable of their ids ...
		Hashtable baseuserIds = new Hashtable();

		List baseusers = getSetOfObjectFromFile(fullString,
				makeOpenTag(BaseUser.class), makeCloseTag(BaseUser.class));
		for (ListIterator<String> li_i = baseusers.listIterator(); li_i
				.hasNext();) {
			String bu_string = li_i.next();

			BaseUser bu = (BaseUser) xstream.fromXML(bu_string);

			// Check to see if base user email exists in database.
			BaseUser buExisting = BaseUser.getByUsername(bu.getUsername());

			RestoreResults rr = new RestoreResults();
			rr.setRestoreId(re.getId());
			rr.setObjectClass(BaseUser.class.getName());
			rr.setObjectName(bu.getUsername());

			// if exists, create warning. Grab its id number for the hashtable
			if (buExisting != null) {
				baseuserIds.put(bu.getTransit_id(), buExisting.getId());
				// Store record that base user already existed, and so was not
				// imported.
				rr.setNotes("Base Username existed. Did not import record.");
			} else {
				bu.saveMe();
				baseuserIds.put(bu.getTransit_id(), bu.getId());
				// Store record of user save.
				rr.setNotes("Imported user.");
			}

			rr.saveMe();
		}

		List users = getSetOfObjectFromFile(fullString,
				makeOpenTag(User.class), makeCloseTag(User.class));
		for (ListIterator<String> li_i = users.listIterator(); li_i.hasNext();) {
			String u_string = li_i.next();

			User user = (User) xstream.fromXML(u_string);

			User existingUser = User.getByUsername(schema, user.getUser_name());

			RestoreResults rr = new RestoreResults();
			rr.setRestoreId(re.getId());
			rr.setObjectClass(User.class.getName());
			rr.setObjectName(user.getUser_name());

			if (existingUser != null) {
				rr.setNotes("Username existed. Did not import record.");
			} else {
				user.setId((Long) baseuserIds.get(user.getTransit_id()));
				user.saveJustUser(schema);
				rr.setNotes("Imported user.");
			}

			rr.saveMe();

		}

		return re.getId();

	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packagePhases(String schema, long sim_id,
			XStream xstream) {

		String returnString = ""; //$NON-NLS-1$
		List<SimulationPhase> allPhases = SimPhaseAssignment.getPhasesForSim(
				schema, sim_id);
		for (ListIterator<SimulationPhase> li = allPhases.listIterator(); li
				.hasNext();) {
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
	public static String packageMetaPhases(String schema, long sim_id,
			XStream xstream) {

		String returnString = ""; //$NON-NLS-1$

		List<SimulationMetaPhase> allMetaPhases = SimulationMetaPhase
				.getAllForSim(schema, sim_id);
		for (ListIterator<SimulationMetaPhase> li = allMetaPhases
				.listIterator(); li.hasNext();) {
			SimulationMetaPhase thisMetaPhase = li.next();

			thisMetaPhase.setTransit_id(thisMetaPhase.getId());
			thisMetaPhase.setId(null);

			returnString += xstream.toXML(thisMetaPhase) + lineTerminator;
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
	public static String packagePhaseAssignments(String schema, long sim_id,
			XStream xstream) {

		String returnString = ""; //$NON-NLS-1$
		List<SimPhaseAssignment> allPhases = SimPhaseAssignment
				.getPhasesAssignmentsForSim(schema, sim_id);
		for (ListIterator<SimPhaseAssignment> li = allPhases.listIterator(); li
				.hasNext();) {
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
	public static SimulationVersion unpackSimBase(String fileloc, String schema) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("sim", SimulationVersion.class); //$NON-NLS-1$

		String fileLocation = FileIO.packaged_sim_dir + File.separator
				+ fileloc;

		Logger.getRootLogger().debug(
				"looking for file to unpack at " + fileLocation); //$NON-NLS-1$

		String fullString = FileIO.getPartialFileContents(
				new File(fileLocation), makeCloseTag(SimulationVersion.class),
				true);

		String simBaseString = getFirstObjectFromFile(fullString,
				makeOpenTag(SimulationVersion.class),
				makeCloseTag(SimulationVersion.class)); //$NON-NLS-1$

		SimulationVersion simBaseRead = (SimulationVersion) xstream
				.fromXML(simBaseString);

		return simBaseRead;
	}

	public static String unpackInformationString = ""; //$NON-NLS-1$

	public static String processUpgradeChanges(String xmlText,
			String upgradeFileName) {

		String fileLocation = FileIO.upgrade_files_dir + File.separator
				+ upgradeFileName;

		File upgradeFile = new File(fileLocation);

		// Loop over changes found in upgrade file and make changes.

		return xmlText;
	}

	/**
	 * 
	 * @param fileName
	 * @param schema
	 */
	public static void unpackageSim(String fileName, String schema,
			String sim_name, String sim_version, String upgradeFileName,
			AuthorFacilitatorSessionObject afso) {

		unpackInformationString = "";

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("sim", Simulation.class); //$NON-NLS-1$

		Hashtable actorIdMappings = new Hashtable();
		// We use the actor id of 0 in the sections table to indicate that it is
		// a 'universal' section
		actorIdMappings.put(new Long(0), new Long(0));

		Hashtable metaPhaseIdMappings = new Hashtable();
		Hashtable phaseIdMappings = new Hashtable();
		Hashtable bssIdMappings = new Hashtable();

		String fileLocation = FileIO.packaged_sim_dir + File.separator
				+ fileName;

		Logger.getRootLogger().debug(
				"looking for file to unpack at " + fileLocation); //$NON-NLS-1$

		RestoreEvents re = new RestoreEvents();
		re.setRestoreDate(new Date());
		re.setSchema(schema);
		re.setFileName(fileName);
		re.saveMe();

		String xmlText = FileIO.getPartialFileContents(new File(fileLocation),
				"<SIM_MEDIA_OBJECTS>", true);

		xmlText = processUpgradeChanges(xmlText, upgradeFileName);

		String xmlMedia = FileIO.getPartialFileContents(new File(fileLocation),
				"<SIM_MEDIA_OBJECTS>", false);

		xmlMedia = processUpgradeChanges(xmlMedia, upgradeFileName);

		String simString = getObjectFromFile(xmlText,
				makeOpenTag(Simulation.class), //$NON-NLS-1$
				makeCloseTag(Simulation.class)); //$NON-NLS-1$

		Simulation simRead = (Simulation) xstream.fromXML(simString);

		simRead.setSimulationName(sim_name);
		simRead.setVersion(sim_version);

		simRead.saveMe(schema);

		afso.sim_id = simRead.getId();

		RestoreResults.createAndSaveNotes(re.getId(), "Unpacking Actors");
		unpackageActors(schema, re.getId(), xmlText, simRead.getId(), xstream,
				actorIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(), "Unpacking MetaPhases"); //$NON-NLS-1$
		unpackageMetaPhases(schema, re.getId(), xmlText, simRead.getId(),
				xstream, metaPhaseIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(), "Unpacking Phases");
		unpackagePhases(schema, re.getId(), xmlText, simRead.getId(), xstream,
				metaPhaseIdMappings, phaseIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(), "Unpacking Injects");
		unpackageInjects(schema, re.getId(), xmlText, simRead.getId(), xstream,
				actorIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(),
				"Unpacking Base Simulation Sections");
		unpackageBaseSimSections(schema, re.getId(), xmlText, simRead.getId(),
				xstream, bssIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(),
				"Unpacking Customizeable Sim Sections");
		unpackageCustomizeableSimSections(schema, re.getId(), xmlText, simRead
				.getId(), xstream, bssIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(),
				"Unpacking Customized Sim Sections");
		unpackageCustomizedSimSections(schema, re.getId(), xmlText, simRead
				.getId(), xstream, bssIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(),
				"Unpacking Simulation Section Assignments");
		unpackageSimSectionAssignmentss(schema, re.getId(), xmlText, simRead
				.getId(), xstream, actorIdMappings, phaseIdMappings,
				bssIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(),
				"Unpacking Simulation Objects");
		unpackageSimObjects(schema, re.getId(), xmlText, simRead.getId(),
				xstream, bssIdMappings, actorIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(),
				"Unpacking Misc Simulation Objects");
		unpackageMiscSimObjects(schema, re.getId(), xmlText, simRead.getId(),
				xstream, bssIdMappings, actorIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(), "Unpacking Media");
		unpackageSimMedia(schema, re.getId(), xmlMedia, simRead.getId(),
				xstream, bssIdMappings, actorIdMappings);

		RestoreResults.createAndSaveNotes(re.getId(), "Import Complete");

	}

	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param xstream
	 * @return
	 */
	public static String packageSimMedia(String schema, Long sim_id,
			XStream xstream) {

		String returnString = "<SIM_MEDIA_OBJECTS>" + lineTerminator;
		List actors = Actor.getAllForSimulation(schema, sim_id);

		for (ListIterator<Actor> li = actors.listIterator(); li.hasNext();) {
			Actor thisActor = li.next();

			String actorImageFile = thisActor.getImageFilename();
			if ((actorImageFile != null)
					&& (actorImageFile.length() > 0)
					&& (!(actorImageFile
							.equalsIgnoreCase("no_image_default.jpg")))) {

				OSPSimMedia osm = new OSPSimMedia();
				osm.setMediaType(OSPSimMedia.ACTOR_IMAGE);
				osm.setMediaName(actorImageFile);
				osm
						.setMediaString(new sun.misc.BASE64Encoder()
								.encode(FileIO
										.getImageFile(OSPSimMedia.ACTOR_IMAGE,
												actorImageFile)));

				returnString += xstream.toXML(osm) + lineTerminator;
			}

			String actorThumbImageFile = thisActor.getImageThumbFilename();
			if ((actorThumbImageFile != null)
					&& (actorThumbImageFile.length() > 0)
					&& (!(actorThumbImageFile
							.equalsIgnoreCase("no_image_default.jpg")))) {

				returnString += packageMedia(OSPSimMedia.ACTOR_IMAGE,
						actorThumbImageFile, xstream);

			}
		}

		returnString += "</SIM_MEDIA_OBJECTS>";

		return returnString;

	}

	/**
	 * 
	 * @param mediaType
	 * @param mediaName
	 * @param xstream
	 * @return
	 */
	public static String packageMedia(int mediaType, String mediaName,
			XStream xstream) {

		try {
			OSPSimMedia osm = new OSPSimMedia();
			osm.setMediaType(mediaType);
			osm.setMediaName(mediaName);
			osm.setMediaString(new sun.misc.BASE64Encoder().encode(FileIO
					.getImageFile(mediaType, mediaName)));

			return (xstream.toXML(osm) + lineTerminator);
		} catch (Exception e) {
			Logger.getRootLogger().warn("Problem finding object: " + mediaName);
			return "";
		}

	}

	public static void unpackageSimMedia(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings, Hashtable actorIdMappings) {

		System.out.println("unpacking media");

		List media = getSetOfObjectFromFile(fullString,
				makeOpenTag(OSPSimMedia.class), makeCloseTag(OSPSimMedia.class));

		System.out.println("trying to get what starts with "
				+ makeOpenTag(OSPSimMedia.class));

		for (ListIterator<String> li_i = media.listIterator(); li_i.hasNext();) {
			String media_string = li_i.next();

			OSPSimMedia this_media = (OSPSimMedia) xstream
					.fromXML(media_string);

			System.out.println("unpacking " + this_media.getMediaName());

			try {
				byte fileBytes[] = new sun.misc.BASE64Decoder()
						.decodeBuffer(this_media.getMediaString());
				FileIO.saveImageFile(this_media.getMediaType(), this_media
						.getMediaName(), fileBytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
	public static void unpackageMiscSimObjects(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings, Hashtable actorIdMappings) {

		String timelineString = getObjectFromFile(fullString,
				makeOpenTag(TimeLine.class), makeCloseTag(TimeLine.class));

		if (timelineString.length() == 0) {
			RestoreResults.createAndSaveNotes(reId,
					"Timeline not found in import file.");
			return;
		}

		TimeLine timeline = (TimeLine) xstream.fromXML(timelineString);

		if (timeline == null) {
			timeline = new TimeLine();
		}

		Long timeline_orig_id = timeline.getId();

		TimeLine thisMaster = TimeLine.getMasterPlan(schema, sim_id.toString());

		timeline.setId(thisMaster.getId());
		timeline.setSimId(sim_id);
		timeline.saveMe(schema);

		List<String> event_list = getSetOfObjectFromFile(fullString,
				makeOpenTag(Event.class), makeCloseTag(Event.class));

		for (ListIterator<String> li_i = event_list.listIterator(); li_i
				.hasNext();) {
			String e_string = li_i.next();

			Event event = (Event) xstream.fromXML(e_string);

			if (event.getTimelineId().equals(timeline_orig_id)) {

				// The id this had on the system it was exported from bears no
				// relationship to the id where its being imported.
				event.setId(null);
				event.setTimelineId(timeline.getId());
				event.setSimId(sim_id);
				event.saveMe(schema);

				RestoreResults.createAndSaveObject(reId, event.getId()
						.toString(), event.getClass().toString(), event
						.getEventTitle(), "Event Added");

			}
		}

		unpackageTips(schema, reId, fullString, sim_id, xstream, bssIdMappings,
				actorIdMappings);
	}

	public static void unpackageTips(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings, Hashtable actorIdMappings) {

		List<String> tips_list = getSetOfObjectFromFile(fullString,
				makeOpenTag(Tips.class), makeCloseTag(Tips.class));

		for (ListIterator<String> li_i = tips_list.listIterator(); li_i
				.hasNext();) {
			String e_string = li_i.next();

			Tips tip = (Tips) xstream.fromXML(e_string);

			// The id this had on the system it was exported from bears no
			// relationship to the id where its being imported.
			tip.setId(null);
			tip.setSimId(sim_id);

			Long newCsId = (Long) bssIdMappings.get(tip.getCsId());
			Long newActorId = (Long) actorIdMappings.get(tip.getActorId());
			tip.setCsId(newCsId);
			tip.setActorId(newActorId);

			tip.setTipName("tip: " + newCsId + "_" + newActorId);
			tip.saveMe(schema);

			RestoreResults.createAndSaveObject(reId, tip.getId().toString(),
					tip.getClass().toString(), tip.getTipName(), "Event Added");

		}
	}

	/** A list, key-ed on object class and transit id, of all of the new ids */

	/** A list, key-ed on the previous id, of this conversation's new id. */

	/*
	 * Pulls the simulation object out of the packaged file. Below is the basic
	 * process Unpackaging
	 * 
	 * Get Objects For each of them, do the following Save Objects and get new
	 * id Create hashtable key based on object class + transit id Add to
	 * hashtable the object id, and its new id
	 * 
	 * List Sub Objects (Conversation Actor Assignments, Events, Etc.) For each
	 * of them, do the following Save Objects and get new id Create hashtable
	 * key based on object class + transit id Add to hashtable the object id,
	 * and its new id Look up linked object id from hashtable set id of linked
	 * object to id found If this object links to actors (eg.
	 * SharedDocumentActorNotificationObject) then map actor id Save Sub Object
	 * 
	 * List BSSDOA For each of them, do the following Set transit id to id Set
	 * id to null Create hashtable key based on object class and original id
	 * Look through hashtable to get id of new object Set object id to be the id
	 * found from the hashtable
	 */

	public static void unpackageSimObjects(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings, Hashtable actorIdMappings) {

		System.out.println("sims id is " + sim_id);

		Hashtable objectMappings = new Hashtable();
		Hashtable conversationMappings = new Hashtable();
		Hashtable sharedDocumentMappings = new Hashtable();
		Hashtable timelineMappings = new Hashtable();
		Hashtable eventMappings = new Hashtable();

		ArrayList<String> classNames = new ArrayList();

		// !!! Note, need to add the classes in the right order, so the lookup
		// of information
		// such as SharedDocument.getId() from the hashtable, can work
		// correctly.
		classNames
				.add(Conversation.class.toString().replaceFirst("class ", ""));
		classNames.add(ConvActorAssignment.class.toString().replaceFirst(
				"class ", ""));
		classNames.add(GenericVariable.class.toString().replaceFirst("class ",
				""));
		classNames.add(InventoryItem.class.toString()
				.replaceFirst("class ", ""));
		classNames.add(OneLink.class.toString().replaceFirst("class ", ""));
		classNames.add(SharedDocument.class.toString().replaceFirst("class ",
				""));
		classNames.add(SharedDocActorNotificAssignObj.class.toString()
				.replaceFirst("class ", ""));
		classNames.add(TimeLine.class.toString().replaceFirst("class ", ""));
		classNames.add(Event.class.toString().replaceFirst("class ", ""));
		classNames.add(Tips.class.toString().replaceFirst("class ", ""));
		classNames.add(TimelineEventAssignment.class.toString().replaceFirst(
				"class ", ""));

		for (ListIterator<String> li_i = classNames.listIterator(); li_i
				.hasNext();) {
			String key = li_i.next();

			RestoreResults.createAndSaveNotes(reId,
					"Looking for objects of class: " + key);

			doClass(key, schema, reId, xstream, sim_id, fullString,
					actorIdMappings, objectMappings, conversationMappings,
					sharedDocumentMappings, timelineMappings, eventMappings);
		}

		// ///////////////////////////////////////////////

		// ////////////////////////////////////////////////
		List<String> bssdoa_list = getSetOfObjectFromFile(fullString,
				makeOpenTag(BaseSimSectionDepObjectAssignment.class),
				makeCloseTag(BaseSimSectionDepObjectAssignment.class));
		for (ListIterator<String> li_i = bssdoa_list.listIterator(); li_i
				.hasNext();) {
			String sd_string = li_i.next();

			BaseSimSectionDepObjectAssignment this_bssdoa = (BaseSimSectionDepObjectAssignment) xstream
					.fromXML(sd_string);

			this_bssdoa.setSim_id(sim_id);

			// get the id of the object from the hashtable.
			String fetchKey = this_bssdoa.getClassName() + "_"
					+ this_bssdoa.getObjectId();
			Long newObjectId = (Long) objectMappings.get(fetchKey);
			this_bssdoa.setObjectId(newObjectId);

			// Map the base section numbers to what they are on this system.
			try {
				Long thisMappedId = (Long) bssIdMappings.get(this_bssdoa
						.getBss_id());
				this_bssdoa.setBss_id(thisMappedId);
			} catch (Exception e) {
				e.printStackTrace();
				RestoreResults.createAndSaveWarning(reId,
						RestoreResults.RESTORE_ERROR, "Problem unpacking: "
								+ sd_string);
			}

			this_bssdoa.saveMe(schema);

			RestoreResults.createAndSaveObject(reId, this_bssdoa.getId()
					.toString(), this_bssdoa.getClass().toString(), this_bssdoa
					.getUniqueTagName(),
					"Found bssdoa. Sim id / bss id / obj id: "
							+ this_bssdoa.getSim_id() + " / "
							+ this_bssdoa.getBss_id() + " / "
							+ this_bssdoa.getObjectId());
		}
	}

	/**
	 * 
	 * @param key
	 * @param schema
	 * @param reId
	 * @param xstream
	 * @param sim_id
	 * @param fullString
	 * @param actorIdMappings
	 * @return
	 */
	public static String doClass(String key, String schema, Long reId,
			XStream xstream, Long sim_id, String fullString,
			Hashtable actorIdMappings, Hashtable objectMappings,
			Hashtable conversationMappings, Hashtable sharedDocumentMappings,
			Hashtable timelineMappings, Hashtable eventMappings) {

		String returnString = "";

		String startXMLTag = "<" + key + ">";
		String endXMLTag = "</" + key + ">";

		List this_list = getSetOfObjectFromFile(fullString, startXMLTag,
				endXMLTag);

		for (ListIterator<String> li_i = this_list.listIterator(); li_i
				.hasNext();) {
			String sd_string = li_i.next();

			SimSectionDependentObject ssdo = null;
			boolean doRest = true;

			if (key.equalsIgnoreCase(Conversation.class.toString()
					.replaceFirst("class ", ""))) {
				ssdo = (Conversation) xstream.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(ConvActorAssignment.class
					.toString().replaceFirst("class ", ""))) {
				ssdo = (ConvActorAssignment) xstream.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(GenericVariable.class.toString()
					.replaceFirst("class ", ""))) {
				ssdo = (GenericVariable) xstream.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(InventoryItem.class.toString()
					.replaceFirst("class ", ""))) {
				ssdo = (InventoryItem) xstream.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(OneLink.class.toString()
					.replaceFirst("class ", ""))) {
				ssdo = (OneLink) xstream.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(SharedDocument.class.toString()
					.replaceFirst("class ", ""))) {
				ssdo = (SharedDocument) xstream.fromXML(sd_string);
			} else if (key
					.equalsIgnoreCase(SharedDocActorNotificAssignObj.class
							.toString().replaceFirst("class ", ""))) {
				ssdo = (SharedDocActorNotificAssignObj) xstream
						.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(TimeLine.class.toString()
					.replaceFirst("class ", ""))) {
				ssdo = (TimeLine) xstream.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(Event.class.toString()
					.replaceFirst("class ", ""))) {
				ssdo = (Event) xstream.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(Tips.class.toString().replaceFirst(
					"class ", ""))) {
				ssdo = (Tips) xstream.fromXML(sd_string);
			} else if (key.equalsIgnoreCase(TimelineEventAssignment.class
					.toString().replaceFirst("class ", ""))) {
				ssdo = (TimelineEventAssignment) xstream.fromXML(sd_string);
			} else {
				Logger
						.getRootLogger()
						.warn(
								"ObjectPackager.doClass: Trying to unpack unknown class.");
			}

			if (ssdo != null) {
				ssdo.setSimId(sim_id);

				if (ssdo.getClass().equals(ConvActorAssignment.class)) {
					ConvActorAssignment caa = (ConvActorAssignment) ssdo;
					Long newActorId = (Long) actorIdMappings.get(caa
							.getActor_id());
					caa.setActor_id(newActorId);

					Long conv_id = null;

					// 11/9/10 This armor plating is probably no longer necessary. SC
					try {
						conv_id = (Long) conversationMappings.get(caa
								.getConv_id());
					} catch (Exception e) {
						e.printStackTrace();
						doRest = false;
					}

					if (conv_id != null) {
						caa.setConv_id(conv_id);
					} else {
						System.out.println("conv_id null for caa with id of "
								+ caa.getConv_id());
						doRest = false;
					}
				} else if (ssdo.getClass().equals(
						SharedDocActorNotificAssignObj.class)) {
					SharedDocActorNotificAssignObj sdano = (SharedDocActorNotificAssignObj) ssdo;
					Long newActorId = (Long) actorIdMappings.get(sdano
							.getActor_id());
					sdano.setActor_id(newActorId);
					sdano.setSd_id((Long) sharedDocumentMappings.get(sdano
							.getSd_id()));
				} else if (ssdo.getClass()
						.equals(TimelineEventAssignment.class)) {
					TimelineEventAssignment sdano = (TimelineEventAssignment) ssdo;
					Long newActorId = (Long) actorIdMappings.get(sdano
							.getCreatingActorId());
					Long newTimeLineId = (Long) timelineMappings.get(sdano
							.getTimeLineId());
					Long newEventId = (Long) eventMappings.get(sdano
							.getEventId());

					sdano.setCreatingActorId(newActorId);
					sdano.setTimeLineId(newTimeLineId);
					sdano.setEventId(newEventId);
				}

				if (doRest) {

					System.out.println("saving ssdo: " + ssdo);
					ssdo.saveMe(schema);

					try {
						// Save information you might need later.
						if (ssdo.getClass().equals(Conversation.class)) {
							Conversation conv = (Conversation) ssdo;
							conversationMappings.put(conv.getTransit_id(), conv
									.getId());
						} else if (ssdo.getClass().equals(SharedDocument.class)) {
							SharedDocument sd = (SharedDocument) ssdo;
							sharedDocumentMappings.put(sd.getTransit_id(), sd
									.getId());
						} else if (ssdo.getClass().equals(TimeLine.class)) {
							TimeLine sd = (TimeLine) ssdo;
							timelineMappings
									.put(sd.getTransit_id(), sd.getId());
						} else if (ssdo.getClass().equals(Event.class)) {
							Event sd = (Event) ssdo;
							eventMappings.put(sd.getTransit_id(), sd.getId());
						}
					} catch (Exception eee) {
						eee.printStackTrace();
					}

					objectMappings.put(key + "_" + ssdo.getTransit_id(), ssdo
							.getId());

					RestoreResults.createAndSaveObject(reId, ssdo.getId()
							.toString(), ssdo.getClass().toString(), "",
							"Found Dependent Object of class " + key
									+ " and it had a transit id of "
									+ ssdo.getTransit_id()
									+ " which was mapped to an id of "
									+ ssdo.getId());
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
	public static void unpackageSimSectionAssignmentss(String schema,
			Long reId, String fullString, Long sim_id, XStream xstream,
			Hashtable actorIdMappings, Hashtable phaseIdMappings,
			Hashtable bssIdMappings) {

		List bsss = getSetOfObjectFromFile(fullString,
				makeOpenTag(SimulationSectionAssignment.class),
				makeCloseTag(SimulationSectionAssignment.class));
		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			SimulationSectionAssignment this_ss = (SimulationSectionAssignment) xstream
					.fromXML(act_string);

			if (this_ss.getActor_id() != null) {
				this_ss.setSim_id(sim_id);
				this_ss.setActor_id((Long) actorIdMappings.get(this_ss
						.getActor_id()));
				this_ss.setPhase_id((Long) phaseIdMappings.get(this_ss
						.getPhase_id()));

				if (this_ss.getBase_sec_id() != null) {
					this_ss.setBase_sec_id((Long) bssIdMappings.get(this_ss
							.getBase_sec_id()));
				}

				this_ss.save(schema);

				if (this_ss.isSimSubSection()) {
					Long x = this_ss.getDisplaySectionId();
					if (x == null) {
						System.out
								.println("The display section of a sub section should not be null.");
					} else {
						Long y = (Long) bssIdMappings.get(x);
						if (y == null) {
							System.out
									.println("got null back from the hash table.");
						} else {
							this_ss.setDisplaySectionId(y);
							this_ss.save(schema);
						}
					}

				}

				RestoreResults.createAndSaveObject(reId, this_ss.getId()
						.toString(), this_ss.getClass().toString(), this_ss
						.getTab_heading(), "");

			}
		}

	}

	/**
	 * Pulls the base sim sections out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static void unpackageCustomizeableSimSections(String schema,
			Long reId, String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings) {

		List bsss = getSetOfObjectFromFile(fullString,
				makeOpenTag(CustomizeableSection.class),
				makeCloseTag(CustomizeableSection.class));

		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			CustomizeableSection this_bss = (CustomizeableSection) xstream
					.fromXML(act_string);

			if (!(this_bss.isThisIsACustomizedSection())) {
				CustomizeableSection correspondingSimSection = (CustomizeableSection) CustomizeableSection
						.getByName(schema, this_bss.getCreatingOrganization(),
								this_bss.getUniqueName(), this_bss.getVersion());

				if (correspondingSimSection == null) {
					String warnString = "<font color=\"red\"> Warning. CustomizeableSection simulation section "
							+ this_bss.getVersionInformation()
							+ " not found.<br /></font>";
					RestoreResults.createAndSaveWarning(reId,
							RestoreResults.RESTORE_WARN, warnString);
				} else {
					bssIdMappings.put(this_bss.getTransitId(),
							correspondingSimSection.getId());
					RestoreResults.createAndSaveNotes(reId, "Found "
							+ this_bss.getUniqueName() + " and it had id "
							+ correspondingSimSection.getId());
				}
			}
		}
	}

	/**
	 * Pulls the base sim sections out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static void unpackageCustomizedSimSections(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings) {

		List bsss = getSetOfObjectFromFile(fullString,
				makeOpenTag(CustomizeableSection.class),
				makeCloseTag(CustomizeableSection.class));
		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			CustomizeableSection this_bss = (CustomizeableSection) xstream
					.fromXML(act_string);

			if (this_bss.isThisIsACustomizedSection()) {

				this_bss.setSimId(sim_id);
				this_bss.saveMe(schema);

				RestoreResults.createAndSaveObject(reId, this_bss.getId()
						.toString(), this_bss.getClass().toString(), this_bss
						.getUniqueName(), "Found " + this_bss.getUniqueName()
						+ " and gave it id " + this_bss.getId());

				bssIdMappings.put(this_bss.getTransitId(), this_bss.getId());

			}
		}
	}

	/**
	 * Pulls the base sim sections out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static void unpackageBaseSimSections(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable bssIdMappings) {

		List bsss = getSetOfObjectFromFile(fullString,
				makeOpenTag(BaseSimSection.class),
				makeCloseTag(BaseSimSection.class));

		for (ListIterator<String> li_i = bsss.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			BaseSimSection this_bss = (BaseSimSection) xstream
					.fromXML(act_string);

			BaseSimSection correspondingSimSection = BaseSimSection.getByName(
					schema, this_bss.getCreatingOrganization(), this_bss
							.getUniqueName(), this_bss.getVersion());

			if (correspondingSimSection == null) {

				// Check to see if this is an author generated simulation
				// section (essentially a URL)
				// if it is, there will not be a corresponding simulation
				// section
				if (this_bss.isAuthorGeneratedSimulationSection()) {
					this_bss.saveMe(schema);
					bssIdMappings
							.put(this_bss.getTransitId(), this_bss.getId());

					RestoreResults.createAndSaveObject(reId, this_bss.getId()
							.toString(), this_bss.getClass().toString(),
							this_bss.getRec_tab_heading(),
							"Found author generated section "
									+ this_bss.getRec_tab_heading()
									+ " and it was given id "
									+ this_bss.getId());

				} else {
					RestoreResults.createAndSaveWarning(reId,
							RestoreResults.RESTORE_WARN,
							"Warning. Base simulation section "
									+ this_bss.getVersionInformation()
									+ " ( id : " + this_bss.getTransitId()
									+ ") not found.");
				}
			} else {
				RestoreResults.createAndSaveObject(reId,
						correspondingSimSection.getId().toString(), this_bss
								.getClass().toString(), this_bss
								.getUniqueName(), "Found "
								+ this_bss.getRec_tab_heading()
								+ " and it had id "
								+ correspondingSimSection.getId());

				bssIdMappings.put(this_bss.getTransitId(),
						correspondingSimSection.getId());
			}

		}
	}

	/**
	 * Pulls the actors out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static void unpackageActors(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable actorIdMappings) {

		List actors = getSetOfObjectFromFile(fullString,
				makeOpenTag(Actor.class), makeCloseTag(Actor.class));
		for (ListIterator<String> li_i = actors.listIterator(); li_i.hasNext();) {
			String act_string = li_i.next();

			Actor this_act = (Actor) xstream.fromXML(act_string);

			// Set the id of the simulation associated with this actor to be the
			// new simulation id.
			this_act.setSim_id(sim_id);
			this_act.saveMe(schema);
			
			RestoreResults.createAndSaveObject(reId, this_act.getId()
					.toString(), this_act.getClass().toString(), this_act
					.getActorName(), "unpacked Actor with originalName: "
					+ this_act.getActorName());

			actorIdMappings.put(this_act.getTransitId(), this_act.getId());

		}

		// Get all of the SimActorAssignments
		List saa = getSetOfObjectFromFile(fullString,
				makeOpenTag(SimActorAssignment.class), makeCloseTag(SimActorAssignment.class));
		for (ListIterator<String> li_i = saa.listIterator(); li_i.hasNext();) {
			String saa_string = li_i.next();

			SimActorAssignment this_saa = (SimActorAssignment) xstream.fromXML(saa_string);
			
			this_saa.setSim_id(sim_id);
			this_saa.setActorId( (Long) actorIdMappings.get(this_saa.getActorId())); 
			
			if (saa_string.contains("<active>true</active>")){
				this_saa.setActive(true);
			}
			
			this_saa.saveMe(schema);
			
		}

	}

	/**
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 * @param metaPhaseIdMappings
	 * @return
	 */
	public static void unpackageMetaPhases(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable metaPhaseIdMappings) {

		List phases = getSetOfObjectFromFile(fullString,
				makeOpenTag(SimulationMetaPhase.class),
				makeCloseTag(SimulationMetaPhase.class));
		for (ListIterator<String> li_i = phases.listIterator(); li_i.hasNext();) {
			String phase_string = li_i.next();

			SimulationMetaPhase this_meta_phase = (SimulationMetaPhase) xstream
					.fromXML(phase_string);

			this_meta_phase.setSim_id(sim_id);
			this_meta_phase.saveMe(schema);
			RestoreResults.createAndSaveObject(reId, this_meta_phase.getId()
					.toString(), this_meta_phase.getClass().toString(),
					this_meta_phase.getMetaPhaseName(), "unpacked MetaPhase");

			metaPhaseIdMappings.put(this_meta_phase.getTransit_id(),
					this_meta_phase.getId());

		}

	}

	/**
	 * Pulls the actors out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static void unpackagePhases(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable metaPhaseIdMappings, Hashtable phaseIdMappings) {

		List phases = getSetOfObjectFromFile(fullString,
				makeOpenTag(SimulationPhase.class),
				makeCloseTag(SimulationPhase.class));
		for (ListIterator<String> li_i = phases.listIterator(); li_i.hasNext();) {
			String phase_string = li_i.next();

			SimulationPhase this_phase = (SimulationPhase) xstream
					.fromXML(phase_string);

			if (this_phase.getMetaPhaseId() != null) {

				Long newMetaPhaseId = (Long) metaPhaseIdMappings.get(this_phase
						.getMetaPhaseId());
				if (newMetaPhaseId != null) {
					this_phase.setMetaPhaseId(newMetaPhaseId);
				} else {
					Logger.getRootLogger().debug(
							"No MetaPhase mapped to id "
									+ this_phase.getMetaPhaseId());
				}
			}

			this_phase.saveMe(schema);

			RestoreResults.createAndSaveObject(reId, this_phase.getId()
					.toString(), this_phase.getClass().toString(), this_phase
					.getPhaseName(), "Phase added to simulation");

			phaseIdMappings.put(this_phase.getTransit_id(), this_phase.getId());

			@SuppressWarnings("unused")
			SimPhaseAssignment spa = new SimPhaseAssignment(schema, sim_id,
					this_phase.getId());
		}
	}

	/**
	 * Pulls the actors out of the packaged file.
	 * 
	 * @param schema
	 * @param fullString
	 * @param sim_id
	 * @param xstream
	 */
	public static void unpackageInjects(String schema, Long reId,
			String fullString, Long sim_id, XStream xstream,
			Hashtable actorIdMappings) {
		Hashtable injectGroupIds = new Hashtable();

		List injectGroups = getSetOfObjectFromFile(fullString,
				makeOpenTag(InjectGroup.class), makeCloseTag(InjectGroup.class));

		for (ListIterator<String> li_i = injectGroups.listIterator(); li_i
				.hasNext();) {
			String injectGroup_string = li_i.next();

			InjectGroup ig = (InjectGroup) xstream.fromXML(injectGroup_string);

			ig.setSim_id(sim_id);
			ig.saveMe(schema);
			RestoreResults.createAndSaveObject(reId, ig.getId().toString(), ig
					.getClass().toString(), ig.getName(),
					"Inject Group added to simulation");

			injectGroupIds.put(ig.getTransit_id(), ig.getId());

		}

		Hashtable injectMappings = new Hashtable();

		List injects = getSetOfObjectFromFile(fullString,
				makeOpenTag(Inject.class), makeCloseTag(Inject.class));

		for (ListIterator<String> li_i = injects.listIterator(); li_i.hasNext();) {
			String inject_string = li_i.next();

			Inject ig = (Inject) xstream.fromXML(inject_string);

			ig.setGroup_id((Long) injectGroupIds.get(ig.getGroup_id()));
			ig.setSim_id(sim_id);
			ig.saveMe(schema);

			// Save the mapping of old id to new to allow the proper assignment
			// of targets.
			injectMappings.put(ig.getTransit_id(), ig.getId());

			RestoreResults.createAndSaveObject(reId, ig.getId().toString(), ig
					.getClass().toString(), ig.getInject_name(),
					"Inject added to simulation");

		}

		// /
		List injectsTargs = getSetOfObjectFromFile(fullString,
				makeOpenTag(InjectActorAssignments.class),
				makeCloseTag(InjectActorAssignments.class));

		for (ListIterator<String> li_i = injectsTargs.listIterator(); li_i
				.hasNext();) {
			String inject_targ_string = li_i.next();

			InjectActorAssignments iaa = (InjectActorAssignments) xstream
					.fromXML(inject_targ_string);

			// Look up what the new actor id is in the database we are moving
			// to.
			iaa.setActor_id((Long) actorIdMappings.get(iaa.getActor_id()));
			iaa.setInject_id((Long) injectMappings.get(iaa.getInject_id()));

			iaa.saveMe(schema);

			String restoreNote = "targeted actor " + iaa.getActor_id()
					+ " for inject " + iaa.getInject_id();
			RestoreResults.createAndSaveObject(reId, iaa.getId().toString(),
					iaa.getClass().toString(), restoreNote,
					"Inject Target added to simulation");

		}
		// /
	}

	/**
	 * This gets a single object out of a file based on start and end tags.
	 * 
	 * @param fullContents
	 * @param startString
	 * @param endString
	 * @return
	 */
	public static String getObjectFromFile(String fullContents,
			String startString, String endString) {

		int findStartOfMatch = fullContents.lastIndexOf(startString);

		int endOfString = fullContents.lastIndexOf(endString);

		if ((findStartOfMatch != -1) && (endOfString != -1)) {

			int findEndOfMatch = endOfString + endString.length();

			return fullContents.substring(findStartOfMatch, findEndOfMatch);

		} else {

			return "";

		}

	}

	public static String getFirstObjectFromFile(String fullContents,
			String startString, String endString) {

		int findStartOfMatch = fullContents.indexOf(startString);

		int endOfString = fullContents.indexOf(endString);

		if ((findStartOfMatch != -1) && (endOfString != -1)) {

			int findEndOfMatch = endOfString + endString.length();

			return fullContents.substring(findStartOfMatch, findEndOfMatch);

		} else {

			return "";

		}

	}

	/**
	 * This gets multiple objects out of a file based on its start and end tags.
	 * 
	 * @param thisFile
	 * @param startString
	 * @param endString
	 * @return
	 */
	public static List getSetOfObjectFromFile(String fullContents,
			String startString, String endString) {

		ArrayList returnList = new ArrayList();

		boolean exitLoop = false;

		while (!exitLoop) {
			int findStartOfMatch = fullContents.lastIndexOf(startString);
			int findEndOfMatch = fullContents.lastIndexOf(endString)
					+ endString.length();

			if (findStartOfMatch > 0) {
				returnList.add(fullContents.substring(findStartOfMatch,
						findEndOfMatch));
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
	public static String addResultsToXML(String startTag, String endTag,
			boolean value) {

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
