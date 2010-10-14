package org.usip.osp.sharing;

/**
 * Creates a Respondable Object that can be responded to.
 * 
 */
public interface CreatesRespondableObjects {

	public void createRespondableObject(String schema, Long simId, Long rsId, Long phase_id, 
			Long actor_id, String userName, String userDisplayName);
	
}
