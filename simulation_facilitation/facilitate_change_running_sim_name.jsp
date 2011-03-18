<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	String rs_id = (String) request.getParameter("rs_id");
	
	RunningSimulation rs = new RunningSimulation();
	
	if ((rs_id != null) && (!(rs_id.equalsIgnoreCase("null")))) {
		rs = RunningSimulation.getById(afso.schema, new Long(rs_id));
		
		String sending_page = (String) request.getParameter("sending_page");
		
		if ((sending_page != null) && ((sending_page.equalsIgnoreCase("rs_changename")))) {
		
			String rs_new_name = (String) request.getParameter("rs_new_name");
			
			rs.setName(rs_new_name);
			rs.saveMe(afso.schema);
		
		}
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Rename Running Simulation</h1>
              <blockquote>
                <p>Changing the name mid-game can be confusing to the players, so it is not advised. But if for any reason you need to change the name of your Running Simulation, you may do it here. </p>
                <h1><a href="helptext/enable_sim_help.jsp" target="helpinright"></a></h1> 
        <% 
			if ((rs_id == null) || (rs_id.equalsIgnoreCase("null"))) {
				// show nothing
			} else {
			

		%>
  
    <form action="facilitate_change_running_sim_name.jsp" method="post" name="form1" id="form1">
      <input type="hidden" name="sending_page" value="rs_changename" />
	  <input type="hidden" name="rs_id" value="<%= rs_id %>" />
      <table width="100%" border="1" cellspacing="0" cellpadding="2">
        <tr valign="top">
          <td>Current Name: </td>
          <td><%= rs.getName() %></td>
        </tr>
        <tr valign="top">
          <td>New Name: </td>
          <td><label>
            <input type="text" name="rs_new_name" value="<%= rs.getName() %>" />
          </label></td>
        </tr>
        <tr valign="top"> 
          <td width="34%">&nbsp;</td>
                <td width="66%"> <input type="submit" name="command" value="Change Name" /></td>
              </tr>
        </table>
    </form>
    </blockquote>
        <%
		
	}// end of if  rs_id has not been passed in

%>        <blockquote>
          <div align="center">
            <p align="left"><a href="facilitate_create_running_sim.jsp">&lt;- 
        Back</a></p>
          </div>
          </blockquote>			</td>
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