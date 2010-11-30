<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.communications.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	Conversation thisConv = afso.handleCreateConversation(request);
	
	Simulation sim = new Simulation();	
	
	if (afso.sim_id != null){
		sim = afso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" /></head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Create/Edit Conversations Page</h1>
              <br />
    <p>Conversations associated with a simulation represent places where a set of players can come to chat.
    <form action="make_create_conversation_page.jsp" method="post" name="form2" id="form2">
      
      <h2>Create New Conversation</h2>
            <table width="100%">
              <tr>
                <td valign="top">Unique Conversation Name <a href="#" target="helpinright">(?)</a>:</td>
              <td valign="top"><input type="text" name="uniq_conv_name" value="<%= thisConv.getUniqueConvName() %>" /></td></tr>
              <tr>
                <td valign="top">Conversation Notes:</td>
                <td valign="top"><label>
                  <textarea name="conv_notes" id="conv_notes" cols="45" rows="5"><%= thisConv.getConversationNotes() %></textarea>
                </label></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>	      <p>Select the Actors to be included in this conversation, and if desired, assign them a designated role <a href="actors_chat_role_help.jsp" target="helpinright">(?)</a>. </p>
	      <p><%
				
			for (ListIterator la = sim.getActors(afso.schema).listIterator(); la.hasNext();) {
				Actor act = (Actor) la.next();
				String checked = "";
				String role = "";
				
				ConvActorAssignment caa = ConvActorAssignment.getSpecificCAA(afso.schema, act.getId(), thisConv.getId());
				
				if (caa != null){
					checked = " checked ";
					role = caa.getRole();
				} 
				
			%>
            <label><input type="checkbox" name="actor_cb_<%= act.getId().toString() %>" value="true" <%= checked %> /> 
              <%= act.getActorName() %></label>
            , 
            <strong>role</strong> (<em>optional</em>):
            <label>
              <input type="text" name="role_<%= act.getId().toString() %>" value="<%= role %>" />
              </label>
            <br/>	 
            <% } // End of loop over Actors 
		
			%></p></td>
              </tr>
              <tr><td>&nbsp;</td><td>
              
              <% if (thisConv.getId() == null) { %>
              
              <input type="submit" name="create_conv" value="Create" />
              
              <%
				} else {
				%>
                <input type="hidden" name="conv_id" value="<%= thisConv.getId() %>" />
				<table width="100%"><tr>
                <td align="left"><input type="submit"  name="update_conv" value="Update" /></td>
				<td align="right"><input type="submit" name="clear_button" value="Clear to Create New Conversation" /></td>
				</tr></table>
                <%
					}
				%>
              
              </td></tr>
              </table>
              <input type="hidden" name="sending_page" value="make_create_conversation_page" />
      </p>
    </form>
      <p>&nbsp;</p>
      <p>Below are listed all of the conversations currently associated with this simulation. </p>
      <table width="100%" border="1">
  <tr><td><strong>Uniq Identifier</strong></td>
  <td><strong>Conversation Notes</strong></td>
  <td><strong>Delete</strong></td>
  </tr>
        <%
			  		int ii = 0;
					for (ListIterator li = Conversation.getAllBaseForSim(afso.schema, afso.sim_id).listIterator(); li.hasNext();) {
						Conversation conv = (Conversation) li.next();
				%>
        
          <tr><td><a href="make_create_conversation_page.jsp?conv_id=<%= conv.getId() %>&queueup=true"><%= conv.getUniqueConvName() %></a></td>
                <td><%= conv.getConversationNotes() %></td>
                <td>delete*</td>
                </tr>
          
                <%
					}
				%>
      </table>
      <p><br />
      * This feature has not yet been implemented.</p>
      <p><a href="<%= afso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
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
