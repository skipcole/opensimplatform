<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.communications.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleMakeChatHelpPage(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	// Get from cs hashtable the constant actors
	
	// Get from cs hashtable the visiting actors
			
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../../../wysiwyg_files/wysiwyg.js">
</script>


<link href="../../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../../../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Make Chat Help Pages</h1>
              <br />
      <blockquote> 
	  <form action="make_chat_help_page.jsp" method="post" name="form2" >
	    <blockquote><strong>Tab Heading</strong>: 
	      <input type="text" name="tab_heading" value="<%= cs.getRec_tab_heading() %>"/>
	      <p>Fill out the top right part of the grid below to determine the sets of characters that will have a private chat window. </p>
	      <p><strong>Constant Actor(s)</strong></p>
	      <blockquote>
	      	<%
				for (ListIterator la = SimActorAssignment.getActorsForSim(afso.schema, afso.sim_id).listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next();
					String checked = "";
					
					if (something) {
						checked = "checked=\"checked\"";
					}
			%>
            <label>
            <input name="constant_actor_<%= act.getId() %>" type="checkbox" id="checkbox" <%= checked %> />
            
            <%= act.getActorName() %></label><br />
		  <% } // End of loop over Actors %>
	        </blockquote>
	      <p><strong>Actors with Access to this Chat</strong></p>
	      <blockquote>
	      	<%
				for (ListIterator la = SimActorAssignment.getActorsForSim(afso.schema, afso.sim_id).listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next();
					String checked = "";
					
					if (something) {
						checked = "checked=\"checked\"";
					}
			%>
            <label>
            <input type="checkbox" name="visiting_actor_<%= act.getId() %>" id="checkbox"  <%= checked %> />
            
            <%= act.getActorName() %></label><br />
		  <% } // End of loop over Actors %>
	        </blockquote>

            <script language="javascript1.2">
  			generate_wysiwyg('text_page_text');
		</script>
            </p>
            <p> 
              <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
              <input type="hidden" name="sending_page" value="make_chat_help_page" />
              <%
				
			boolean hasItAlready = SimulationSectionAssignment.determineIfActorHasThisSectionAtThisPhase(afso.schema, 
				afso.sim_id, afso.actor_being_worked_on_id, afso.phase_id, cs.getId());
			
			String actors_name_string = "fill it in from cache";
			
			if (afso.actor_being_worked_on_id.equals(new Long(0))) {
				actors_name_string = " every actor ";
			} else {
				System.out.println("actor id : " + afso.actor_being_worked_on_id);
				actors_name_string = USIP_OSP_Cache.getActorName(afso.schema, afso.sim_id, new Long(0), request, afso.actor_being_worked_on_id);
			}
			
			
			if (!(hasItAlready)) { %>
		    	<p> 
		    	  <input type="submit" name="save_and_add" value="Save and Add Section" />
		    	  Add this to <%= actors_name_string %> in phase <%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, afso.phase_id) %>              </p>
            <% } else { %>
            	<p>
            	  <input type="submit" name="save_page" value="Save" />
            	  This section has already been added to <%= actors_name_string %> for phase <%= USIP_OSP_Cache.getPhaseNameById(request, afso.schema, afso.phase_id) %>.</p>
            <% } %>
            <p>&nbsp;</p>
          </blockquote>
      </form>	  <a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
<%
	
%>
