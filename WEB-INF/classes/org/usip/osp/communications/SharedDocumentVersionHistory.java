package org.usip.osp.communications;

import javax.persistence.*;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents previous versions of documents.
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
@Table(name = "SHARED_DOCUMENTS_VERSION_HISTORY")
@Proxy(lazy = false)
public class SharedDocumentVersionHistory {

	/** Database id of this Shared Document. */
	@Id
	@GeneratedValue
	private Long id;

	/** Id of the base from which this copy is made. */
	@Column(name = "DOC_ID")
	private Long docId;

	private Long versionNum= new Long(1);

	/** Contents of this version of the document. */
	@Lob
	private String docText = ""; //$NON-NLS-1$

	private Date saveDate = new Date();

	public SharedDocumentVersionHistory() {

	}

	public SharedDocumentVersionHistory(String schema, SharedDocument sd,
			Session session) {
		this.docId = sd.getId();
		this.docText = sd.getBigString();
		this.saveDate = new Date();

		List<Long> versionList = null;

		if ((sd != null) && (sd.getId() != null)) {
			// select max version number
			String hqlString = "select MAX(versionNum) from SharedDocumentVersionHistory where DOC_ID = :docId";

			versionList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(hqlString).setLong("docId", sd.getId()).list();

		}

		if ((versionList == null) || (versionList.size() == 0)) {

		} else {
			Long oldMaxVersionNum = (Long) versionList.get(0);
			
			if (oldMaxVersionNum != null){
				versionNum = new Long (oldMaxVersionNum.intValue() + 1);
			}
		}
		


		// save
		session.saveOrUpdate(this);

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Long getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(Long versionNum) {
		this.versionNum = versionNum;
	}

	public String getDocText() {
		return docText;
	}

	public void setDocText(String docText) {
		this.docText = docText;
	}

	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	public static void main(String args[]) {

		System.out.println("sdvh");
		SharedDocument sd1 = new SharedDocument();
		sd1.setBigString("Hello");
		sd1.saveMe("test");
		sd1.setBigString("Hello2");
		sd1.saveMe("test");
		sd1.setBigString("Hello3");
		sd1.saveMe("test");

	}

}
