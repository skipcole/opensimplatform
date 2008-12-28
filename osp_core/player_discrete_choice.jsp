<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.communications.*,
	org.usip.osp.specialfeatures.*" 
	errorPage="" %>
<% 
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);
	
	String cs_id = (String) request.getParameter("cs_id");
	CustomizeableSection cs = CustomizeableSection.getMe(pso.schema, cs_id);
	
	pso.takePlayerChoice(request, cs);
	
	// Get the generic variable associated with this decision
	Long currentVarId = (Long) cs.getContents().get(PSO_SectionMgmt.GEN_VAR_KEY);

	// Get list of allowable responses
	List allowableResponses = AllowableResponse.pullOutArs(cs, pso.schema);
	
	////////////////////////////////////////
	Vector answersSelected = pso.selectedChoices(currentVarId, allowableResponses);
	
	///////////////////////////////////////
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

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
              <h1>Player Make Decision</h1>
              <br />
			 
      <form action="player_discrete_choice.jsp" method="post" name="form2" id="form2">
      	<input type="hidden" name="cs_id" value="<%= cs_id %>" />
        <input type="hidden" name="num_ars" value="<%= allowableResponses.size() %>" />
        <blockquote>
          <p><%= cs.getBigString() %></p>
            <table width="100%" border="0">
              <%
		
		int ii = 0;
		for (ListIterator li = allowableResponses.listIterator(); li.hasNext();) {
			AllowableResponse ar = (AllowableResponse) li.next();	
			%>
              <tr>
                <td width="16%" valign="top">
                <input type="radio" name="players_choice" id="players_choice" value="<%= ar.getId() %>" <%= (String) answersSelected.get(ii) %> />
                <% ii += 1; %>
                </td>
                <td width="16%" valign="top"><%= ar.getIndex() %></td>
                <td width="67%" valign="top">
                  <%= ar.getResponseText() %>
                  <input type="hidden" name="ar_id_<%= ar.getIndex() %>" value="<%= ar.getId() %>" />
                  </label></td>
                </tr>
              <% } // end of loop over allowable responses %>
              </table>
            <p>
              <input type="hidden" name="custom_page" value="<%= pso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
              <input type="hidden" name="sending_page" value="player_discrete_choice" />
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
<%
	
%>
