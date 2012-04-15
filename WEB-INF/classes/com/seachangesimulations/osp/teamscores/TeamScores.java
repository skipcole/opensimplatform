package com.seachangesimulations.osp.teamscores;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy=false)
public class TeamScores {

    /** Database id of this Inject. */
	@Id
	@GeneratedValue
    private Long id;
	
	private Long simId;
	
	private Long rsId;
	
	private Long csId;
	
	private Long teamScoresTimePeriod;
	
	private int scoreType;
	
	private int scoreValue;
	
	private String scoreNotes;
	
	
	
}
