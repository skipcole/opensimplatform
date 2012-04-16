package com.seachangesimulations.osp.teamscores;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.baseobjects.core.Customizer;
import org.usip.osp.networking.SessionObjectBase;

public class TeamScoresCustomizer  extends Customizer{

	public static final String KEY_FOR_PAGETITLE = "teamscores_page_title"; //$NON-NLS-1$
	
	@Override
	public void handleCustomizeSection(HttpServletRequest request, 
			SessionObjectBase afso, CustomizeableSection cs) {

		Logger.getRootLogger().debug("TeamScoresCustomizer.handleCustomizeSection"); //$NON-NLS-1$
		
		String save_results = request.getParameter("save_results"); //$NON-NLS-1$

		if ((save_results != null) && (save_results.equalsIgnoreCase("true"))) { //$NON-NLS-1$
			
			Logger.getRootLogger().debug("GridDocCustomizer.handleCustomizeSection.save_results"); //$NON-NLS-1$

			cs.setBigString(request.getParameter("cs_bigstring"));
			
			cs.getContents().put(KEY_FOR_PAGETITLE, request.getParameter(KEY_FOR_PAGETITLE));
			
			cs.saveMe(afso.schema);

		}

	}

	@Override
	public void loadSimCustomizeSection(HttpServletRequest request,
			SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadSimCustomSectionForEditing(HttpServletRequest request,
			SessionObjectBase pso, CustomizeableSection cs) {
		// TODO Auto-generated method stub
		
	}

}
