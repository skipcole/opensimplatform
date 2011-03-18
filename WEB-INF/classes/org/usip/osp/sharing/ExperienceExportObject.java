package org.usip.osp.sharing;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.USIP_OSP_Util;
import org.usip.osp.communications.InjectFiringHistory;
import org.usip.osp.communications.Tips;
import org.usip.osp.networking.FileIO;
import org.usip.osp.networking.SessionObjectBase;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/** This object encapsulates the information kept in an 'experience export.' */
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
@Entity
@Proxy(lazy = false)
public class ExperienceExportObject {

	/** Database id of this Object. */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * When records are exported, we keep a record. When they are imported, we
	 * also store a record.
	 */
	private boolean importedRecord = true;

	private String fileName = "";
	private String exportersEmail = "";
	private String exportersName = "";
	private String dbSchema = "";
	private Date exportDate = new Date();
	private String exportNotes = "";

	@Transient
	private ArrayList<RunningSimulation> setOfRunningSims = new ArrayList<RunningSimulation>();

	@Transient
	private ArrayList<Tips> setOfTips = new ArrayList<Tips>();

	@Transient
	private ArrayList<InjectFiringHistory> setOfIFH = new ArrayList<InjectFiringHistory>();

	public static void main(String args[]) {
		System.out.println("Hello World");

		XStream xstream = new XStream();

		ExperienceExportObject eeo = new ExperienceExportObject();

		System.out.println(xstream.toXML(eeo));

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isImportedRecord() {
		return importedRecord;
	}

	public void setImportedRecord(boolean importedRecord) {
		this.importedRecord = importedRecord;
	}

	public String getExportersEmail() {
		return exportersEmail;
	}

	public void setExportersEmail(String exportersEmail) {
		this.exportersEmail = exportersEmail;
	}

	public String getExportersName() {
		return exportersName;
	}

	public void setExportersName(String exportersName) {
		this.exportersName = exportersName;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	public String getExportNotes() {
		return exportNotes;
	}

	public void setExportNotes(String exportNotes) {
		this.exportNotes = exportNotes;
	}

	public ArrayList<RunningSimulation> getSetOfRunningSims() {
		return setOfRunningSims;
	}

	public void setSetOfRunningSims(
			ArrayList<RunningSimulation> setOfRunningSims) {
		this.setOfRunningSims = setOfRunningSims;
	}

	public ArrayList<Tips> getSetOfTips() {
		return setOfTips;
	}

	public void setSetOfTips(ArrayList<Tips> setOfTips) {
		this.setOfTips = setOfTips;
	}

	public ArrayList<InjectFiringHistory> getSetOfIFH() {
		return setOfIFH;
	}

	public void setSetOfIFH(ArrayList<InjectFiringHistory> setOfIFH) {
		this.setOfIFH = setOfIFH;
	}

	/**
	 * Saves this object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Handles the exporting of an experience file. A database record is kept of
	 * when the export was made.
	 * 
	 * @param request
	 */
	public static ExperienceExportObject handleExportExperience(
			HttpServletRequest request, SessionObjectBase sob) {

		String sending_page = request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("export_experience"))) {

			String simid = request.getParameter("sim_id");

			if ((simid != null) && (!(simid.equalsIgnoreCase("none")))) {
				sob.sim_id = new Long(simid);
			} else {
				sob.sim_id = null;
			}

			return null;
		}

		Hashtable fullSetOfRunningSims = new Hashtable();
		Hashtable listsOfTips = new Hashtable();
		Hashtable listsOfIFH = new Hashtable();

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("export_experience_export"))) {

			String file_name = request.getParameter("file_name");

			if ((file_name != null) && (file_name.length() > 0)) {

				String file_notes = request.getParameter("file_notes");

				ExperienceExportObject eeo = new ExperienceExportObject();
				eeo.setFileName(file_name);
				eeo.setExportNotes(file_notes);
				eeo.setImportedRecord(false);
				eeo.saveMe(sob.schema);

				// Get list of tips and running sims
				List listOfRSiDsForTips = USIP_OSP_Util.getIdsOfCheckBoxes(
						"t_", request);
				for (ListIterator<String> li = listOfRSiDsForTips
						.listIterator(); li.hasNext();) {
					String this_t = li.next();
					fullSetOfRunningSims.put(this_t, "set");
					listsOfTips.put(this_t, "set");
				}
				// ////////////////////////////////////////

				// Get list of injects and running sims
				List listOfRSiDsForInjects = USIP_OSP_Util.getIdsOfCheckBoxes(
						"i_", request);
				for (ListIterator<String> li = listOfRSiDsForInjects
						.listIterator(); li.hasNext();) {
					String this_i = li.next();
					fullSetOfRunningSims.put(this_i, "set");
					listsOfIFH.put(this_i, "set");
				}

				List listOfRSiDsForResponses = USIP_OSP_Util
						.getIdsOfCheckBoxes("rt_", request);

				List listOfRSiDsForReflections = USIP_OSP_Util
						.getIdsOfCheckBoxes("r_", request);

				// ///////////////////////////////
				ArrayList allRS = new ArrayList();
				for (Enumeration e = fullSetOfRunningSims.keys(); e
						.hasMoreElements();) {
					String key = (String) e.nextElement();

					Long rsId = new Long(key);

					RunningSimulation rs = RunningSimulation.getById(sob.schema, rsId);
					rs.setTransitId(rs.getId());
					rs.setId(null);
					
					// Add it to the transient field for export.
					allRS.add(rs);

					// Add it to the look up field to record
					ExperienceExportObjectComponents eeoc = new ExperienceExportObjectComponents(
							sob.schema, eeo.getId(), rsId,
							RunningSimulation.class.toString());
				}
				eeo.setSetOfRunningSims(allRS);
				// ///////////////////////////////
				/*
				 * ArrayList <Tips> allTips = new ArrayList<Tips>(); for
				 * (Enumeration e = listsOfIFH.keys(); e.hasMoreElements();) {
				 * String key = (String) e.nextElement();
				 * 
				 * } eeo.setSetOfTips(allTips);
				 */
				// ///////////////////////////////
				ArrayList<InjectFiringHistory> allIFH = new ArrayList<InjectFiringHistory>();
				for (Enumeration e = listsOfIFH.keys(); e.hasMoreElements();) {
					String rs_id = (String) e.nextElement();

					List injectsForThisRunSim = InjectFiringHistory
							.getAllForRunningSim(sob.schema, new Long(rs_id));

					for (ListIterator<InjectFiringHistory> li = injectsForThisRunSim
							.listIterator(); li.hasNext();) {
						InjectFiringHistory ifh = li.next();

						ifh.setTransitId(ifh.getId());
						ifh.setId(null);
						allIFH.add(ifh);

						// Add it to the look up field to record
						ExperienceExportObjectComponents eeoc = new ExperienceExportObjectComponents(
								sob.schema, eeo.getId(), ifh.getId(),
								InjectFiringHistory.class.toString());

					}
					eeo.setSetOfIFH(allIFH);

				}

				// ////////////////////

				XStream xstream = new XStream();

				String fileName = FileIO.sim_experience_dir + file_name;
				FileIO.saveFile(xstream.toXML(eeo), fileName);

				eeo.saveMe(sob.schema);
				return eeo;

			} // End of if file name != null
		} // End of if we came here to do something
		return null;
	}

	/**
	 * Loads up the details of the experience for perusal before the entire import is done.
	 * 
	 * @param request
	 * @return
	 */
	public static ExperienceExportObject importExperienceDetails(
			HttpServletRequest request) {

		String loaddetails = (String) request.getParameter("loaddetails");

		if ((loaddetails != null) && (loaddetails.equalsIgnoreCase("true"))) {
			System.out.println("made it here");

			String filename = (String) request.getParameter("filename");

			ExperienceExportObject eeo = ExperienceExportObject
					.loadEEOFromFile(filename);

			System.out.println(eeo.getExportNotes());

			return eeo;
		}

		return new ExperienceExportObject();
	}

	/**
	 * Loads in an experience object from its XML file.
	 * @param filename
	 * @return
	 */
	public static ExperienceExportObject loadEEOFromFile(String filename) {
		String fileLocation = FileIO.sim_experience_dir + File.separator
				+ filename;

		File importFile = new File(fileLocation);

		String thisEEOString = FileIO.getFileContents(new File(fileLocation));

		XStream xstream = new XStream(new DomDriver());

		ExperienceExportObject eeo = (ExperienceExportObject) xstream
				.fromXML(thisEEOString);
		eeo.setImportedRecord(true);

		return eeo;
	}

	/**
	 * Does the import of the Experience object.
	 * 
	 * @param request
	 * @return
	 */
	public static ExperienceExportObject importExperience(
			HttpServletRequest request, String schema) {

		System.out.println("made it here");
		
		String importFlag = (String) request.getParameter("import");

		if ((importFlag != null) && (importFlag.equalsIgnoreCase("true"))) {

			// load experience object from its file
			String filename = (String) request.getParameter("filename");
			ExperienceExportObject eeo = ExperienceExportObject.loadEEOFromFile(filename);
			
			Hashtable runningSimLookupTable = new Hashtable();
			
			// Loop over running sims, save them (marked as imported) and create lookup table of new ids
			for (ListIterator<RunningSimulation> li = eeo.getSetOfRunningSims().listIterator(); li.hasNext();) {
				RunningSimulation this_rs = li.next();
				this_rs.setImportedRecord(true);
				this_rs.saveMe(schema);
				runningSimLookupTable.put(this_rs.getTransitId(), this_rs.getId());
			}
			
			// Loop over running sims, save them (marked as imported) and create lookup table of new ids
			for (ListIterator<InjectFiringHistory> li = eeo.getSetOfIFH().listIterator(); li.hasNext();) {
				InjectFiringHistory this_ifh = li.next();
				
				//Set the running sim id to what it will be on this platform
				this_ifh.setRunning_sim_id((Long) runningSimLookupTable.get(this_ifh.getRunning_sim_id()));
				this_ifh.setImportedRecord(true);
				this_ifh.saveMe(schema);

			}


			
		}
		return null;
	}

}
