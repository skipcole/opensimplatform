<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,com.oreilly.servlet.*, com.oreilly.servlet.multipart.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.specialfeatures.*" 
	errorPage="/error.jsp" %>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
	
	CustomizeableSection cs = afso.handleMakeCustomizedSection(request, CS_TYPES.IMAGE_PAGE);
	
	if (afso.forward_on){
		afso.forward_on = false;
		response.sendRedirect(afso.backPage);
		return;
	}
	
	
	String imageName = (String) cs.getContents().get("image_file_name");
	
	boolean hasImage = false;
	
	if ((imageName != null) && (imageName.trim().length() > 0)) {
		hasImage = true;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



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
              <h1>Create Image Page</h1>
              <br />
      <form enctype="multipart/form-data" action="make_image_page.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="add_image_page" />
        <table width="100%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Page Tab Heading:</td>
              <td valign="top"> <input type="text" name="tab_heading" value="<%= afso.getMyPSO_SectionMgmt().get_tab_heading() %>"/></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Text Introduction (if any)</td>
              <td valign="top"> <textarea name="page_text" cols="40" rows="2"><%= cs.getBigString() %></textarea></td>
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
                <input name="uploadedfile" type="file" value="<%= cs.simImage(afso.getBaseSimURL()) %>"  /></td>
            </tr>
          <tr> 
            <td colspan="3" valign="top"></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>
                <input type="hidden" name="sending_page" value="add_image_page" />
                <input type="hidden" name="custom_page" value="<%=  afso.getMyPSO_SectionMgmt().get_custom_section_id() %>" />
                <input type="submit" name="save_page" value="Save" />
                <input type="submit" name="save_and_add" value="Save and Add Section" />                </td>
            </tr>
          </table>
      </form>
      <% if (hasImage) { %>
      <img src="<%= cs.simImage(afso.getBaseSimURL()) %>" />
      <% } %>      <p><a href="<%= afso.backPage %>"><img src="../../../Templates/images/back.gif" alt="Back" border="0"/></a></p>			</td>
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
