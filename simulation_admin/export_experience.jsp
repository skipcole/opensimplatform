<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.hibernate.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_facilitation/create_running_sim.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}
	
	 
	String sending_page = request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("export_experience"))) {
	
		String sim_id = request.getParameter("sim_id");
		System.out.println(sim_id);
		
		if ((sim_id != null) && (!(sim_id.equalsIgnoreCase("none")))){
			afso.sim_id = new Long(sim_id);
		} else {
			afso.sim_id = null;	
		}
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>USIP Open Simulation Platform</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
                <td width="100%"><h1> <br />
                  Export Experience (<span class="style1">Work In Progress</span>) </h1>
				  <blockquote>
                  <table width="90%" border="1">
                    <tr>
                      <td><h2>Instructions</h2>
                  <p>To export experience from a simulation, you must do the following</p>
                  <ol>
                    <li>Select the simulation</li>
                    <li>Select the running simulations and aspects of them (listed below) that you want to export 
                      <ul>
                        <li>Instructor Tips (T) </li>
                        <li>Instructor Injects (I) </li>
                        <li>Player Responses to Injects (RT) </li>
                        <li>Student Reflections (R) </li>
                      </ul>
                    </li>
                    <li>Enter in information about this export (name, reason, etc.)</li>
                    <li>Hit the submit button at the bottom of the page.  <br />
                          </li>
                  </ol></td>
                    </tr>
                  </table>
                    <p>&nbsp;</p>

                      <h2>Select the Simulation </h2>
                    <form id="form1" name="form1" method="post" action="export_experience.jsp">
					<input type="hidden" name="sending_page" value="export_experience"  />
					      <select name="sim_id">
					        <option value="none">Non Selected</option>
					        <% 
						  List simList = Simulation.getAll(afso.schema);
  
  							for (ListIterator li = simList.listIterator(); li.hasNext();) {
								Simulation sim = (Simulation) li.next();
							%>
					        <option value="<%= sim.getId() %>" <%= USIP_OSP_Util.matchSelected(afso.sim_id, sim.getId(), " selected ") %>   ><%= sim.getSimulationName() %></option>
					        <% } // End of loop over simulations %>
				          </select>
			              <input type="submit" name="Submit" value="Change" />
				      </form>
					    <% if (afso.sim_id != null) { 
					
					Simulation sim = Simulation.getById(afso.schema, afso.sim_id);
					
					%>
					  <h2>Running Simulations for Simulation <%= sim.getDisplayName() %>   </h2>
					  <form id="form2" name="form2" method="post" action="">
					    <table width="100%" border="0">
					      <tr>
					        <td width="5%">&nbsp;</td>
                          <td>Running Simulation </td>
                          <td width="10%">Phase</td>
                          <td>T</td>
                          <td>I</td>
                          <td>R</td>
                          <td>RT</td>
                          <td>&nbsp;</td>
                        </tr>
					      <%
					  	List rsList = RunningSimulation.getAllForSim(afso.sim_id, afso.schema);	
	
						for (ListIterator lirs = rsList.listIterator(); lirs.hasNext();) {
							RunningSimulation rs = (RunningSimulation) lirs.next();
					  %>
					      <tr>
					        <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>
                            <input type="checkbox" name="checkbox" value="checkbox" />                            </td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
					      <%
					  	} // end of loop over running simulations
						%>
				        </table>
					  </form>
					  <p>
					    <% } // end of if sim id not null %>
				      </p>
					  <h2>Export File Details</h2>
					  <p>&nbsp;</p>
					  <p>Submit</p>
					  <p>&nbsp; </p>
				    </blockquote>
                  </blockquote>                  </td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
        </tr>
      </table></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>
<%
%>
