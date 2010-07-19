<%@ page contentType="text/html; charset=utf-8" language="java" 
	import="java.sql.*,org.usip.osp.networking.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	if (  (!(afso.isLoggedin()))){
		response.sendRedirect("index.jsp");
		return;
	}

	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	pso.loadInAFSOInformation(afso);
	
	String sec_id = request.getParameter("sec_id");
	System.out.println("sec id is: " + sec_id);
	
	SimulationSectionAssignment ssa = SimulationSectionAssignment.getById(afso.schema, new Long(sec_id));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OSP Show Section Preview</title>
</head>

<frameset rows="50,90%,50" cols="*">
  <frame src="show_section_preview_top.jsp">
  <frame src="<%= ssa.generateURLforBottomFrame(new Long(0), pso.getActorId(), new Long(0)) %>">
  <frame src="show_section_preview_bot.jsp?base_sec_id=<%= ssa.getBase_sec_id() %>&ssa_sec_id=<%= sec_id %>">
</frameset>
<noframes><body>
</body>
</noframes></html>
