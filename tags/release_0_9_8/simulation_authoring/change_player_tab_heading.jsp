<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*,org.usip.osp.graphs.*" 
	errorPage="../error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	SimulationSectionAssignment ssa = new SimulationSectionAssignment();
	
	String sec_id = request.getParameter("sec_id");
	if (sec_id != null) {
		ssa = SimulationSectionAssignment.getById(afso.schema, new Long(sec_id));
	}
	
	String sending_page = request.getParameter("sending_page");
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("change_player_tab_heading"))){
		String new_tab_heading = request.getParameter("new_tab_heading");
		ssa.setTab_heading(new_tab_heading);
		ssa.save(afso.schema);
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform Delete Object Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" width="100%" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Change Player Tab Heading </h1>
              <p><br />
                    <blockquote>
                This page alllows you to change the tab heading for this player. It does not change the default 'tab heading' for this section.      </p>
              <p>&nbsp;        </p>
              <form action="change_player_tab_heading.jsp" method="post" name="form1" id="form1">
			  <input type="hidden" name="sending_page" value="change_player_tab_heading" />
			  <input type="hidden" name="sec_id" value="<%= sec_id %>" />
        <table width="90%" border="1">
          <tr>
            <td><strong>Current Tab Heading </strong></td>
            <td><%= ssa.getTab_heading() %></td>
          </tr>
          <tr>
            <td><strong>New Tab Heading </strong></td>
            <td><input type="text" name="new_tab_heading" border="1" value="<%= ssa.getTab_heading() %>" /></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td><input type="submit" name="change_tab_heading" value="Change Tab Heading" /></td>
          </tr>
        </table>
		</form>

<a href="<%= afso.backPage %>" target="bodyinleft"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>
			Back</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
