package com.seachangesimulations.osp.teamscores;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;

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
	private Long teamScoresTimePeriod;
	
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

	public Long getTeamScoresTimePeriod() {
		return teamScoresTimePeriod;
	}

	public void setTeamScoresTimePeriod(Long teamScoresTimePeriod) {
		this.teamScoresTimePeriod = teamScoresTimePeriod;
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
	
	
	
}
