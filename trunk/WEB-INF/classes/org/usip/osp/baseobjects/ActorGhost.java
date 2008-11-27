package org.usip.osp.baseobjects;

/**
 * @author Ronald "Skip" Cole<br />
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
	
    private String name = "";
    
    private String defaultColorChatBubble = "#FFFFFF";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultColorChatBubble() {
		return defaultColorChatBubble;
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

        return -(a.name.compareTo(name));

    }
    
    public ActorGhost(){
    	
    }
    
    public ActorGhost(Actor act){
    	
    	this.setId(act.getId());
    	
    	this.setDefaultColorChatBubble(act.getDefaultColorChatBubble());
    	this.setName(act.getName());
    }
}
