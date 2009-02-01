<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	String sending_page = (String) request.getParameter("sending_page");
	String submit_new_image_page = (String) request.getParameter("submit_new_image_page");
	
	///////////////////////////////////
	
	String debug = "";
	
	List simImagePages = pso.simulation.getImage_pages();

	//////////////////////////////////
	


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
<%
	String myLogoutPage = pso.getBaseSimURL() + "/simulation/logout.jsp";
	
	if ( (pso.isAuthor())  || (pso.isFacilitator())) {
		myLogoutPage = pso.getBaseSimURL() + "/simulation_authoring/logout.jsp";
	}
%>
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
	<% } else if (pso.isFacilitator()) { %>
		<tr>
          <td><div align="center"><a href="../simulation_facilitation/instructor_home.jsp" target="_top" class="menu_item"><img src="../Templates/images/home.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
	<% } %>	
        <tr>
          <td><div align="center"><a href="../simulation_user_admin/my_profile.jsp" class="menu_item"><img src="../Templates/images/my_profile.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
        </tr>
        <tr>
          <td><div align="center"><a href="<%= myLogoutPage %>" target="_top" class="menu_item"><img src="../Templates/images/logout.png" alt="Home" width="90" height="19" border="0" /></a></div></td>
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
      <h1>Create Placeholder / Image Page</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
<p><%= Debug.getDebug(debug) %></p>
      <blockquote>
        <p>Current images pages for the Simulation <%= pso.simulation.getDisplayName() %>:</p>
        <blockquote>
          <p>
            <% if (simImagePages.size() == 0) { %>
          </p>
        </blockquote>
        <ul>
          <li>None
            <p>
              <% } %>
              <% for (ListIterator li = simImagePages.listIterator(); li.hasNext();) {
					IntVariable iv = (IntVariable) li.next();
	%>
            </p>
          </li>
          <li><a href="sf_image_page.jsp?edit_sv=true&amp;sf_id=<%= iv.getid() %>"><%= iv.getName() %></a>
		  <a href="delete_object.jsp?object_type=sf_var_int&amp;objid=<%= iv.getid() %>&amp;backpage=sf_image_page.jsp&amp;object_info=&quot;<%= iv.getname() %>&quot;"> 
              (Remove) <%= iv.getName() %> </a>
            <p>
              <% } %>
            </p>
          </li>
        </ul>
        <p>Add an image page</p>
      </blockquote>
	        <form enctype="multipart/form-data" action="jsp_image_page_uploader.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="add_image_page" />
        <table width="100%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Internal Name:</td>
            <td valign="top"> <input type="text" name="section_short_name" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Page Tab Heading:</td>
            <td valign="top"> <input type="text" name="page_tab_heading" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Page Title:</td>
            <td valign="top"> <input type="text" name="page_title" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Description Text</td>
            <td valign="top"> <textarea name="page_description" cols="40" rows="2"></textarea></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Image File:</td>
            <td valign="top"> <input type="hidden" name="MAX_FILE_SIZE" value="400000" /> 
              <input name="uploadedfile" type="file" /></td>
          </tr>
          <tr> 
            <td colspan="3" valign="top"></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">&nbsp;</td>
            <td valign="top">&nbsp;</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td><input type="submit" name="submit_new_image_page" value="Submit" /></td>
          </tr>
        </table>
        <p>&nbsp;</p>
      </form>
      <p align="center"><a href="incorporate_underlying_model.jsp">Back to Add Special 
        Features</a></p>
      <p>&nbsp;</p>
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
