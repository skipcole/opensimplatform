<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.oscw.networking.*,com.oreilly.servlet.*, com.oreilly.servlet.multipart.*,org.usip.oscw.persistence.*,org.usip.oscw.baseobjects.*,org.usip.oscw.specialfeatures.*" 
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
<!-- InstanceEndEditable --><!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<link href="../usip_oscw.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<!-- InstanceParam name="onloadAttribute" type="text" value="" -->
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="80%" valign="top"><!-- InstanceBeginEditable name="pageTitle" -->
      <h1>Create Image Page</h1>
      <!-- InstanceEndEditable --></td>
    <td width="20%" align="right" valign="top"> 
		<% 
		String canEdit = (String) session.getAttribute("author");
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
		<a href="../simulation_planning/index.jsp" target="_top">Think</a><br>
	    <a href="creationwebui.jsp" target="_top">Create</a><br>
		<a href="../simulation_facilitation/facilitateweb.jsp" target="_top">Play</a><br>
        <a href="../simulation_sharing/index.jsp" target="_top">Share</a>
		<% } %>
		</td>
  </tr>
</table>
<BR />
<table width="720" bgcolor="#DDDDFF" align="center" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24" >&nbsp;</td>
</tr>
<tr> 
    <td colspan="3"><!-- InstanceBeginEditable name="pageBody" --> 
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
            <td valign="top">Page Title:</td>
            <td valign="top"> <input type="text" name="page_title" value="<%= page_title %>" /></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td valign="top">Short Description Text</td>
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
      <!-- InstanceEndEditable --></td>
  </tr>
<tr> 
    <td width="24" height="24"  >&nbsp;</td>
    <td width="672">&nbsp;</td>
    <td width="24" height="24"  >&nbsp;</td>
  </tr>
</table>

<p>&nbsp;</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td align="left" valign="bottom"> 
	<% 
		if ((canEdit != null) && (canEdit.equalsIgnoreCase("true"))) { %>
	<a href="intro.jsp" target="_top">Home 
      </a>
	  <% } else { %>
	  <a href="../simulation_facilitation/index.jsp" target="_top">Home 
      </a>
	  <% } %>
	  </td>
    <td align="right" valign="bottom"><a href="../simulation_user_admin/my_profile.jsp">My 
      Profile</a> </td>
  </tr>
  <tr>
    <td align="left" valign="bottom"><a href="logout.jsp" target="_top">Logout</a></td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
<p align="center">The <a href="http://www.usip.org">USIP</a> Online Simulation Platform is a <a href="http://code.google.com/p/usiponlinesimulationcreationwizard/">USIP Open Source Software Project</a>. </p>
</body>
<!-- InstanceEnd --></html>
