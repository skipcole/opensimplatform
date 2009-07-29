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
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	CustomizeableSection cs = pso.handleMakePrivateChatPage(request);
	
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect(pso.backPage);
		return;
	}
	
	Simulation sim = new Simulation();
	
	if (pso.sim_id != null){
		sim = pso.giveMeSim();
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
              <h1>Make Private Chat Pages</h1>
              <br />
      <blockquote> 
	  <form action="make_private_chat_page.jsp" method="post" name="form2" >
	    <blockquote><strong>Tab Heading</strong>: 
	      <input type="text" name="tab_heading" value="<%= cs.getRec_tab_heading() %>"/>
	      <p>Fill out the top right part of the grid below to determine the sets of characters that will have a private chat window. </p>
          <table border="1"><tr><td>&nbsp;</td>
		  <%
			for (ListIterator la = sim.getActors(pso.schema).listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();
				String checked = "";
			%>
            <td><strong><%= act.getName() %></strong></td>
		  <% } // End of loop over Actors %>
            </tr>
            <%
			int index_row = 0;
			for (ListIterator la = sim.getActors(pso.schema).listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();
				index_row += 1;
			%>
            <tr><td><strong><%= act.getName() %></strong></td>
				  <%
				// Get this from pso
				Hashtable setConversations = pso.setOfConversationForASection(cs.getId());
				
				int index_col = 0;
				for (ListIterator la2 = sim.getActors(pso.schema).listIterator(); la2.hasNext();) {
					Actor act2 = (Actor) la2.next();
					index_col += 1;
				
					String checked = "";	
					if ((!(act2.getName().equalsIgnoreCase(act.getName()))) && (index_col > index_row)){
						
						String getKey = act.getId() + "_" + act2.getId();
						
						if (setConversations.get(getKey) != null){
							checked = " checked ";
						}
				
				%>
              <td><input type="checkbox" 
						name="act_cb_<%= act.getId().toString() %>_<%= act2.getId().toString() %>" 
						value="true" <%= checked %> /></td>
					  <% }  else { // end of if names are not alike. %>
              <td>&nbsp;</td>
					  <% } %>
              <% } // End of loop over Actor Columns! %>
              </tr>
            <% } // End of loop over Actor Rows! %>
            </table>
        
          <p>
            <script language="javascript1.2">
  			generate_wysiwyg('text_page_text');
		</script>
            </p>
            <p> 
              <input type="hidden" name="custom_page" value="<%= pso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
              <input type="hidden" name="sending_page" value="make_private_chat_page" />
              <input type="submit" name="save_and_add" value="Save and Add Sections" />
              </p>
            <p>&nbsp;</p>
          </blockquote>
      </form>	  <a href="<%= pso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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