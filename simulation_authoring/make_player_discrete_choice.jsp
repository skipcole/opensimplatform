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
	
	CustomizeableSection cs = pso.handleMakePlayerDiscreteChoice(request);
	
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect(pso.backPage);
		return;
	}
	
	// Get list of allowable responses
	List allowableResponses = AllowableResponse.pullOutArs(cs, pso.schema);
	
	Hashtable answersSelected = pso.selectedChoices(cs, true);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../wysiwyg_files/wysiwyg.js">
</script>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
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
      <h1>Player Make Decision</h1>
    <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 

      <form action="make_player_discrete_choice.jsp" method="post" name="form2" id="form2">
      <input type="hidden" name="num_ars" value="<%= allowableResponses.size() %>" />
        <blockquote>
          <p>Tab Heading: 
            <input type="text" name="tab_heading" value="<%= pso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            <br />
          </p>
          <p><strong>Enter the choice that the player will need to make on this page.            </strong></p>
          <p>
            <textarea id="make_player_discrete_choice_text" name="make_player_discrete_choice_text" style="height: 710px; width: 710px;"><%= cs.getBigString() %></textarea>
            
            <script language="javascript1.2">
  			generate_wysiwyg('make_write_document_page_text');
		</script>
          </p>
          <p>Enter in the answers that the player may select:</p>
          <table width="100%" border="0">
            <tr>
              <td valign="top">&nbsp;</td>
              <td valign="top">Choice:</td>
              <td valign="top">Initially Selected?</td>
              <td valign="top">Remove</td>
            </tr>
        <%
		
		for (ListIterator li = allowableResponses.listIterator(); li.hasNext();) {
			AllowableResponse ar = (AllowableResponse) li.next();
			%>
            <tr>
              <td width="16%" valign="top"><%= ar.getIndex() %></td>
              <td width="67%" valign="top"><label>
                <input name="ar_choice_text_<%= ar.getIndex() %>" type="text" id="textfield" size="60" value="<%= ar.getResponseText() %>" />
                <input type="hidden" name="ar_id_<%= ar.getIndex() %>" value="<%= ar.getId() %>" />
              </label></td>
              <td width="12%" valign="top"><label>
              
                <input type="radio" name="ar_selected_<%= ar.getIndex() %>" id="radio" value="true" <%= (String) answersSelected.get(ar.getId()) %>  /><%= ar.getId() %>
              </label></td>
              <td width="5%" valign="top"><a href="delete_object.jsp?object_type=allowable_response&objid=<%= ar.getId() %>&object_info="Answer <%= ar.getIndex() %>"><img src="../simulation_authoring/images/delete.png" alt="Delete Answer" width="26" height="22" border="0" /></a></td>
            </tr>
            <% } // end of loop over allowable responses %>
          </table>
          <p>
            <label></label>
            Put changes made here into the After Action Report? 
            <label>
            <input type="radio" name="add_final_value_text_to_aar" id="add_final_value_text_to_aar_false" value="false" /> No
            </label> 
            / 
            <label>
            <input type="radio" name="add_final_value_text_to_aar" id="add_final_value_text_to_aar" value="true" /> Yes
            </label> 
     
</p>
          <p>If a particular answer is going to lead to some particular text appearing in the after action report, place it below.</p>
          <table width="100%" border="0">
                  <%
		
		for (ListIterator li = allowableResponses.listIterator(); li.hasNext();) {
			AllowableResponse ar = (AllowableResponse) li.next();
			%>
            <tr>
              <td width="6%" valign="top"><%= ar.getIndex() %></td>
              <td width="94%" valign="top"><label>
                <textarea name="ar_aar_text_<%= ar.getId() %>" id="textarea3" cols="45" rows="5"><%= ar.getSpecificWordsForAAR() %></textarea>
              </label></td>
            </tr>
            <% } // end of loop over allowable responses %>
          </table>
          <p>&nbsp;</p>
          <p>&nbsp;</p>
          <p> 
            <input type="hidden" name="custom_page" value="<%= pso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
            <input type="hidden" name="sending_page" value="make_player_discrete_choice" />
            <input type="submit" name="save_page" value="Save" />
            <input type="submit" name="save_and_add" value="Save and Add Section" />
          </p>
          <p>&nbsp;</p>
        </blockquote>
      </form>
      <form action="make_player_discrete_choice.jsp" method="post" name="add_choice_form" id="add_choice_form">
      <label>
                <input type="submit" name="add_choice" id="add_choice" value="Add New Choice" />
      </label>
      <input type="hidden" name="author_adds_choice" value="true" />
      </form>
                
	  <a href="<%= pso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a><!-- InstanceEndEditable -->
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
