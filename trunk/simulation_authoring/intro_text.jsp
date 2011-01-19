<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.io.*,java.util.*,java.text.*,
	java.sql.*,
	org.usip.osp.networking.*,
	org.usip.osp.baseobjects.*" 
errorPage="../error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	afso.backPage = "intro_text.jsp";
	
	Simulation simulation = new Simulation();
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />

<style type="text/css">
<!--
.style1 {
	font-family: "Courier New", Courier, monospace;
	font-weight: bold;
}
-->
</style>
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1 align="center"> Welcome <%= afso.userDisplayName %> ! </h1>
              <% if (afso.sim_id != null) { %>
              	<p>You are working on simulation <span class="style1"><%= simulation.getSimulationName() %></span>.</p>
                <p>To select/create a different simulation, <a href="select_simulation.jsp">click here</a>.</p>
              <% } %>
			  <br />            <table  background="../Templates/images/page_bg.png" align="center" cellpadding="0" cellspacing="0">
              <tr> 
                <td width="80%"> 
                  <p><font size="+1">We have created this wizard to help 
                      <a href="helptext/intro_basichelp.jsp" target="helpinright">you, a subject matter expert</a>, create an <a href="helptext/intro2_basichelp.jsp" target="helpinright">online training simulation</a> and 
                    then be able to <a href="helptext/intro3_basichelp.jsp" target="helpinright">share</a> it with others.</font>
                    </blockquote>
                  </p>

          <p><font size="+1">Links across the top of this page will walk you through all of the basic steps in this process:</font></p>
  

          <ol>
            <li><font size="+1">Think: Some advice on creating online simulations</font></li>
              <li><font size="+1"><a href="control_panel.jsp">Create</a>: Use this tool to create online simulations</font></li>
              <li><font size="+1">Play: Expose players to the simulations you have 
                created and gain valuable feedback.</font></li>
              <li><font size="+1">Share: Compact and upload your Simulation to share 
                with others.</font></li>
            </ol>

          </blockquote>
</p>        </td>
    </tr>
            </table>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>
</body>
</html>