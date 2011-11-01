<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*,
	org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	java.io.*,
	com.oreilly.servlet.MultipartRequest" 
	errorPage="/error.jsp" %>
<% 
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));

	if (!(afso.isLoggedin())) {
		response.sendRedirect("index.jsp");			
		return;
	}
	
	boolean saveSuccess = false;
	
	try {
		MultipartRequest mpr = new MultipartRequest(request, USIP_OSP_Properties.getValue("uploads"));
	
	// Determine if uploading simulation
	String sending_page = (String) mpr.getParameter("sending_page");
	
	if ( (sending_page != null) && (sending_page.equalsIgnoreCase("upload_simulation"))){
		
				String initFileName = mpr.getOriginalFileName("uploadedfile");

				if ((initFileName != null) && (initFileName.trim().length() > 0)) {

					String savedFileName = mpr.getOriginalFileName("uploadedfile");

					File fileData = mpr.getFile("uploadedfile");

					saveSuccess = FileIO.saveSimulationXMLFile(mpr.getFile("uploadedfile"), savedFileName);

				}

	} // End of if changing sim to set text for.
	
	} catch (Exception e) {

			System.out.println(e.getMessage());
		}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Open Simulation Platform Control Page</title>



<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../third_party_libraries/jquery/jquery-1.6.3.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../help-bubble.js">
</script>
</head>
<body onLoad="">
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Upload Simulation</h1>
              <p>Select the browse button and browse until you find the file you want to upload.</p>
              <br />
    <form  enctype="multipart/form-data"  action="upload_simulation.jsp" method="post" name="form1" id="form1">
    <input type="hidden" name="sending_page" value="upload_simulation" />
      <p><input name="uploadedfile" type="file"  /></p>
      <p>
        <input type="submit" name="button" id="button" value="Upload" />
      </p>
    </form>
    <p>&nbsp;</p>
    <% if (saveSuccess) { %>
    	File Saved
    <% } %>
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
