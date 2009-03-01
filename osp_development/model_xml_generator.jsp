<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,
	org.usip.osp.persistence.*,
	org.usip.osp.baseobjects.*,
	org.usip.osp.modelinterface.*,
	org.usip.osp.persistence.*" 
	errorPage="" %>

<%
		ModelDefinitionObject mdo = ModelDefinitionObject.generateMDOforXML(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>Open Simulation Platform Control Page</title>


<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0"><tr><td>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
<tr> 
    <td>
		<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="120"><img src="../Templates/images/white_block_120.png" /></td>
			<td width="100%"><br />
              <h1>Model Definition Object XML Generator</h1>
              <p>Enter information in the fields below and then hit submit. The XML for the Model Definition Object you want to create will then be shown.</p>
              <br />
      <form action="model_xml_generator.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="custom_section_xml_generator" />
        <blockquote>
        
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">BigString</td>
              <td valign="top">
                <input type="text" name="model_name" value="<%= mdo.getModelName() %>" tabindex="1" /></td>
            </tr>
          <tr>
            <td>&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td valign="top">&nbsp;</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">&nbsp;</td>
              <td valign="top">

                <input type="submit" name="command" value="Update XML" />               </td>
            </tr>
          <tr>
            <td>&nbsp;</td>
            <td valign="top">&nbsp;</td>
            <td valign="top">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="3" bgcolor="#FFFF99">
			<% String x = ObjectPackager.getObjectXML(mdo);
				x = x.replaceAll("<", "&lt;");
				x = x.replaceAll(">", "&gt;");
				x = x.replaceAll("\\r\\n", "<br />");
			%>
			<pre><%= x %></pre></td>
            </tr>
          </table>
          <blockquote>
            <p>&nbsp;</p>
          </blockquote>
      </form>
      <blockquote>
        <p>&nbsp;</p>
          <p>&nbsp;</p>
          </blockquote>      </td>
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
