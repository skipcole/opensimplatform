<%@ page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.util.*,org.usip.oscw.networking.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*" 
errorPage="" %>
<%
	
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
	// This is what adds it to the base sim section list.
	CustomizeableSection cs = pso.handleMakeCustomizedSection(request);
	
	String tab_heading = (String) request.getParameter("tab_heading");
    String tab_pos = (String) request.getParameter("tab_pos");
	String universal = (String) request.getParameter("universal");
	
	System.out.println("pcp - universal was : " + universal);
	System.out.println("tab_heading : " + tab_heading);
	
	if (true) {
	
		pso.addSectionFromProcessCustomPage(cs.getId(), tab_pos, tab_heading, request, universal);
     
		response.sendRedirect(pso.backPage);
		return;
	}
	
%>
