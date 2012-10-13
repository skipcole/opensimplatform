<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.communications.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Add Special Features Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<body>
<blockquote>
              <h1>View Injects for <strong><%= simulation.getDisplayName() %></strong></h1>
</blockquote>
           <% if (afso.sim_id != null) {%>
		<table border="0" width="100%" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" valign="top">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%" valign="top">
          <table width="100%" border="0" cellspacing="0" cellpadding="4" valign="top">
            <tr>
              <td colspan="4"><strong><u>Current Inject Groups and Injects</u></strong></td>
              </tr>
            <%
			for (ListIterator li = InjectGroup.getAllForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
			InjectGroup ig = (InjectGroup) li.next();
		%>
            <tr>
              <td width="25%" valign="top"><strong><%= ig.getName() %></strong></td>
              <td width="13%" valign="top">&nbsp;</td>
              <td width="27%" valign="top">&nbsp;</td>
              <td width="35%" valign="top">&nbsp;</td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td colspan="3" valign="top"><strong>Group Description:</strong> <%= ig.getDescription() %></td>
            </tr>
            <% 
		  List injectList = Inject.getAllForSimAndGroup(afso.schema, afso.sim_id, ig.getId());
		  
		   if (injectList.size() > 0) { 
		  	for (ListIterator lii = injectList.listIterator(); lii.hasNext();) {
			Inject da_inject = (Inject) lii.next();
		  
		  %>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top"><strong>inject name:</strong></td>
              <td colspan="2" valign="top"><strong><%= da_inject.getInject_name() %></strong></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">inject text:</td>
              <td colspan="2" valign="top"><%= da_inject.getInject_text() %></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">inject notes:</td>
              <td colspan="2" valign="top"><%= da_inject.getInject_Notes() %></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td colspan="2" valign="top"><a href="inject_create.jsp?sending_page=view&queueup=true&inj_id=<%= da_inject.getId() %>">update</a> / 
                <a href="delete_object.jsp?object_type=inject&objid=<%= da_inject.getId() %>&object_info=<%= da_inject.getInject_name() %>"> 
                  delete</a></td>
            </tr>
            <% }  // End of loop over injects.  %>
		  <% } else { %>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td colspan="2" valign="top">No injects in this group yet. </td>
            </tr>
            <% } %>
            <tr>
              <td colspan="4" valign="top"><hr/></td>
            </tr>
          <% } // end of loop over inject groups %>
            </table>
          </td></tr></table>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>
      
    <p>  
            <a href="injects.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>
</p>

    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>


<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
