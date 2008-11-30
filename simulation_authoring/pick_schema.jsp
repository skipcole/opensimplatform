<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.io.*,java.util.*,java.text.*,java.sql.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" errorPage="" %>
<%

	String attempting_select = (String) request.getParameter("attempting_select");
	
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	System.out.println("user id is " + pso.user_id);
	BaseUser bu = BaseUser.getByUserId(pso.user_id);
	
	List ghostList = BaseUser.getAuthorizedSchemas(pso.user_id);
	
	if ((attempting_select != null) && (attempting_select.equalsIgnoreCase("true"))){
		
		String selected_schema = (String) request.getParameter("selected_schema");
		
		User user = pso.loginToSchema(pso.user_id, selected_schema, request);
				
		if ((user.isSim_author()) || (user.isSim_instructor()) ) {
			pso.schema = selected_schema;
			response.sendRedirect("intro.jsp");
			return;
		} else {
			pso.errorMsg = "Not authorized to author or instruct simulations.";
			response.sendRedirect("index.jsp");
			return;
		}

	} // End of if login in.
			
%>
<html>
<head>
<title>USIP Open Simulation Platform Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../usip_osp.css" rel="stylesheet" type="text/css">
</head>

<body>
<p>&nbsp;</p>
<p>&nbsp;</p>
<table width="720" border="0" cellspacing="0" cellpadding="0" align="center" bgcolor="#DDDDFF">
<tr> 
    <td width="24" height="24" >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3"><P>&nbsp;</P>
      <h1 align="center">&nbsp;&nbsp;&nbsp;USIP Online Simulation Creation</h1>
      <blockquote> 
        <blockquote>
          <p>You belong to multiple organizations.</p>
          <p>Please pick from the list below the organization that you will be 
            working on this session.</p>
        </blockquote>
      </blockquote>
      <form name="form1" method="post" action="pick_schema.jsp" target="_top">
	  	<input type="hidden" name="attempting_select" value="true">
        <blockquote> 
          <blockquote> 
            <p> 
              <select name="selected_schema">
			  <%
			  	for (ListIterator<SchemaGhost> li = ghostList.listIterator(); li.hasNext();) {
            		SchemaGhost this_sg = (SchemaGhost) li.next();
				%>
				<option value="<%= this_sg.getSchema_name() %>"><%= this_sg.getSchema_organization() %> : <%= this_sg.getSchema_name() %></option>
			<% } %>
              </select></font>
              </p>
            <p>
              <input type="submit" name="Submit" value="Submit">
            </p>
          </blockquote>
        </blockquote>
      </form>
      <blockquote> 
        <blockquote>
          <p><center> </p>
          <p align="center">&nbsp;</p>
        </blockquote>
      </blockquote></td>
  </tr>
  <tr> 
    <td width="24" height="24" >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
</table>

<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Creation Software Wizard is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
</body>
</html>
<%
	
%>