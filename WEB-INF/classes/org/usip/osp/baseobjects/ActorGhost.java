package org.usip.osp.baseobjects;

/**
 * This class represents a non-persistent version of an Actor. We ran into problems using the hibernate
 * versions of classes directly in the web application. If using this ghost is necessary, I don't know. But it
 * helped us quickly get around our issues.
 */
/*
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
public class ActorGhost {

	private Long id;
	
    private String name = ""; //$NON-NLS-1$
    
    private String defaultColorChatBubble = "#FFFFFF"; //$NON-NLS-1$

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Deprecated
	public String getName() {
		return this.name;
	}
	
	public String getActorName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultColorChatBubble() {
		return this.defaultColorChatBubble;
	}

	public void setDefaultColorChatBubble(String defaultColorChatBubble) {
		this.defaultColorChatBubble = defaultColorChatBubble;
	}
    
    /**
     * Compares two actors to order them by name.
     * 
     * @param o
     * @return
     */
    public int compareTo(Object o) {

        ActorGhost a = (ActorGhost) o;

        return -(a.name.compareTo(this.name));

    }
    
    /**
     * Zero argument constructor needed by Hibernate.
     */
    public ActorGhost(){
    	
    }
    
    public ActorGhost(Actor act){
    	
    	this.setId(act.getId());
    	
    	this.setDefaultColorChatBubble(act.getDefaultColorChatBubble());
    	this.setName(act.getActorName());
    }
}
