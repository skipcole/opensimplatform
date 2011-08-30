<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,com.oreilly.servlet.*" 
	errorPage="../error.jsp" %>
<%
		AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
		afso.backPage = "../simulation_authoring/create_actors.jsp";
		
		if (!(afso.isLoggedin())) {
			response.sendRedirect("../blank.jsp");
			return;
		}

		Actor actorOnScratchPad = new Actor();
		if (afso.actor_being_worked_on_id != null) {
			actorOnScratchPad = afso.giveMeActor();
					
			// TODO remove code below. it shouldn't happen. afso.actor_being_worked_on_id is getting bad old data somehow		
			if (actorOnScratchPad == null){
				actorOnScratchPad = new Actor();
			}		
		}
		
		String selectimage = request.getParameter("selectimage");
		String setimage = request.getParameter("setimage");
		
		if ((setimage != null) && (setimage.equalsIgnoreCase("true"))){
		
			String img_name = request.getParameter("img_name");
			
			actorOnScratchPad = afso.giveMeActor();
			
			if ((selectimage != null) && (selectimage.equalsIgnoreCase("mainimg"))){
			
				actorOnScratchPad.setImageFilename(img_name);
				
			} else if ((selectimage != null) && (selectimage.equalsIgnoreCase("thumbimg"))){
			
				actorOnScratchPad.setImageThumbFilename(img_name);
				
			}
			
			actorOnScratchPad.saveMe(afso.schema);
			response.sendRedirect("create_actors_image.jsp");
			return;
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
<style type="text/css">
<!--
img.sample_image {
	max-width:200px;
	max-height:200px;
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
              <h1>Browse Actor Images for <%= actorOnScratchPad.getActorName() %></h1>
              <table width="100%" border="1" cellspacing="0">
			         <%
					 List imageList = afso.getImageFiles();
					 
		for (ListIterator li = imageList.listIterator(); li.hasNext();) {
			Vector actV = (Vector) li.next();
			
			String fileLoc = (String) actV.get(0);
			String fileName = (String) actV.get(1);
			
		%>
                <tr>
                  <td valign="top"><img src="<%= fileLoc %>" alt="image" class="sample_image" /></td>
                  <td valign="top"><%= fileName %></td>
                  <td valign="top"><a href="browse_actors_image.jsp?setimage=true&selectimage=<%= selectimage %>&img_name=<%= fileName %>">Select</a></td>
                </tr>
				
		<% } %>
              </table>
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
