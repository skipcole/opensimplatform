<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.sql.*, java.util.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.networking.*" 
errorPage="/error.jsp" %><%


	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
		//////////////////////////////////
	// Get list of all simulations for pull down
	List simList = Simulation.getAll(afso.schema);
	
	String saveMsg = "";
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("run_eie_test"))){
	
		String sim_id = (String) request.getParameter("sim_id");
		String filename = (String) request.getParameter("filename");
		
		saveMsg = afso.handleExportImportExportTest(sim_id, filename);
		
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>USIP Open Simulation Platform</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
			<h1>Export / Import Diagnostic Test</h1>
			<!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
			<p>Pick a simulation below and test it.</p><table width="100%" border="1">
            <tr><td>Simulation</td><td>File Name</td><td>Submit</td></tr>
            <%
		
		for (ListIterator li = simList.listIterator(); li.hasNext();) {
			Simulation sim = (Simulation) li.next();
			String nameToSend = java.net.URLEncoder.encode(sim.getSimulationName());
			
		%>
            <tr> 
              <form id="form2" name="form2" method="post" action="import_export_copy_test.jsp">
                <td>
                  <input type="hidden" name="sending_page" value="run_eie_test" />
                  <input type="hidden" name="sim_id" value="<%= sim.getId().toString() %>" />
                  <%= sim.getSimulationName() %> : <%= sim.getVersion() %>                  </td>
              <td>
                <input name="filename" type="text" id="textfield" value="<%= afso.getDefaultDiagnosticXMLFileName(sim, "ver_a") %>" size="60" />                </td>
              <td><label>
                <input type="submit" name="button" id="button" value="Run Test" />
                </label>            </td>
			  </form>
            </tr>
            <%
	}
%>
            </table></p>
			<p><%= saveMsg %></p>
			<p>2.) Export to File (version A)</p>
			<p>3.) Import from File</p>
			<p>4.) Export to File (version B)</p>
			<p>5.) Compare version A and version B.</p>
			<p>This Test will import the test simulation for this version, then export it, then import it again and compare the first simulation with the second one. Alte</p>
			<!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
