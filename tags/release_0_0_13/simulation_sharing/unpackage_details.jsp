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
	
	
	String loaddetails = (String) request.getParameter("loaddetails");
	Simulation sim = new Simulation();
	if ((loaddetails != null) && (loaddetails.equalsIgnoreCase("true"))){
		sim = afso.handleUnpackDetails(request);
	}
	
	String unpack = (String) request.getParameter("unpack");
	
	if ((unpack != null) && (unpack.equalsIgnoreCase("true"))){
		afso.handleUnpackSimulation(request);
		response.sendRedirect("unpackage_results.jsp");
		return;
	}
	
	
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
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
              <h1>Unpackage Details</h1>
              <br />
    <p>Enter information concerning this import.</p>
    <form id="form1" name="form1" method="post" action="">
  <table>
  <tr><td>Name:</td>
    <td><label>
      <input type="text" name="sim_name" id="sim_name" value="<%= sim.getSimulationName() %>" />
      </label></td>
    </tr>
  <tr>
    <td>Version:</td>
    <td><label>
      <input type="text" name="sim_version" id="sim_version" value="<%= sim.getVersion() %>" />
      </label></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><label>
      <input type="hidden" name="unpack" value="true" />
      <input type="submit" name="button" id="button" value="Submit" />
      </label></td>
  </tr>
    </table>
      </form>
    <p>&nbsp;</p>    <p>&nbsp;</p>			</td>
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
