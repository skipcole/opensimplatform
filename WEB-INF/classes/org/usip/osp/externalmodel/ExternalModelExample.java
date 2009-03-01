package org.usip.osp.externalmodel;

import java.util.ArrayList;
import java.util.List;

import org.usip.osp.modelinterface.ModelController;

/**
 * This abstract class is an example class of how one can add external models.
 * 
 * @author Ronald "Skip" Cole<br />
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
public class ExternalModelExample extends ModelController{

	@Override
	public List doEquation(String equationName, List equationFlags, List inputMetaData, List inputData,
			List outputMetaData, List outputData) {

		ArrayList returnList = new ArrayList();
		
		Long newLong = new Long(1);
		
		returnList.add(newLong);
		
		return null;
	}

}
