<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,java.io.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	afso.backPage = "review_sim.jsp";
	
	String loadSim = (String) request.getParameter("loadSim");
	if ((loadSim != null) && (loadSim.equalsIgnoreCase("true"))) {
		afso.sim_id = new Long((String) request.getParameter("sim_id"));
	}
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="third_party_libraries/jquery/jquery-1.4.1.js"></script>
<script  type="text/javascript">

// Here is where we do the div show/hide magjic
// I found lots of tutorials on how to do this online.
// One really good one was at http://www.sohtanaka.com/web-design/easy-toggle-jquery-tutorial/
// Of course wee need to work this to get it to look like the rest of the site.

$(document).ready(function(){

	//Hide (Collapse) the toggle containers on load
	$(".toggle_container").hide(); 

	//Switch the "Open" and "Close" state per click
	$("h3.trigger").toggle(function(){
		$(this).addClass("active");
		}, function () {
		$(this).removeClass("active");
	});

	//Slide up and down on click
	$("h3.trigger").click(function(){
		$(this).next(".toggle_container").slideToggle("slow");
	});

});
</script>
<style type="text/css">
h3.trigger {
	padding: 0 0 10 50px;
	margin: 0 0 5px 0;
	height: 28px;
	line-height: 28px;
	width: 450px;
	font-size: 2em;
	font-weight: normal;
}
h3.trigger a {

	text-decoration: none;
	display: block;
}
h3.trigger a:hover { color: #ccc; }

.toggle_container {
	margin: 0 0 5px;
	padding: 0;
	border-top: 1px solid #d6d6d6;
	overflow: hidden;
	font-size: 1.2em;
	width: 500px;
	clear: both;
}
.toggle_container .block {
	padding: 20px;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>USIP Open Simulation Platform</title>


<link href="usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1>Review Simulation</h1>
			  <p>This is a public facing review of this simulation. Many details are concealed here to prevent students from inadvertently finding out details that might spoil their experience during the play of the simulation. If you would like to contact the administer of this site to </p>
			  <br />
			
	        <blockquote>
	          <h2>Simulation Name: </h2>
			  <blockquote>
			    <p><%= simulation.getDisplayName() %></p>
			    <p>&nbsp;</p>
			  </blockquote>
			  <blockquote><hr /></blockquote>
		  <h2>Simulation Learning Objectives</h2>
			  <blockquote>
			    <p><%= simulation.getLearning_objvs() %></p>
			    <p>&nbsp;</p>
			  </blockquote>
			  <blockquote><hr /></blockquote>
		  <h2>Simulation Audience</h2>
			  <blockquote>
			    <p><%= simulation.getAudience() %></p>
			    <p>&nbsp;</p>
			  </blockquote>
			  <blockquote><hr /></blockquote>
		  
		<h2>Simulation Introduction</h2>
			  <blockquote>
			    <p><%= simulation.getIntroduction() %></p>
			    <p>&nbsp;</p>
			  </blockquote>
			  <blockquote><hr /></blockquote>
			  
		<h2>Simulation Planned Play Ideas </h2>
			  <blockquote>
			    <p><%= simulation.getPlannedPlayIdeas() %></p>
			    <p>&nbsp;</p>
			  </blockquote>
			  <blockquote><hr /></blockquote>
		  
		<h2>6. Simulation Phases </h2>
			  <blockquote>
			    <p>
			      
			      <%
		for (ListIterator li = simulation.getPhases(afso.schema).listIterator(); li.hasNext();) {
			SimulationPhase sp = (SimulationPhase) li.next();
			
		%>
			      
			      <%= sp.getPhaseName() %><br />
			      <%
	}
%>
			      </p>
			    <p>&nbsp;</p>
			  </blockquote>
			  <blockquote><hr /></blockquote>
		  
		
		  <h2>7. &amp; 8. Simulation Actors </h2>
		  <blockquote>
		    
		    <% for (ListIterator la = simulation.getActors(afso.schema).listIterator(); la.hasNext();) {
					Actor act = (Actor) la.next();
					
					String pub_desc = act.getPublic_description();
					String semi_desc = act.getSemi_public_description();
					String priv_desc = act.getPrivate_description();
					
			  %>
		    
<h3 class="trigger"><a href="#"><%= act.getActorName() %></a></h2>
<div class="toggle_container">
	<div class="block">
		<blockquote>
            <strong>Public Description:</strong><br />
			<%=  pub_desc %><hr />
		</blockquote>
	</div>
</div>    
		    <% } // End of loop over Actors %>
		    
		    <P>		    </P>
		    </blockquote>
          <blockquote>&nbsp;</blockquote>
		      <p>&nbsp;</p>
	          </blockquote>

	  	    <a href="simulation_facilitation/facilitate_library.jsp"><img src="Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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