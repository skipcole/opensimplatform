<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.oscw.communications.*,
		org.usip.oscw.networking.*,
		org.usip.oscw.persistence.*,
		org.usip.oscw.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	Simulation sim = pso.handleMakeCaucusPage(request);
	
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect(pso.backPage);
		return;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Make Caucus  Page</h1>
    <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote> 
	  <form action="make_caucus_page.jsp" method="post" name="form2" id="form2">
        <blockquote><strong>Tab Heading</strong>: 
          <input type="text" name="tab_heading" value="<%= pso.tab_heading %>"/>
        <p>Select the Actors to be included in this Caucus. If desired, assign them a designated role (such as participant, visitor, etc.) </p>
        <p><%
		
		MultiSchemaHibernateUtil.beginTransaction(pso.schema);
		
		Conversation conv = new Conversation();
		
		if (pso.sim_conv_id != null) {
			conv = (Conversation) MultiSchemaHibernateUtil.getSession(pso.schema).get(Conversation.class, pso.sim_conv_id);
		}	
			for (ListIterator la = sim.getActors().listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();
				String checked = "";
				if (conv.hasActor(act.getId())){
					checked = " checked ";
				}
			%>
          <label><input type="checkbox" name="actor_cb_<%= act.getId().toString() %>" value="true" <%= checked %> /> 
          <%= act.getName() %></label> 
          <label>
          <input type="text" name="role" />
          </label>
          <br/>	 
		<% } // End of loop over Actors 
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
		%></p>
        <p>Enter any text that will appear on this page. <br>
        </p>

      
          <p>
            <textarea id="text_page_text" name="text_page_text" style="height: 710px; width: 710px;"><%= pso.customizableSectionOnScratchPad.getBigString() %></textarea>

		<script language="javascript1.2">
  			generate_wysiwyg('text_page_text');
		</script>
          </p>
          <p> 
            <input type="hidden" name="custom_page" value="<%= pso.custom_page %>" />
            <input type="hidden" name="sending_page" value="make_caucus_page" />
            <input type="submit" name="save_page" value="Save" />
            <input type="submit" name="save_and_add" value="Save and Add Section" />
          </p>
          <p><input type="submit" name="create_duplicate" value="Create Duplicate" disabled /></p>
        </blockquote>
      </form>
	  <a href="<%= pso.backPage %>">&lt;-- Back</a><!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
<%
	
%>
