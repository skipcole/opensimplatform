<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="" %>

<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	Simulation simulation = new Simulation();
	simulation.setCreator(pso.user_Display_Name);
	
	if (pso.sim_id != null){
		simulation = pso.giveMeSim();
	}
	
	String sending_page = (String) request.getParameter("sending_page");
	String addsimulation = (String) request.getParameter("addsimulation");
	
	///////////////////////////////////
	
	boolean justAdded = false;
	
	String debug_string = "";
	
	if ( (sending_page != null) && (addsimulation != null) && (sending_page.equalsIgnoreCase("create_simulation"))){
          pso.createNewSim(request);  
	} // End of if coming from this page and have added simulation.

	//////////////////////////////////
	// Put sim on scratch pad
	String edit_simulation = (String) request.getParameter("edit_simulation");
	
	
	if ((edit_simulation != null) && (edit_simulation.equalsIgnoreCase("true"))){
		
		pso.sim_id = new Long(   (String) request.getParameter("sim_id")   );
		simulation = pso.giveMeSim();
		
		pso.simulationSelected = true;
			
	}
	
	
	//////////////////////////////////
	// Clear sim from scratch pad
	String clear_simulation = (String) request.getParameter("clear_button");
	
	if ((clear_simulation != null) && (clear_simulation.equalsIgnoreCase("Clear"))){
		
		simulation = new Simulation();
		simulation.setCreator(pso.user_Display_Name);
		pso.simulationSelected = false;
			
	}
	
	//////////////////////////////////
	List simList = Simulation.getAll(pso.schema);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
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
			<!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Simulation User Administration</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <form action="../simulation_facilitation/instructor_home.jsp" method="post" name="form1" id="form1">
	  	<input type="hidden" name="sending_page" value="create_simulation" />
        <blockquote>
        <blockquote>
          <p>&nbsp;</p>
        </blockquote>
      </form>
      <blockquote>
        
        <p>&nbsp;</p>
        <br>
      </blockquote>
      <p align="center"></p>

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
