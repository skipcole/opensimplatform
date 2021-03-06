<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.baseobjects.*,
		org.usip.osp.modelinterface.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*" 
	errorPage="/error.jsp" %>

<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	ModelSystemController.handleInstallModels(request, afso.schema);        

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
</head>
<body onLoad="">

<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3">
        <h1>
          Install Models</h1>

        <blockquote>
          <p>Below are listed all of the model descriptor files found on this system. On this system,
          model definition files are located in the directory: <%= FileIO.getModel_dir() %></p>
  <table width="100%" cellpadding="2" cellspacing="2" border="1"><tr>
  <td valign="top"><strong>Model Name / Version</strong></td>
  <td valign="top"><div align="right"><strong>State</strong></div></td>
  <td valign="top"><strong>Action</strong></td>
  </tr>
	<% for (ListIterator li = ModelSystemController.screenModelsFromXMLFiles(afso.schema).listIterator(); li.hasNext();) {
			ModelDefinitionObject mdo = (ModelDefinitionObject) li.next(); 
			
			Long loaded_id = ModelDefinitionObject.checkInstalled(afso.schema, mdo);
			
			boolean loaded = false;
			
			if (loaded_id != null){
				loaded = true;
			}
			
			%>
            <tr><td valign="top"><%= mdo.getModelName() %></td><td valign="top"><div align="right">
            <% if (loaded) { %>
            Loaded
            <% } else { %>
            Not Loaded
            <% } %>
            </div></td>
              <td valign="top"><div align="right">
              <form action="install_simulation_sections.jsp" method="post">
              <input type="hidden" name="fullfileloc" value="<%= mdo.getModelDirectory() %>" />
              <input type="hidden" name="loaded_id" value="<%= loaded_id %>" />
            <% if (loaded) { %>
            <input type="submit" name="command" id="unload_button" value="Unload" />
            <input type="submit" name="command" id="reload_button" value="Reload" />
            <% } else { %>
            <input type="submit" name="command" id="load_button" value="Load" />
            <% } %>
            </form>
            </div></td>
            </tr>
	<% } %>
    </table>
  </p>

        </blockquote>
        <p><a href="simulation_admin.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>
      <p>&nbsp;</p>
      <p>&nbsp;</p></td>
  </tr>
  <tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p>

<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP 
  Open Source Software Project</a>. </p>
</body>
</html>