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
		
		String act_id = request.getParameter("act_id");
		
		if ( (act_id != null) && (!(act_id.equalsIgnoreCase("")))){
			afso.actor_being_worked_on_id = new Long(act_id);
		}
		
		Actor actorOnScratchPad = new Actor();
		if (afso.actor_being_worked_on_id != null) {
			afso.handleCreateActorImages(request);
			actorOnScratchPad = afso.giveMeActor();
		}

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
              <h1>Set Actor Images for <%= actorOnScratchPad.getActorName() %></h1>
              <p>On this page you will set the images for an actor. You can either upload an image file from your local machine to the server, or browse the server for an image to use.</p>
              <br />
    <form enctype="multipart/form-data" action="create_actors_image.jsp" method="post">
    
    <input type="hidden" name="sim_id" value="<%= afso.sim_id %>" />
    
      <p>
        <input type="hidden" name="sending_page" value="create_actors" />
        <span class="style1"><%= afso.errorMsg %></span>
        <% afso.errorMsg = ""; %>
        </p>
          <table width="100%" border="0" cellspacing="2" cellpadding="2">
            <tr>
              <td align="left" valign="top"><h2>Actor 
                <%= actorOnScratchPad.getActorName() %></h2></td>
              <td align="left" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
            </tr>
            <tr>
              <td valign="top">Chat Color (?) 
                <select name="chat_color" >
                  <option value="ffffff">White</option>
                  <option value="ffdddd">Light Red</option>
                  <option value="ddffdd">Light Green</option>
                  <option value="ddddff">Light Blue</option>
                </select> 
                (not implemented) </td>
              <td valign="top" bgcolor="#FFFFCC">&nbsp;</td>
              <td valign="top" bgcolor="#FFFFCC">&nbsp;</td>
            </tr>
            <tr> 
              <td align="left" valign="top"><input type="hidden" name="MAX_FILE_SIZE" value="100000" />
                Upload Image: 
                  <input name="uploadedfile" type="file" tabindex="5" /></td>
              <td align="left" valign="top">or</td>
              <td align="left" valign="top">Browser Server (Coming soon) </td>
            </tr>
            <tr> 
              <td align="left" valign="top">
                Upload Thumbnail Image: 
                  <input name="uploaded_thumb_file" type="file" tabindex="5" /></td>
              <td align="left" valign="top">or</td>
              <td align="left" valign="top">Browse Server (Coming soon) </td>
            </tr>
            <tr>

              <td align="left" valign="top">
  <input type="hidden" name="actorid" value="<%= afso.actor_being_worked_on_id %>" />
  <%
				if (afso.actor_being_worked_on_id != null) {
				%>
  <input type="submit" name="set_images" value="Set Images" />
                <%
				} else {
				%>
                <input type="hidden" name="sim_id" value="<%= afso.sim_id %>" />
                <%
					}
				%>              </td>
              <td align="left" valign="top">&nbsp;</td>
              <td align="left" valign="top">&nbsp;</td>
            </tr>
            </table>
    </form>
    <p>
      <% 
if ((actorOnScratchPad.getImageFilename() != null) && (actorOnScratchPad.getImageFilename().trim().length() > 0)){ %>
        <img src="../osp_core/images/actors/<%= actorOnScratchPad.getImageFilename() %>">
        <% } %>
      <% 
if ((actorOnScratchPad.getImageThumbFilename() != null) && (actorOnScratchPad.getImageThumbFilename().trim().length() > 0)){ %>
          <img src="../osp_core/images/actors/<%= actorOnScratchPad.getImageThumbFilename() %>">
          <% } %>
    </p>
	<hr />
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p align="center"><a href="assign_actor_to_simulation.jsp"></a></p>    
    <a href="create_actors.jsp"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
