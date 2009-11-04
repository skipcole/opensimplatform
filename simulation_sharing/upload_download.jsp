<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*" 
	errorPage="" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
		
	//////////////////////////////////
	// Get list of all simulations for pull down
	List simList = Simulation.getAll(afso.hibernate_session);
	
	// Determine if setting sim to edit.
	String sending_page = (String) request.getParameter("sending_page");
	
	String selected_sim_id = null;
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("create_game_details"))){
		selected_sim_id = (String) request.getParameter("selected_sim_id");
		if (selected_sim_id != null) {
			afso.simulation.id = selected_sim_id;
			afso.simulation.load();
		}
	} // End of if changing sim to set text for.
	
	///////////////////////////////////////
	// 

	
	String game_intro = (String) request.getParameter("game_intro");
	String commit_edits = (String) request.getParameter("commit_edits");
	
	if ( (sending_page != null) && (commit_edits != null) && (sending_page.equalsIgnoreCase("create_game_details2"))){
		
		afso.simulation.id = afso.simulation.id;
		afso.simulation.load();
		afso.simulation.setIntroductionInDB(game_intro);
		selected_sim_id = afso.simulation.id;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



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
              <h1>Upload/Download Simulations</h1>
              <br />
    <form action="unpackage_simulation.jsp" method="post" name="form1" id="form1">
      <p>&nbsp;</p>
    </form>
    <p>This functionality is in the works!</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>    <p>&nbsp;</p>			</td>
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
