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
	
	// Get list of allowable responses
	List allowableResponses = AllowableResponse.pullOutArs(cs, pso.schema);
	
	if (pso.forward_on){
		pso.forward_on = false;
		response.sendRedirect(pso.backPage);
		return;
	}
	
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
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform</h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if (pso.isAuthor()) { %>
        <tr>
          <td><div align="center"><a href="intro.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } else { %>
		<tr>
          <td><div align="center"><a href="../simulation_facilitation/instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="logout.jsp" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
      </table>	  
	  </div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_bot.png" width="120" height="20" /></td>
    <td height="20" colspan="2" valign="bottom" bgcolor="#475DB0"><% 
		
		String bgColor_think = "#475DB0";
		String bgColor_create = "#475DB0";
		String bgColor_play = "#475DB0";
		String bgColor_share = "#475DB0";
		
		pso.findPageType(request);
		
		if (pso.page_type == 1){
			bgColor_think = "#9AABE1";
		} else if (pso.page_type == 2){
			bgColor_create = "#9AABE1";
		} else if (pso.page_type == 3){
			bgColor_play = "#9AABE1";
		} else if (pso.page_type == 4){
			bgColor_share = "#9AABE1";
		}
		
		if (pso.isAuthor()) { %>
		
		<table border="0" cellpadding="0" cellspacing="0" >
		   <tr>
		<td bgcolor="<%= bgColor_think %>"><a href="../simulation_planning/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;THINK&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
	    <td bgcolor="<%= bgColor_create %>"><a href="creationwebui.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;CREATE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
		<td bgcolor="<%= bgColor_play %>"><a href="../simulation_facilitation/facilitateweb.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;PLAY&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		<td>&nbsp;</td>
        <td bgcolor="<%= bgColor_share %>"><a href="../simulation_sharing/index.jsp" target="_top" class="menu_item">&nbsp;&nbsp;&nbsp;&nbsp;SHARE&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
		   </tr>
		</table>
	<% } %></td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<BR />
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
        <blockquote>
          <p>Tab Heading: 
            <input type="text" name="tab_heading" value="<%= pso.getMyPSO_SectionMgmt().get_tab_heading() %>"/>
            <br />
          </p>
          <p><strong>Enter the introductory text that will appear on this page.            </strong></p>
          <p>
            <textarea id="make_player_discrete_choice_text" name="make_write_document_page_text" style="height: 710px; width: 710px;"><%= cs.getBigString() %></textarea>
            
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
            </tr>
        <%
		
		for (ListIterator li = allowableResponses.listIterator(); li.hasNext();) {
			AllowableResponse ar = (AllowableResponse) li.next();
			%>
            <tr>
              <td width="4%" valign="top"><%= ar.getIndex() %></td>
              <td width="77%" valign="top"><label>
                <input name="ar_1" type="text" id="textfield" size="60" value="<%= ar.getResponseText() %>" />
                <input type="hidden" name="ar_id_1" value="" />
              </label></td>
              <td width="19%" valign="top"><label>
                <input type="radio" name="ar_selected_1" id="radio" value="radio" />
              </label></td>
              </tr>
            <tr>
            <% } // End of loop over allowable responses %>
            
            <tr>
              <td width="4%" valign="top">1</td>
              <td width="77%" valign="top"><label>
                <input name="ar_1" type="text" id="textfield" size="60" />
                <input type="hidden" name="ar_id_1" value="" />
              </label></td>
              <td width="19%" valign="top"><label>
                <input type="radio" name="ar_selected_1" id="radio" value="radio" />
              </label></td>
              </tr>
            <tr>
              <td valign="top">2</td>
              <td valign="top"><label>
                <input name="ar_2" type="text" id="textfield2" size="60" />
                <input type="hidden" name="ar_id_1" value="" />
              </label></td>
              <td valign="top"><label>
                <input type="radio" name="ar_selected_2" id="radio2" value="radio2" />
                
              </label></td>
              </tr>
          </table>
          <p>
            <label></label>
            Put chances made here into the After Action Report? 
            <label>
            <input type="radio" name="radio3" id="radio3" value="radio3" />
            </label> 
            No / 
            <label>
            <input type="radio" name="radio4" id="radio4" value="radio4" />
            </label> 
            Yes
</p>
          <p>If a particular answer is going to lead to some particular text appearing in the </p>
          <table width="100%" border="0">
            <tr>
              <td width="6%" valign="top">1</td>
              <td width="94%" valign="top"><label>
                <textarea name="textarea3" id="textarea3" cols="45" rows="5"></textarea>
              </label></td>
            </tr>
            <tr>
              <td valign="top">2</td>
              <td valign="top"><label>
                <textarea name="textarea3" id="textarea4" cols="45" rows="5"></textarea>
              </label></td>
            </tr>
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
	  <a href="<%= pso.backPage %>"><img src="../Templates/images/back.gif" alt="Back" border="0"/></a><!-- InstanceEndEditable -->			</td>
		</tr>
		</table>
	</td>
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
<!-- InstanceEnd --></html>
<%
	
%>
