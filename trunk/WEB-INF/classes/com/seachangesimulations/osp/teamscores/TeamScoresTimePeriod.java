package com.seachangesimulations.osp.teamscores;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;

@Entity
@Proxy(lazy=false)
public class TeamScoresTimePeriod {

    /** Database id of this Inject. */
	@Id
	@GeneratedValue
    private Long id;
	
	private String periodDescription;
	
	private Date periodDate;
	
}
