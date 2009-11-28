<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String sp_id = (String) request.getParameter("sp_id");
	
	SimulationPhase spOnScratchPad = SimulationPhase.getMe(afso.schema, new Long(sp_id));
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
			  <h1>Advanced Phase Features</h1>
			  <br />
            <blockquote> 

              <blockquote>
                <form action="create_simulation_phases.jsp" method="post" name="form1" id="form1">
              <table width="80%" border="0" cellspacing="2" cellpadding="2">
                <tr> 
                  <td valign="top">Phase Name:</td>
                  <td valign="top"><input type="text" name="phase_name" value="<%= spOnScratchPad.getName() %>" /></td>
                </tr>
                <tr>
                  <td valign="top">Meta Phase</td>
                  <td valign="top"><label>
<select name="metaphase" id="metaphase">
  <option value="none">None</option>
</select>                  
(<a href="create_simulation_metaphases.jsp">Create Meta Phase</a>)</label></td>
                </tr>
                <tr>
                  <td valign="top">&nbsp;</td>
                  <td valign="top">place holder to create copies of objects</td>
                </tr>
                <tr>
                  <td valign="top">&nbsp;</td>
                  <td valign="top">place holder to creatre new simulation sections based on player objects</td>
                </tr>
                <tr> 
                  <td valign="top">&nbsp;</td>
                  <td valign="top">
  
                    <input type="hidden" name="sp_id" value="<%= spOnScratchPad.getId() %>" />
                    <input type="submit" name="command" value="Update" />                </td>
                </tr>
                </table>
              <p>&nbsp;</p>
            </form>
          </blockquote>
            </blockquote>            <a href="create_simulation_phases.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
