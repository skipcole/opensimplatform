<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,com.oreilly.servlet.*" 
	errorPage="../error.jsp" %>
<%
		AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
		afso.backPage = "../simulation_authoring/create_actors.jsp";
		
		if (!(afso.isLoggedin())) {
			response.sendRedirect("index.jsp");
			return;
		}

		afso.handleCreateActor(request);
		
		Actor actorOnScratchPad = new Actor();
		if (afso.actor_being_worked_on_id != null) {
			actorOnScratchPad = afso.giveMeActor();
					
			// TODO remove code below. it shouldn't happen. afso.actor_being_worked_on_id is getting bad old data somehow		
			if (actorOnScratchPad == null){
				actorOnScratchPad = new Actor();
			}		
		}

		boolean inEditMode = false;
		
		///////////////////////////////////////////////////////////////////
		List actorL = Actor.getAll(afso.schema);
		///////////////////////////////////////////////////////////////////
		Simulation simulation = new Simulation();
		if (afso.sim_id != null) {
			simulation = afso.giveMeSim();
			actorL = Actor.getAllForSimulation(afso.schema, afso.sim_id);
		}
		// ///////////////////////////////////////////////////////////////////////

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>


<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>

<% 
			if (afso.sim_id != null) {    
%>
          

<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Create / Edit Actor</h1>
              <br />
    <form enctype="multipart/form-data" action="create_actors.jsp" method="post">
    
    <input type="hidden" name="sim_id" value="<%= afso.sim_id %>" />
    
      <p>
        <input type="hidden" name="sending_page" value="create_actors" />
        <span class="style1"><%= afso.errorMsg %></span>
        <% afso.errorMsg = ""; %>
        </p>
          <table width="50%" border="0" cellspacing="2" cellpadding="2">
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">Actor 
                Name</font><a href="helptext/actor_name.jsp" target="helpinright">(?)</a>: </td>
              <td valign="top"><input type="text" name="actor_name" value="<%= actorOnScratchPad.getName() %>" tabindex="1" />
                <% if (actorOnScratchPad.getId() != null) { %>
                	<% if (actorOnScratchPad.isControl_actor()) { %>
                    	<B><I>Control Character <a href="helptext/control_actor.jsp"  target="helpinright">(?)</a></I></B>
                        <% } // end of if this is a control character %>
                <% } %>            </td>
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
					wysiwygWidth = 480;
					wysiwygHeight = 120;
  			generate_wysiwyg('public_description');
		</script></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">Semi 
                Public Description</font><a href="helptext/actor_semi_public_desc.jsp" target="helpinright">(?)</a>: </td>
              <td valign="top"><textarea id="semi_public_description" name="semi_public_description" style="height: 120px; width: 480px;"><%= actorOnScratchPad.getSemi_public_description() %></textarea>
                <script language="javascript1.2">
					wysiwygWidth = 480;
					wysiwygHeight = 120;
  			generate_wysiwyg('semi_public_description');
		</script>			</td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">Private 
                Description</font><a href="helptext/actor_private_desc.jsp" target="helpinright">(?)</a>: </td>
              <td valign="top"><textarea id="private_description" name="private_description" style="height: 120px; width: 480px;"><%= actorOnScratchPad.getPrivate_description() %></textarea>
                <script language="javascript1.2">
					wysiwygWidth = 480;
					wysiwygHeight = 120;
  					generate_wysiwyg('private_description');
				</script></td>
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
			    Control <a href="helptext/control_actor.jsp"  target="helpinright">(?)</a></label></td>
              <td valign="top">This actor is a control character.</td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
			  <td valign="top"><label>
			    <input name="add_to_sim" type="checkbox" value="true" checked="checked" />
			    Assign</label></td>
              <td valign="top" >Assign this actor to the simulation <%= simulation.getDisplayName() %></td>
            </tr>
            <!-- tr>
              <td valign="top">&nbsp;</td>
              <td valign="top" bgcolor="#FFFFCC">Actor's Role in Sim(?)</td>
              <td valign="top" bgcolor="#FFFFCC">
                <textarea name="actors_role" id="actors_role"  style="height: 120px; width: 480px;" ><% if ((actorOnScratchPad.getId() != null) && (simulation.getId() != null) ) { %><%= actorOnScratchPad.getRole(afso.schema, simulation.getId()) %><% } %>
              </textarea>            </td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top" bgcolor="#FFFFCC">Chat Color(?)</td>
              <td valign="top" bgcolor="#FFFFCC"><select name="chat_color" >
                <option value="ffffff">White</option>
                <option value="ffdddd">Light Red</option>
                <option value="ddffdd">Light Green</option>
                <option value="ddddff">Light Blue</option>
  </select> </td>
            </tr    -->
            <tr> 
              <td colspan="3" valign="top"><input type="hidden" name="MAX_FILE_SIZE" value="100000" />
                Choose an image file to upload: 
                <input name="uploadedfile" type="file" tabindex="5" /></td>
            </tr>
            <tr> 
              <td colspan="3" valign="top">
                Choose an thumbnail (chat) image file to upload: 
                <input name="uploaded_thumb_file" type="file" tabindex="5" /></td>
            </tr>
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td valign="top">
  <input type="hidden" name="actorid" value="<%= afso.actor_being_worked_on_id %>" />
  <%
				if ((afso.actor_being_worked_on_id == null) || (afso.actor_being_worked_on_id.equals(new Long(0))) ) {
				%>
  <input type="submit" name="create_actor" value="Create Actor" />
                <%
				} else {
				%>
                <input type="hidden" name="sim_id" value="<%= simulation.getId() %>" />
                <input type="submit" name="clear_button" value="Clear" />
                <input type="submit"  name="update_actor" value="Update Actor" /> 
                <%
					}
				%>              </td>
            </tr>
            </table>
    <p>      </p>
    </form>
    <% 
if ((actorOnScratchPad.getImageFilename() != null) && (actorOnScratchPad.getImageFilename().trim().length() > 0)){ %>
    <img src="../osp_core/images/actors/<%= actorOnScratchPad.getImageFilename() %>">
    <% } %>
        <% 
if ((actorOnScratchPad.getImageThumbFilename() != null) && (actorOnScratchPad.getImageThumbFilename().trim().length() > 0)){ %>
    <img src="../osp_core/images/actors/<%= actorOnScratchPad.getImageThumbFilename() %>">
    <% } %>

    <hr />
    <p>Below are listed alphabetically all of the current Actors associated with this simulation.</p>
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
          <% } %>          </td>
      </tr>
        <%
	}
%>
        </table>
      </blockquote>
    <p>&nbsp;</p>
    <p align="center"><a href="assign_actor_to_simulation.jsp">Next Step: Assign Actor 
      to Simulation</a></p>    <a href="create_simulation_phases.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
		</tr>
		</table>	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
  </tr>
</table>

<% } else { // End of if have set simulation id. %>
	</TD></TR>
    <TR><TD>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>

</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>
