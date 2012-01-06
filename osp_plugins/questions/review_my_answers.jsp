<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	com.seachangesimulations.osp.questions.*" 
	errorPage="/error.jsp" %>

<%
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getById(pso.schema, cs_id);
	
	Hashtable contents = cs.getContents();
	
	Simulation simulation = new Simulation();	
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
%>
<html>
<head>
<title>Questions View Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1><%=  QuestionCustomizer.getPageStringValue(cs, QuestionCustomizer.KEY_FOR_PAGETITLE) %></h1>
<p><%= cs.getBigString() %></p>

<table cellspacing="0" border="1" width="90%">
<tr><td valign="top"><strong>Q. </strong></td><td valign="top"><strong>User</strong></td><td valign="top"><strong>Answer</strong></td>
  <td valign="top"><strong>Submitted</strong></td>
</tr>
<%

	List <QuestionAndResponse> qAndRList = QuestionAndResponse.getAllForSimAndCustomSection(pso.schema, pso.sim_id, cs.getId());
	
	//loop over questions
	for (ListIterator lq = qAndRList.listIterator(); lq.hasNext();) {
		QuestionAndResponse this_qar = (QuestionAndResponse) lq.next();
%>
<tr><td valign="top"><strong><%= this_qar.getQuestionIdentifier() %></strong></td>
<td valign="top"><strong>Author's Answer</strong></td>
<td valign="top"><strong><%= this_qar.getAnswer() %></strong></td>
<td valign="top">&nbsp;</td>
</tr>
<%

	List uas = new ArrayList();
	
	if (!(pso.preview_mode)) {
		uas = UserAssignment.getUniqueSetOfUsers(pso.schema, pso.getRunningSimId());
	}
					
			// Loop over all of the user assignments
			for (ListIterator liua = uas.listIterator(); liua.hasNext();) {
					
				Long ua = (Long) liua.next();
				
					PlayerAnswer pa = 
						PlayerAnswer.getByQuestionRunningSimAndUserIds
						(pso.schema, this_qar.getId(), pso.getRunningSimId(),  ua);
%>
               <tr><td valign="top"><%= this_qar.getQuestionIdentifier() %></td>
               <td valign="top">
			   <%= USIP_OSP_Cache.getUSERName(pso.schema, request, ua) %>
               </td>
               
               <td valign="top"><%= pa.getPlayerAnswer() %></td>
                 <td valign="top"><% if (pa.isSubmitted()) { %><%= pa.getTimeSubmitted() %><% } %></td>
               </tr>
<%
			} // end of loop over user ids
	} // End of loop over questions
			
%>                    
</table>

<p>&nbsp;</p>
</body>
</html>