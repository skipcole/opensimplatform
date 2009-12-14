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
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleMakePlayerDiscreteChoice(request);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	// Get list of allowable responses
	List allowableResponses = AllowableResponse.pullOutArs(cs, afso.schema);
	
	Hashtable answersSelected = afso.selectedChoices(cs, true);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>
<script language="JavaScript" type="text/javascript" src="../../../wysiwyg_files/wysiwyg.js">
</script>


<link href="../../../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../../../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Set Parameter</h1>
              <br />
			 
      <form action="../../make_player_discrete_choice.jsp" method="post" name="form2" id="form2">
        <input type="hidden" name="num_ars" value="<%= allowableResponses.size() %>" />
        <blockquote>
          <p>Tab Heading: 
            <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            <br />
            </p>
            <p><strong>Enter the choice that the player will need to make on this page.            </strong></p>
            <p>
              <textarea id="make_player_discrete_choice_text" name="make_player_discrete_choice_text" style="height: 710px; width: 710px;"><%= cs.getBigString() %></textarea>
              
              <script language="javascript1.2">
  			generate_wysiwyg('make_write_document_page_text');
		</script>
              </p>
            <p>&nbsp;</p>
            <p>
              <label></label>
              Put changes made here into the After Action Report? 
              <label>
                <input type="radio" name="add_final_value_text_to_aar" id="add_final_value_text_to_aar_false" value="false" /> No                </label> 
              / 
              <label>
                <input type="radio" name="add_final_value_text_to_aar" id="add_final_value_text_to_aar" value="true" /> Yes                </label> 
  </p>
            <p>&nbsp;</p>
            <p> 
              <input type="hidden" name="custom_page" value="<%= afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
              <input type="hidden" name="sending_page" value="make_player_discrete_choice" />
              <input type="submit" name="save_page" value="Save" />
              <input type="submit" name="save_and_add" value="Save and Add Section" />
              </p>
            <p>&nbsp;</p>
          </blockquote>
      </form>
      <form action="../../make_player_discrete_choice.jsp" method="post" name="add_choice_form" id="add_choice_form">
        <label>
          <input type="submit" name="add_choice" id="add_choice" value="Add New Choice" />
          </label>
        <input type="hidden" name="author_adds_choice" value="true" />
      </form>
      <a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a>			</td>
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
