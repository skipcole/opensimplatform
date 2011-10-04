package org.usip.osp.baseobjects.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.networking.SessionObjectBase;

public class InventoryCustomizer  extends Customizer{

	@Override
	public void handleCustomizeSection(HttpServletRequest request,
			SessionObjectBase afso, CustomizeableSection cs) {

		Logger.getRootLogger().debug("InventoryCustomizer"); //$NON-NLS-1$
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			
			Logger.getRootLogger().debug("InventoryCustomizer.handleCustomizeSection.save_results"); //$NON-NLS-1$

			cs.setBigString(request.getParameter("cs_bigstring"));

			cs.saveMe(afso.schema);

		}
		
	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request,
			SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request,
			SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

}
