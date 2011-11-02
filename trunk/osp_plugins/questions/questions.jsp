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
	
	QuestionCustomizer.handleChanges(request, cs, pso.schema, pso.sim_id, pso.getRunningSimId(), pso.getActorId(), pso.user_id);
	
	Hashtable contents = cs.getContents();
	
%>
<html>
<head>
<title>Questions Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<body>
<h1><%=  QuestionCustomizer.getPageStringValue(cs, QuestionCustomizer.KEY_FOR_PAGETITLE) %></h1>
<p><%= cs.getBigString() %></p>
<form name="form1" method="post" action="questions.jsp">
<input type="hidden" name="cs_id" value="<%= cs.getId() %>" />
<input type="hidden" name="sending_page" value="questions" />
<table border="1" cellspacing="0">
<%

	boolean answersHaveBeenSubmitted = false;
	
	List <QuestionAndResponse> qAndRList = QuestionAndResponse.getAllForSimAndCustomSection(pso.schema, pso.sim_id, cs.getId());
	
	if (qAndRList.size() == 0) {
		QuestionAndResponse qar = new QuestionAndResponse();
		qAndRList.add(qar);
	}

	int questionIndex = 1;
	
		for (ListIterator li = qAndRList.listIterator(); li.hasNext();) {
			QuestionAndResponse this_qar = (QuestionAndResponse) li.next();
			PlayerAnswer this_pa = PlayerAnswer.getByQuestionRunningSimAndUserIds(pso.schema, this_qar.getId(), pso.getRunningSimId(), pso.user_id);
%>
<tr><td valign="top">
	<%= this_qar.getQuestionIdentifier() %>
	<input type="hidden" name="q_id_<%= this_qar.getId() %>" value="<%= this_qar.getId() %>" />
	</td><td valign="top"><strong>Question</strong></td><td valign="top"><%= this_qar.getQuestion() %></td></tr>

<% if (!(this_pa.isSubmitted())) { %>
<tr><td valign="top">&nbsp;</td><td valign="top"><strong>Your Answer</strong></td><td valign="top">
  <textarea name="answer_<%= this_qar.getId() %>" id="answer_<%= this_qar.getId() %>" cols="45" rows="5"><%= this_pa.getPlayerAnswer() %></textarea></td></tr>
<% } else { 
	answersHaveBeenSubmitted = true;
%>
<tr><td valign="top">&nbsp;</td><td valign="top"><strong>Your Answer</strong></td><td valign="top"><%= this_pa.getPlayerAnswer() %></td></tr>
<tr><td valign="top">&nbsp;</td><td valign="top"><strong>Instructor's Answer</strong></td><td valign="top"><%= this_qar.getAnswer() %></td></tr>
<% } %>

<% } // end of loop over questions %>
</table>

<% if (!(answersHaveBeenSubmitted)) { %>
  <p>
    <input type="submit" name="command_save" id="save" value="Save">
  </p>
  <p align="right">
    <input type="submit" name="command_submit" id="submit_final" value="Submit">
  </p>
<% } %>
</form>
<p>&nbsp;</p>


</body>
</html>