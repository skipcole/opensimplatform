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
              <h1>Unpackage Details</h1>
              <br />
    <p>Enter information concerning this import.</p>
    <form id="form1" name="form1" method="post" action="">
  <table width="100%">
  <tr><td width="23%">Name:</td>
    <td width="77%"><label>
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
    <td>Simulation <br />
      USIP OSP Version </td>
    <td><%= sim.getSoftwareVersion() %></td>
  </tr>
  <tr>
    <td>Your <br />
      USIP OSP Version </td>
    <td><%= USIP_OSP_Properties.getRelease() %></td>
  </tr>
  
  <% int release_relation = USIP_OSP_Properties.compareRelease(sim.getSoftwareVersion()); 
  
    String sim_software_code = sim.getSoftwareVersion().replaceAll("\\.","_");
    String this_software_code = USIP_OSP_Properties.getRelease().replaceAll("\\.","_");
	
  	String upgradeFileName = "USIP_OSP_Upgrade_";
	
	upgradeFileName += sim_software_code;
	upgradeFileName += "_to_";
	
	upgradeFileName += this_software_code;
	upgradeFileName += ".xml";
  
  	boolean showSubmitButton = true;
  %>

  <% if (release_relation == USIP_OSP_Properties.FUTURE_SOFTWARE_VERSION_MAJOR) { 
  
  	showSubmitButton = false;
	
  %>
    <tr>
    <td>&nbsp;</td>
    <td><span class="style1">This simulation was created using a future major release of the USIP OSP software. You must upgrade this server installation Before importing this simulation.</span></td>
  </tr>
  <% } // End of if trying to import simulation from future major release   
  
  	else if (release_relation == USIP_OSP_Properties.FUTURE_SOFTWARE_VERSION_MINOR) { 
  
  		showSubmitButton = false;
	
  %>
  <tr>
    <td>&nbsp;</td>
    <td><span class="style1">This simulation was created using a future minor release of the USIP OSP software. You must upgrade this server installation Before importing this simulation.</span></td>
  </tr>
	  <% } // End of if trying to import simulation from future minor release   
  
  	else if (release_relation == USIP_OSP_Properties.FUTURE_SOFTWARE_VERSION_MICRO) { 
	
  %>
  <tr>
    <td>&nbsp;</td>
    <td>This simulation was created using a future micro release of the USIP OSP software. This should not cause any compatibilitity issues. </td>
  </tr>
  <% } // End of if trying to import simulation from future micro release
    	else if (release_relation == USIP_OSP_Properties.EARLIER_SOFTWARE_VERSION_MICRO) { 
  %>
  <tr>
    <td>&nbsp;</td>
    <td>This simulation was created using an earlier micro release of the USIP OSP software. This should not cause any compatibilitity issues. </td>
  </tr>
    <% } // End of if trying to import simulation from earlier micro release
    	else if (release_relation == USIP_OSP_Properties.EARLIER_SOFTWARE_VERSION_MINOR) { 
  %>
  <tr>
    <td>&nbsp;</td>
    <td>This simulation was created with an earlier minor release, and the update file <%= upgradeFileName %> was found. This simulation will be updated to your current version of the USIP OSP. </td>
  </tr>
  
  
  <tr>
    <td>&nbsp;</td>
    <td><span class="style1">This simulation was created with an earlier minor release, and the update file <%= upgradeFileName %> was not found. Obtain this update file before continuing. It can be found at ... </span></td>
  </tr>
  
    <% } // End of if trying to import simulation from earlier minor release
    	else if (release_relation == USIP_OSP_Properties.EARLIER_SOFTWARE_VERSION_MAJOR) { 
  %>
  
  <tr>
    <td>&nbsp;</td>
    <td>This simulation was created with an earlier major release, and the update file <%= upgradeFileName %> was found. This simulation will be updated to your current version of the USIP OSP. </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><span class="style1">This simulation was created with an earlier major release, and the update file <%= upgradeFileName %> was not found. Obtain this update file before continuing. It can be found at ... </span></td>
  </tr>
  
    <% } // End of if trying to import simulation from earlier major release
  %>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><label>
      <input type="hidden" name="unpack" value="true" />
	  <% if (showSubmitButton) { %>
      <input type="submit" name="button" id="button" value="Submit" />
	  <% } // end of if show submit button.  %>
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
