<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,com.oreilly.servlet.*" 
	errorPage="" %>
<%
		ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
		
		pso.backPage = "../simulation_authoring/create_actors.jsp";
		
		///////////////////////////////////////////////////////////////////
		Simulation simulation = new Simulation();
		if (pso.sim_id != null) {
			simulation = pso.giveMeSim();
		}

		pso.handleCreateActor(request);
		
		Actor actorOnScratchPad = new Actor();
		if (pso.actor_id != null) {
			actorOnScratchPad = pso.giveMeActor();
					
			// TODO remove code below. it shouldn't happen. pso.actor_id is getting bad old data somehow		
			if (actorOnScratchPad == null){
				actorOnScratchPad = new Actor();
			}		
		}

		boolean inEditMode = false;
		
		// ////////////////////////////////////////////////////////////////////////////
		List actorL = new Actor().getAll(pso.schema);
		// ///////////////////////////////////////////////////////////////////////

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">
<% String canEdit = (String) session.getAttribute("author"); %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
        <tr>
          <td><div align="center"><a href="intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else { %>
		<tr>
          <td><div align="center"><a href="../simulation_facilitation/instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		String bgColor_think = "#475DB0";
		String bgColor_create = "#475DB0";
		String bgColor_play = "#475DB0";
		String bgColor_share = "#475DB0";
		
		pso.findPageType(request);
		
		if (pso.page_type == 1){
			bgColor_think = "#9AABE1";
		} else if (pso.page_type == 2){
			bgColor_create = "#9AABE1";
		} else if (pso.page_type == 3){
			bgColor_play = "#9AABE1";
		} else if (pso.page_type == 4){
			bgColor_share = "#9AABE1";
		}
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td bgcolor="<%= bgColor_think %>"><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;THINK&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
	    <td bgcolor="<%= bgColor_create %>"><a href="creationwebui.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;CREATE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
		<td bgcolor="<%= bgColor_play %>"><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;PLAY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
        <td bgcolor="<%= bgColor_share %>"><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;SHARE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
  </tr>
</table>
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Create / Edit Actor</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" -->
<form enctype="multipart/form-data" action="create_actors.jsp" method="post">
  <p>
    <input type="hidden" name="sending_page" value="create_actors" />
  </p>
        <table width="50%" border="0" cellspacing="2" cellpadding="2">
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">Actor 
              Name</font><a href="helptext/actor_name.jsp" target="helpinright">(?)</a>: </td>
            <td valign="top"><input type="text" name="actor_name" value="<%= actorOnScratchPad.getName() %>" tabindex="1" /></td>
          </tr>
          <!-- tr>
      <td valign="top">Non-Player Character</td>
      <td valign="top"><input type="checkbox" name="npc" value="true">
      Yes</td>
    </tr -->
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">Public 
              Description</font><a href="helptext/actor_public_desc.jsp" target="helpinright">(?)</a>: </td>
            <td valign="top"><textarea id="public_description" name="public_description" style="height: 120px; width: 480px;"><%= actorOnScratchPad.getPublic_description() %></textarea>
            <script language="javascript1.2">
  			generate_wysiwyg('public_description');
		</script></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">Semi 
              Public Description</font><a href="helptext/actor_semi_public_desc.jsp" target="helpinright">(?)</a>: </td>
            <td valign="top"><textarea id="semi_public_description" name="semi_public_description" style="height: 120px; width: 480px;"><%= actorOnScratchPad.getSemi_public_description() %></textarea>
            <script language="javascript1.2">
  			generate_wysiwyg('semi_public_description');
		</script>			</td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">Private 
              Description</font><a href="helptext/actor_private_desc.jsp" target="helpinright">(?)</a>: </td>
            <td valign="top"><textarea id="private_description" name="private_description" style="height: 120px; width: 480px;"><%= actorOnScratchPad.getPrivate_description() %></textarea>
            <script language="javascript1.2">
  			generate_wysiwyg('private_description');
		</script></td>
          </tr>
		  <% 
			if (pso.simulationSelected) {    
		  %>
           <tr>
             <td valign="top">&nbsp;</td>
             <td valign="top"><label>
               <% String isShown = "";
			   		if (actorOnScratchPad.isShown()) {
						isShown = "checked";
					}
				%>
			  <input name="actor_shown" type="checkbox" value="true" <%= isShown %> />
		    Shown</label></td>
             <td valign="top">This actor is shown to other actors. [need to make this work] </td>
           </tr>
           <tr>
		  	<td valign="top">&nbsp;</td>
			<td valign="top"><label>
               <% String isControl = "";
			   		if (actorOnScratchPad.isControl_actor()) {
						isControl = "checked";
					}
				%>
			  <input name="control_actor" type="checkbox" value="true" <%= isControl %> />
		    Control</label></td>
            <td valign="top">This actor is a control character.</td>
          </tr>
          <tr>
		  	<td valign="top">&nbsp;</td>
			<td valign="top"><label>
			  <input name="add_to_sim" type="checkbox" value="true" checked="checked" />
		    Add</label></td>
            <td valign="top">Add this actor to the simulation <%= simulation.getDisplayName() %></td>
          </tr>
		  <% } %>
          <tr> 
            <td colspan="3" valign="top"><input type="hidden" name="MAX_FILE_SIZE" value="100000" />
              Choose an image file to upload: 
              <input name="uploadedfile" type="file" tabindex="5" /></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
            <td valign="top">&nbsp;</td>
            <td valign="top">
<input type="hidden" name="actorid" value="<%= pso.actor_id %>" /> 
              <input type="submit"  name="update_actor" value="Update Actor" /> 

              <input type="submit" name="create_actor" value="Create Actor" /> 

              <input type="submit" name="clear_button" value="Clear" /></td>
          </tr>
        </table>
  <p> 
  
  </p>

</form>
<% 
if ((actorOnScratchPad.getImageFilename() != null) && (actorOnScratchPad.getImageFilename().trim().length() > 0)){ %>
<img src="../osp_core/images/actors/<%= actorOnScratchPad.getImageFilename() %>">
<% } %>

<hr />
<p>Below are listed alphabetically all of the current Actors.</p>
<blockquote> 
  <table>
       <%
		for (ListIterator li = actorL.listIterator(); li.hasNext();) {
			Actor aa = (Actor) li.next();
			
		%>
    <tr>
      <td><a href="create_actors.jsp?editmode=true&actorid=<%= aa.getId() %>" ><%= aa.getName() %></td>
      <td>&nbsp;</td>
      <td>
	  <% if ((aa.getName() != null) && (!(aa.getName().equalsIgnoreCase("control")))) { %>
	  <a href="delete_object.jsp?object_type=actor&objid=<%= aa.getId().toString() %>&object_info=<%= aa.getName() %>"> 
        Remove <%= aa.getName() %> </a>
		<% } %>
		</td>
    </tr>
    <%
	}
%>
  </table>
</blockquote>
<p>&nbsp;</p>
      <p align="center"><a href="assign_actor_to_simulation.jsp">Next Step: Assign Actor 
        to Simulation</a></p>
      <a href="create_simulation_phases.jsp">&lt;- Back</a><!-- InstanceEndEditable -->
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
<%
	
%>
