<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*,
	org.usip.osp.specialfeatures.*" 
	errorPage="" %>
<% 
	PlayerSessionObject pso = PlayerSessionObject.getPSO(request.getSession(true));
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	SimSectionRSDepOjbectAssignment ssrsdoa = 
		SimSectionRSDepOjbectAssignment.getOneForRunningSimSection(pso.schema, pso.running_sim_id, cs.getId(), 0);
		
	GenericVariable gv = GenericVariable.getMe(pso.schema, ssrsdoa.getObjectId());
	
	String sending_page = request.getParameter("sending_page");
	
	if ((sending_page != null) && (sending_page.equalsIgnoreCase("set_parameter"))){
		String gv_value = request.getParameter("gv_value");
		gv.setValue(gv_value);
		gv.saveMe(pso.schema);
		
	}
		
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>

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
</head>
<body onLoad="">
<BR />
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Set Parameter</h1>
              <br />
			 
      <form action="player_set_parameter.jsp" method="post" name="form2" id="form2">
      	<input type="hidden" name="cs_id" value="<%= cs_id %>" />
        <blockquote>
          <p><%= cs.getBigString() %></p>
          Value: 
          
          <input type="text" name="gv_value" id="textfield" value="<%= gv.getValue() %>" />
          
          <p>
              <input type="hidden" name="custom_page" value="<%= cs_id %>" />
              <input type="hidden" name="sending_page" value="set_parameter" />
              <input type="submit" name="save_page" value="Submit" />
              </p>
            <p>&nbsp;</p>
          </blockquote>
      </form></td>
		</tr>
		</table>	</td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
</html>