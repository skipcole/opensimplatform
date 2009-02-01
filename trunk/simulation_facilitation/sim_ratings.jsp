<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	if (!(pso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String sending_page = (String) request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("sim_ratings"))) {
		
		pso.handleEnterInstructorRatings(request);
		
	}
	
	///////////////////////////////////////////////////////////////////////
	String sim_id = (String) request.getParameter("sim_id");
	if (sim_id != null) {
		pso.sim_id = new Long(sim_id);
	}
	Simulation sim = new Simulation();	
	if (pso.sim_id != null){
		sim = pso.giveMeSim();
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
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
<%
	String myLogoutPage = pso.getBaseSimURL() + "/simulation/logout.jsp";
	
	if ( (pso.isAuthor())  || (pso.isFacilitator())) {
		myLogoutPage = pso.getBaseSimURL() + "/simulation_authoring/logout.jsp";
	}
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if (pso.isAuthor()) { %>
        <tr>
          <td><div align="center"><a href="../simulation_authoring/intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else if (pso.isFacilitator()) { %>
		<tr>
          <td><div align="center"><a href="instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="<%= myLogoutPage %>" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
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
		
		if (pso.isAuthor()) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td bgcolor="<%= bgColor_think %>"><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;THINK&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
	    <td bgcolor="<%= bgColor_create %>"><a href="../simulation_authoring/creationwebui.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;CREATE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
		<td bgcolor="<%= bgColor_play %>"><a href="facilitateweb.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;PLAY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
        <td bgcolor="<%= bgColor_share %>"><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;SHARE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
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
      <h1>Ratings for Simulation: <%= sim.getName() %> : <%= sim.getVersion() %>  </h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <blockquote>
      <p>Simulation Blurb</p>
      <p><%= sim.getBlurb() %></p>
      <hr />
      <h2>Instructor Simulation Ratings</h2>
      <table border="1" cellpadding="1" cellspacing="0">
      <tr><td><strong>Stars</strong></td>
      <td><strong>Commentor</strong></td>
      <td><strong>Comments</strong></td>
      </tr>
	  <%
	  	for (ListIterator li = SimulationRatings.getRatingsBySim(pso.schema, pso.sim_id, SimulationRatings.INSTRUCTOR_COMMENT).listIterator(); li.hasNext();) {
			SimulationRatings sr = (SimulationRatings) li.next();
			
	  %>
      <tr><td><%= sr.getNumberOfStars() %></td><td><%= sr.getUsers_stated_name() %></td><td><%= sr.getUser_comments() %></td></tr>
      <%
	  } // end of loop over Sim ratings.
	  %>
      </table>
      <p>&nbsp;</p>
      <h2>Your Rating</h2>
      <%
	  	SimulationRatings sr_i = SimulationRatings.getInstructorRatingsBySimAndUser(pso.schema, pso.sim_id, pso.user_id);
		
			String selected_num_1 = "";
			String selected_num_2 = "";
			String selected_num_3 = "";
			String selected_num_4 = "";
			String selected_num_5 = "";
			String selected_num_6 = "";
			String selected_num_7 = "";
			String selected_num_8 = "";
			String selected_num_9 = "";
			String selected_num_10 = "";
			
			if (sr_i.getNumberOfStars() == 1){
				selected_num_1 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 2){
				selected_num_2 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 3){
				selected_num_3 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 4){
				selected_num_4 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 5){
				selected_num_5 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 6){
				selected_num_6 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 7){
				selected_num_7 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 8){
				selected_num_8 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 9){
				selected_num_9 = " selected ";
			}
			if (sr_i.getNumberOfStars() == 10){
				selected_num_10 = " selected ";
			}
			
	  %>
      <form id="form1" name="form1" method="post" action="sim_ratings.jsp">
      <input type="hidden" name="sending_page" value="sim_ratings" />
      <table border="1" cellpadding="1" cellspacing="0">
        <tr>
          <td><strong>Stars</strong></td>
          <td><strong>Comments</strong></td>
          <td><strong>Save Changes</strong></td>
        </tr>
        <tr>
          <td valign="top"><select name="num_stars" id="select">
            <option value="1" <%= selected_num_1 %> >1</option>
            <option value="2" <%= selected_num_2 %> >2</option>
            <option value="3" <%= selected_num_3 %> >3</option>
            <option value="4" <%= selected_num_4 %> >4</option>
            <option value="5" <%= selected_num_5 %> >5</option>
            <option value="6" <%= selected_num_6 %> >6</option>
            <option value="7" <%= selected_num_7 %> >7</option>
            <option value="8" <%= selected_num_8 %> >8</option>
            <option value="9" <%= selected_num_9 %> >9</option>
            <option value="10" <%= selected_num_10 %> >10</option>
                                        </select></td>
          <td valign="top"><label>
            <textarea name="user_comments" id="user_comments" cols="45" rows="5"><%= sr_i.getUser_comments() %></textarea>
          </label></td>
          <td valign="top"><label></label></td>
        </tr>
        <tr>
          <td valign="top">&nbsp;</td>
          <td valign="top">Your Name (optional):
            <label>
            <input type="text" name="user_stated_name" id="textfield" value="<%= sr_i.getUsers_stated_name() %>" />
            </label></td>
          <td valign="top"><input type="submit" name="button" id="button" value="Submit" /></td>
        </tr>
</table>
        <p>&nbsp;</p>
      </form>
      
           <p>&nbsp;</p>
           <h2>Player Simulation Ratings</h2>
           <table border="1" cellpadding="1" cellspacing="0">
      <tr><td valign="top"><strong>Actor</strong></td>
      <td valign="top"><strong>User's Stated Name</strong></td>
      <td valign="top"><strong>Comments</strong></td>
      </tr>
              <%
	  	for (ListIterator li = SimulationRatings.getRatingsBySim(pso.schema, pso.sim_id, SimulationRatings.PLAYER_COMMENT).listIterator(); li.hasNext();) {
			SimulationRatings sr = (SimulationRatings) li.next();
	  %>
      <tr><td valign="top"><%= sr.getActor_name() %></td><td valign="top"><%= sr.getUsers_stated_name() %></td><td valign="top"><%= sr.getUser_comments() %></td></tr>
        <%
	  } // end of loop over Sim ratings.
	  %>
      </table>
        
            <p>&nbsp;</p>
      </blockquote>
      

      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
<%
	
%>
