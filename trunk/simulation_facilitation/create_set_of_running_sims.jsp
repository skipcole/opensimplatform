<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_facilitation/create_running_sim.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	Simulation simulation = new Simulation();	
	
	if (afso.sim_id != null){
		simulation = afso.giveMeSim();
	}
	
	///////////////
	String create_set = (String) request.getParameter("create_set");
	
	if (create_set != null) {
		String set_name = (String) request.getParameter("set_name");
		RunningSimSet rss = new RunningSimSet();
		rss.setRunningSimSetName(set_name);
		rss.setSim_id(afso.sim_id);
		rss.saveMe(afso.schema);
	}
	
	
	RunningSimSet rssQueued = new RunningSimSet();
    String display_rss = (String) request.getParameter("display_rss");
	
	if ((display_rss != null) && (display_rss.equalsIgnoreCase("true"))) {
    	String rss_id = (String) request.getParameter("rss_id");
		rssQueued = RunningSimSet.getMe(afso.schema, new Long(rss_id));
    }
	
	String sending_page = (String) request.getParameter("sending_page");
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("set_of_running_sims"))) {
    	String rss_id = (String) request.getParameter("rss_id");
		rssQueued = RunningSimSet.getMe(afso.schema, new Long(rss_id));
    }	
    
	/////////////////

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>USIP Open Simulation Platform</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
			  <h1>Create Set of Running Simulations</h1>
			  <br />
            <blockquote> 
              <% 
			if (afso.sim_id != null) {
		%>
              <p>Create set of running simulations for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
                (If you would like to create running simulations for a different simulation, 
                <a href="../simulation_authoring/select_simulation.jsp">click here</a>.)</p>
              <p>Current sets of running simulations</p>
              <p><% 
			 
			  	List rssList = RunningSimSet.getAllForSim(afso.sim_id.toString(), afso.schema);
			
				for (ListIterator li = rssList.listIterator(); li.hasNext();) {
					RunningSimSet rss = (RunningSimSet) li.next();
			  
			  %>
                <a href="create_set_of_running_sims.jsp?rss_id=<%= rss.getId() %>&display_rss=true"><%= rss.getRunningSimSetName() %></a><br />
              <% } %>
              </p>
              <% if (rssQueued.getId() != null) { 
			  
			  Hashtable rsInSet = RunningSimSet.getHashSetOfRunningSims(afso.schema, rssQueued.getId());
				
			  %>
              <p>Below are the running simulations currently in the set  <b> <%= rssQueued.getRunningSimSetName() %> </b>. </p>
              <blockquote>
                <p>
                <form id="form2" name="form2" method="post" action="create_set_of_running_sims.jsp">
                <input type="hidden" name="sending_page" value="set_of_running_sims" />
                <input type="hidden" name="rss_id" value="<%= rssQueued.getId() %>" />
<table width="80%" border = "1">
                <tr> 
                  <td><h2>Running Simulation</h2></td>
                  <td><h2>In Set</h2></td>
                </tr>
                <%
		  	List rsList = RunningSimulation.getAllForSim(afso.sim_id.toString(), afso.schema);
			
			for (ListIterator li = rsList.listIterator(); li.hasNext();) {
				RunningSimulation rs = (RunningSimulation) li.next();
				
				String checked = "";
				
				if (rsInSet.contains(rs.getId()){
					checked = "checked=\"checked\"";
				}
				
		%>
                <tr> 
                  <td><%= rs.getRunningSimulationName() %></td>
                  <td>
                    <label>
                      <input type="checkbox" name="rsid_<%= rs.getId() %>" id="checkbox" <%= checked %> />
                      </label>
                  </td>
                </tr>
                <%
			}
		%>
                </table>
                </form>
                </p>
              </blockquote>
              <% } %>
              <p><br />
              </p>
            </blockquote>
            <form action="create_set_of_running_sims.jsp" method="post" name="form1" id="form1">
              <h2>
                <input type="hidden" name="sending_page" value="create_set_of_running_sims" />
                Create New Running Sim Set              </h2>
              <table width="80%" border="0" cellspacing="2" cellpadding="2">
                <tr> 
                  <td>Enter Set Name</td>
              <td><input type="text" name="set_name" /></td>
            </tr>
                <tr> 
                  <td>&nbsp;</td>
              <td><input type="submit" name="create_set" value="Create" /></td>
            </tr>
                </table>
            </form>
            <p align="center">&nbsp;</p>
            <% } else { // End of if have set simulation id. %>
            <blockquote> 
              <p>                </p>
            <%@ include file="../simulation_authoring/select_message.jsp" %>
                  </blockquote>
            <p>
              <% } // End of if have not set simulation for edits. %>
                  </p>
            <p>&nbsp;</p>
            <% 
		if (!(afso.isAuthor())) { %>
	  		<a href="instructor_home.jsp" target="_top">&lt;-- Back            </a>
	        <% } %>			</td>
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
