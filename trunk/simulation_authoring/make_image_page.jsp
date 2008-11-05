<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,com.oreilly.servlet.*, com.oreilly.servlet.multipart.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="" %>
<%
	ParticipantSessionObject pso = ParticipantSessionObject.getPSO(request.getSession(true), true);

	String tab_heading = (String) session.getAttribute("tab_heading");
    String tab_pos = (String) session.getAttribute("tab_pos");
	String universal = (String) session.getAttribute("universal");
	
	String new_tab_heading = request.getParameter("tab_heading");
	if ((new_tab_heading != null) && (new_tab_heading.length() > 0)) {
		tab_heading = new_tab_heading;
	}
	
	String custom_page = request.getParameter("custom_page");
	
	String page_title = "";
	String image_file_name = "";
	
	CustomizeableSection cs = null;
	
	if (custom_page != null) {
		MultiSchemaHibernateUtil.beginTransaction(pso.schema);
		cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(custom_page));
		page_title = (String) cs.getContents().get("page_title");
		image_file_name = (String) cs.getContents().get("image_file_name");
		MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
	}
	
	try {
		MultipartRequest mpr = new MultipartRequest(request, USIP_OSCW_Properties.getValue("uploads"));
		
		if (mpr != null) {
					
			String sending_page = (String) mpr.getParameter("sending_page");
			String submit_new_image_page = (String) mpr.getParameter("submit_new_image_page");
	
			String upload_and_add = (String) mpr.getParameter("upload_and_add");
			
			custom_page = mpr.getParameter("custom_page");
			
				if (custom_page != null) {
					MultiSchemaHibernateUtil.beginTransaction(pso.schema);
					cs = (CustomizeableSection) MultiSchemaHibernateUtil.getSession(pso.schema).get(CustomizeableSection.class, new Long(custom_page));
					System.out.println("from db it was " + cs.getContents().get("page_title"));
					MultiSchemaHibernateUtil.commitAndCloseTransaction(pso.schema);
				
				}
				
				if ( (sending_page != null) && (upload_and_add != null) && (sending_page.equalsIgnoreCase("add_image_page"))){
					// If this is the original custom page, make a new page
					if (!(cs.isThisIsACustomizedSection())){
						System.out.println("making copy");
						cs = cs.makeCopy(pso.schema);
						custom_page = cs.getId() + "";
					}
					
					///////////////////////
					new_tab_heading = mpr.getParameter("tab_heading");
					if ((new_tab_heading != null) && (new_tab_heading.length() > 0)) {
							tab_heading = new_tab_heading;
					}
					///////////////////////
					
					page_title = (String) mpr.getParameter("page_title");
					
					cs.setRec_tab_heading(tab_heading);
					cs.getContents().put("page_title", page_title);
					
					String page_description = (String) mpr.getParameter("page_description");
					cs.setDescription(page_description);
					
					/////////////////////////////////////////
					// Do file upload piece
					pso.makeUploadDir();
					String initFileName = mpr.getOriginalFileName("uploadedfile");
					
					System.out.println("init file was: " + initFileName);
					
					if ((initFileName != null) && (initFileName.trim().length() > 0)) {
						cs.getContents().put("image_file_name", initFileName);
						
						for (Enumeration e = mpr.getFileNames(); e
                            .hasMoreElements();) {
                        	String fn = (String) e.nextElement();

                        	FileIO.saveImageFile("simImage", initFileName, mpr.getFile(fn));
                    	}
					}
					// End of file upload piece
					///////////////////////////////////
					
					cs.save(pso.schema);
					
					// add section
					pso.addSectionFromProcessCustomPage(cs.getId(), tab_pos, tab_heading, request, universal);
					// send them back
					response.sendRedirect(pso.backPage);
					
				} // End of if user took action
				
		} // End of if mpr != null
	} catch (Exception mpr_e){
		System.out.println("error : " + mpr_e.getMessage());
	}
	
	/*
		//Update page values 
		String text_page_text = (String) request.getParameter("text_page_text");
		cs.setBigString(text_page_text);

	*/


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/controlPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Online Simulation Platform Control Page</title>
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
<% String canEdit = (String) session.getAttribute("author"); %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../Templates/images/logo_top.png" width="120" height="100" border="0" /></td>
    <td width="80%" valign="middle"  background="../Templates/images/top_fade.png"><h1 class="header">&nbsp;Open Simulation Platform </h1></td>
    <td align="right" background="../Templates/images/top_fade.png" width="20%"> 

	  <div align="center">
	    <table border="0" cellspacing="1" cellpadding="0">
	<%  if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
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
		
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		
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
  	<td width="120" align="right" valign="top">&nbsp;</td>
    <td colspan="1" valign="top"><br /></td>
    <td width="194" align="right" valign="top">		</td>
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
      <h1>Create Image Page</h1>
      <!-- InstanceEndEditable --><br />
			<!-- InstanceBeginEditable name="pageBody" --> 
      <form enctype="multipart/form-data" action="make_image_page.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="add_image_page" />
        <table width="100%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Page Tab Heading:</td>
            <td valign="top"> <input type="text" name="tab_heading" value="<%= tab_heading %>"/></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Title Line of Page</td>
            <td valign="top"> <input type="text" name="page_title" value="<%= page_title %>" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Description Text of Page</td>
            <td valign="top"> <textarea name="page_description" cols="40" rows="2"><%= cs.getDescription() %></textarea></td>
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
            <td>&nbsp;</td>
            <td>
			<input type="hidden" name="sending_page" value="add_image_page" />
			<input type="hidden" name="custom_page" value="<%= custom_page %>" />
			<input type="submit" name="upload_and_add" value="Upload and Add" /></td>
          </tr>
        </table>
        <p>&nbsp;</p>
      </form>
      <p><a href="<%= pso.backPage %>">&lt;-- Back</a></p>
      <!-- InstanceEndEditable -->
			</td>
		</tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td>
    <p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p></td>
  </tr>
</table>
</td></tr></table>

<p>&nbsp;</p>

<p align="center">&nbsp;</p>
</body>
<!-- InstanceEnd --></html>
