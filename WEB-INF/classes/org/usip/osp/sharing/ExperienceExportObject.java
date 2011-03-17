package org.usip.osp.sharing;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.communications.InjectFiringHistory;
import org.usip.osp.communications.Tips;

import com.thoughtworks.xstream.XStream;

/** This object encapsulates the information kept in an 'experience export.'
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
@Entity
@Proxy(lazy = false)
public class ExperienceExportObject {

	/** Database id of this Tip. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** When records are exported, we keep a record. When they are imported, we also store a record. */
	private boolean importedRecord = true;
	
	private String exportersEmail = "";
	private String exportersName = "";
	private String dbSchema = "";
	private Date exportDate = new Date();
	private String exportNotes = "";
	
	private ArrayList <RunningSimulation> setOfRunningSimIdObjects = new ArrayList<RunningSimulation>();
	private ArrayList <Tips> setOfTips = new ArrayList<Tips>();
	private ArrayList <InjectFiringHistory> setOfIFH = new ArrayList<InjectFiringHistory>();
	
	
	
	public static void main(String args[]){
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



	public ArrayList<RunningSimulation> getSetOfRunningSimIdObjects() {
		return setOfRunningSimIdObjects;
	}



	public void setSetOfRunningSimIdObjects(
			ArrayList<RunningSimulation> setOfRunningSimIdObjects) {
		this.setOfRunningSimIdObjects = setOfRunningSimIdObjects;
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
	
	

}
