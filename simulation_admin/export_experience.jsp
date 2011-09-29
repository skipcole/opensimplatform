<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.sharing.*,
	org.usip.osp.baseobjects.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	afso.backPage = "../simulation_facilitation/create_running_sim.jsp";
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../login.jsp");
		return;
	}
	
	ExperienceExportObject eeo = ExperienceExportObject.handleExportExperience(request, afso);
	

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
                  Export Experience</h1>
				  
				  <% if (eeo != null) { %>
				  <p>Your experience has been exported to: <%= eeo.getFileName() %></p>
				  <% } else { %>
				  
				  <blockquote>
                  <table width="90%" border="1">
                    <tr>
                      <td><h2>Instructions</h2>
                  <p>To export experience from a simulation, you must do the following</p>
                  <ol>
                    <li>Select the simulation</li>
                    <li>Select the running simulations and aspects of them (listed below) that you want to export 
                      <ul>
                        <li>Instructor Tips (T) (<span class="style1">work in progress</span>) </li>
                        <li>Instructor Injects (I) </li>
                        <li>Player Responses to Injects (RT) (<span class="style1">work in progress</span>)</li>
                        <li>Student Reflections (R) (<span class="style1">work in progress</span>)</li>
                        <li>Timeline of Phase Changes (P) (<span class="style1">work in progress</span>) </li>
                      </ul>
                    </li>
                    <li>Enter in information about this export (name, reason, etc.)</li>
                    <li>Hit the submit button at the bottom of the page.  <br />
                          </li>
                  </ol></td>
                    </tr>
                  </table>
                    <p>&nbsp;</p>

                      <h2>1. Select the Simulation </h2>
					  <blockquote>
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
	
						String defaultFileName = afso.getDefaultExperienceExportXMLFileName(sim);			
					%>
					</blockquote>
					  <h2>2. Running Simulations for Simulation <%= sim.getDisplayName() %>   </h2>
					  <form id="form2" name="form2" method="post" action="export_experience.jsp">
					  <input type="hidden" name="sending_page" value="export_experience_export"  />
					    <blockquote>
						<table width="90%" border="1">
					      <tr>
					        <td valign="top"><strong>Running Simulation </strong></td>
                          <td width="10%" valign="top"><strong>Phase</strong></td>
                          <td valign="top"><strong>T</strong></td>
                          <td valign="top"><strong>I</strong></td>
                          <td valign="top"><strong>R</strong></td>
                          <td valign="top"><strong>RT</strong></td>
                          <td valign="top"><strong>P</strong></td>
					      </tr>
					      <%
					  	List rsList = RunningSimulation.getAllForSim(afso.sim_id, afso.schema);	
	
						for (ListIterator lirs = rsList.listIterator(); lirs.hasNext();) {
							RunningSimulation rs = (RunningSimulation) lirs.next();
							
							SimulationPhase phase = SimulationPhase.getById(afso.schema, rs.getPhase_id());
					  %>
					      <tr>
					        <td valign="top"><%= rs.getName() %></td>
                          <td valign="top"><%= phase.getPhaseName() %></td>
                          <td valign="top">
                            <input type="checkbox" name="t_<%= rs.getId() %>" value="true" />                            </td>
                          <td valign="top"><label>
                            <input type="checkbox" name="i_<%= rs.getId() %>" value="true" />
                          </label></td>
                          <td valign="top"><label>
                            <input type="checkbox" name="r_<%= rs.getId() %>" value="true" />
                          </label></td>
                          <td valign="top"><label>
                            <input type="checkbox" name="rt_<%= rs.getId() %>" value="true" />
                          </label></td>
                          <td valign="top"><input type="checkbox" name="p_<%= rs.getId() %>" value="true" /></td>
					      </tr>
					      <%
					  	} // end of loop over running simulations
						%>
				        </table>
					  	</blockquote>

					  <h2>3. Export File Details</h2>
					  <blockquote>
					  <table width="90%" border="1">
                        <tr>
                          <td valign="top"><label>File Name: </label></td>
                          <td valign="top"><input name="file_name" type="text" size="80" value="<%= defaultFileName %>" /></td>
                        </tr>
                        <tr>
                          <td valign="top">File Description: </td>
                          <td valign="top"><label>
                            <textarea name="file_notes" cols="80"></textarea>
                          </label></td>
                        </tr>
                      </table>
					  </blockquote>
					  <p>&nbsp;</p>

					  <h2>4. Submit</h2>
					  <blockquote>
					    <label>
					    <input type="submit" name="Submit2" value="Submit" />
					    </label>
					  </blockquote>
					  <p>&nbsp; </p></form>
					  
					  					  <p>
					    <% } // end of if sim id not null %>
						<% } // end of if eeo == null %>
				      </p>
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
