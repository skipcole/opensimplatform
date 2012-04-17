package com.seachangesimulations.osp.teamscores;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

@Entity
@Proxy(lazy=false)
public class TeamScores {

	public static final int SMALL_QUANTITY_IMAGES = 1;
	
    /** Database id of this Inject. */
	@Id
	@GeneratedValue
    private Long id;
	
	private Long simId;
	
	private Long rsId;
	
	private Long csId;
	
	/** The marking period for this score. */
	private Long tpId;
	
	private int scoreType;
	
	private int scoreValue;
	
	/** Any notes left by the instructor about this score. */
	private String scoreNotes;
	
	/** If the score is given with an image, the name of the image. */
	private String scoreImage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSimId() {
		return simId;
	}

	public void setSimId(Long simId) {
		this.simId = simId;
	}

	public Long getRsId() {
		return rsId;
	}

	public void setRsId(Long rsId) {
		this.rsId = rsId;
	}

	public Long getCsId() {
		return csId;
	}

	public void setCsId(Long csId) {
		this.csId = csId;
	}

	public Long getTpId() {
		return tpId;
	}

	public void setTpId(Long tpId) {
		this.tpId = tpId;
	}

	public int getScoreType() {
		return scoreType;
	}

	public void setScoreType(int scoreType) {
		this.scoreType = scoreType;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public String getScoreNotes() {
		return scoreNotes;
	}

	public void setScoreNotes(String scoreNotes) {
		this.scoreNotes = scoreNotes;
	}

	public String getScoreImage() {
		return scoreImage;
	}

	public void setScoreImage(String scoreImage) {
		this.scoreImage = scoreImage;
	}
	
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	public static TeamScores getById(String schema, Long ts_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		TeamScores ts = (TeamScores) MultiSchemaHibernateUtil
				.getSession(schema).get(TeamScores.class, ts_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return ts;
	}
	
	public static TeamScores getBySimSectionRunningSimTimePeriod(String schema, Long simId, 
			Long csId, Long rsId, Long tpId){
		
		
		if ((simId == null) || (csId == null) || (rsId == null) || (tpId == null)) {
			return new TeamScores();
		}

		String hQl = "from TeamScores where simId = :simId and csId = :csId and rsId = :rsId and tpId = :tpId";
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List tempList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(hQl)
				.setLong("simId", simId)
				.setLong("csId", csId)
				.setLong("rsId", rsId)
				.setLong("tpId", tpId)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		
		TeamScores returnScore = new TeamScores();
		if (tempList.size() > 0) {
			returnScore = (TeamScores) tempList.get(0);
		} else {
			returnScore.setSimId(simId);
			returnScore.setRsId(rsId);
			returnScore.setCsId(csId);
			returnScore.setTpId(tpId);
		}
		
		return returnScore;
		
	}
	
	public static TeamScores updateCreateScore(HttpServletRequest request, String schema, Long simId){
		
		String teamScoreIdString = (String) request.getParameter("ts_id");
		String cs_id = (String) request.getParameter("cs_id");
		String rs_id = (String) request.getParameter("rs_id");
		String tstp_id = (String) request.getParameter("tstp_id");
		
		String sending_page = (String) request.getParameter("sending_page");
		
		TeamScores scoreToUpdate = new TeamScores();
		
		if ((sending_page != null) && (sending_page.equalsIgnoreCase("enter_team_score"))){
			
			if ((teamScoreIdString != null) && (teamScoreIdString.length() > 0) && (!(teamScoreIdString.equalsIgnoreCase("null")))){
				scoreToUpdate = TeamScores.getById(schema, new Long(teamScoreIdString));
			} else {
				scoreToUpdate = 
					TeamScores.getBySimSectionRunningSimTimePeriod(schema, simId, new Long(cs_id), 
							new Long (rs_id), new Long (tstp_id));
			}
			
			
			
			String score_notes = (String) request.getParameter("score_notes");
			scoreToUpdate.setScoreNotes(score_notes);
			
			String points_awarded = (String) request.getParameter("points_awarded");
			
			scoreToUpdate.setScoreValue(new Long(points_awarded).intValue());
			scoreToUpdate.saveMe(schema);
		}
			
		return scoreToUpdate;
		
	}
	
}
