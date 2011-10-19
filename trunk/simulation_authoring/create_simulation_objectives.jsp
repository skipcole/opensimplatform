<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="../error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	if (!(afso.isLoggedin())) {
		response.sendRedirect("../blank.jsp");
		return;
	}

	Simulation simulation = afso.handleWizardPage(request, afso.SIM_OBJECTIVES);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.nextPage);
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
.style1 {color: #FF0000}
-->
</style>

<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Enter Simulation Learning Objectives <a href="helptext/sim_objectives.jsp"  target="helpinright">(?)</a> </h1>
              <br />
      <blockquote>
        <% 
			if (afso.sim_id != null) {
		%>
        <p>Enter your learning objectives for the simulation <strong><%= simulation.getDisplayName() %></strong>.<br>
          (If you would like to work on a different simulation, <a href="select_simulation.jsp">click 
            here</a>.)</p>
      </blockquote>
      <form action="create_simulation_objectives.jsp" method="post" name="form2" id="form2">
        <blockquote>
          <p>
            <textarea id="sim_text" name="sim_text" style="height: 300px; width: 710px;"><%= simulation.getLearning_objvs() %></textarea>
            
            <script language="javascript1.2">
					wysiwygWidth = 710;
					wysiwygHeight = 300;
  			generate_wysiwyg('sim_text');
		</script>
            </p>
          <h1>Hidden objectives</h1>
          <p>If you want to hide some of the objectives of the simulation (so people perusing the objectives on a review page will not see them) put your true objectives here. </p>
          <p>
            <textarea id="sim_hidden_objs" name="sim_hidden_objs" style="height: 300px; width: 710px;"><%= simulation.getHiddenLearningObjectives() %></textarea>
            
            <script language="javascript1.2">
					wysiwygWidth = 710;
					wysiwygHeight = 300;
  			generate_wysiwyg('sim_hidden_objs');
		</script>
            </p>
          <p> 
              <input type="hidden" name="sending_page" value="authoring_wizard_page" />
              <a href="browse_objectives.jsp">Browse Objectives from Other Simulations</a> </p>
            <table width="100%" border="0">
              <tr>
                <td align="center"><input type="submit" name="save" value="Save" /></td>
                <td align="center"><input type="submit" name="cancel" value="Cancel"   onClick="return confirm('Are you sure you want to cancel? All changes will be lost.');"  /></td>
                <td align="center"><label>
                  <input type="submit" name="save_and_proceed" value="Save and Proceed" />
                </label></td>
              </tr>
            </table>
            <p>&nbsp;</p>
        </blockquote>
      </form>
      <p align="center">&nbsp;</p>
      <% } else { // End of if have set simulation id. %>
      <blockquote>
        <p>
          <%@ include file="select_message.jsp" %></p>
      </blockquote>
      <% } // End of if have not set simulation for edits. %>      <p><a href="create_simulation.jsp"></a> </p>			</td>
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