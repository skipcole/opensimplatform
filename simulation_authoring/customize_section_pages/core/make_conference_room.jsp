<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,
		org.usip.osp.communications.*,
		org.usip.osp.networking.*,
		org.usip.osp.persistence.*,
		org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true), true);
	
	Conversation conv = afso.handleMakeMeetingRoomPage(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	CustomizeableSection cs = afso.getMyPSO_SectionMgmt().getCustomizableSectionOnScratchPad();
	
	Simulation sim = new Simulation();	
	
	if (afso.sim_id != null){
		sim = afso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

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
              <h1>Make Meeting Room</h1>
              <br />
      <blockquote> 
	  <form action="make_conference_room.jsp" method="post" name="form2" id="form2">
	    <blockquote><strong>Tab Heading</strong>: 
	      <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
	      <p>Select the Actors to be included in this meeting room, and if desired, assign them a designated role <a href="actors_chat_role_help.jsp" target="helpinright">(?)</a>. </p>
          <p><%
				
			for (ListIterator la = sim.getActors(afso.schema).listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();
				String checked = "";
				String role = "";
				
				ConvActorAssignment caa = ConvActorAssignment.getSpecificCAA(afso.schema, act.getId(), conv.getId());
				
				if (caa != null){
					checked = " checked ";
					role = caa.getRole();
				} 
				
			%>
            <label><input type="checkbox" name="actor_cb_<%= act.getId().toString() %>" value="true" <%= checked %> /> 
              <%= act.getName() %></label>
            , 
            <strong>role</strong> (<em>optional</em>):
            <label>
              <input type="text" name="role_<%= act.getId().toString() %>" value="<%= role %>" />
              </label>
            <br/>	 
            <% } // End of loop over Actors 
		
			%></p>
          <p>Title that will appear for this room.</p>
          <p><input type="text" name="page_title" id="page_title" value="<%= cs.getPageTitle() %>" />
          </p>
          <p>Enter any text that will appear on this page. <br>
          </p>
          <p>
              <textarea id="text_page_text" name="text_page_text" style="height: 710px; width: 710px;"><%= cs.getBigString() %></textarea>
              
              <script language="javascript1.2">
  			generate_wysiwyg('text_page_text');
		</script>
              </p>
            <p> 
              <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
              <input type="hidden" name="sending_page" value="make_caucus_page" />
              <input type="submit" name="save_page" value="Save" />
              <input type="submit" name="save_and_add" value="Save and Add Section" />
            </p>
            </blockquote>
      </form>	  <a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
<%
	
%>
