<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="java.sql.*,java.util.*,org.usip.osp.networking.*,org.usip.osp.persistence.*,org.usip.osp.baseobjects.*,org.usip.osp.persistence.*" 
	errorPage="" %>

<%
		CustomizeableSection cs = CustomizeableSection.generateCSforXML(request);
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
              <h1>Custom Section XML Generator</h1>
              <p>Enter information in the fields below and then hit submit. The XML for the Custom Section you want to create will then be shown.</p>
              <br />
      <form action="custom_section_xml_generator.jsp" method="post" name="form1" id="form1">
        <input type="hidden" name="sending_page" value="custom_section_xml_generator" />
        <blockquote>
        
        <table width="80%" border="0" cellspacing="2" cellpadding="2">
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">BigString (?)</td>
              <td valign="top">
                <input type="text" name="bigString" value="<%= cs.getBigString() %>" tabindex="1" /></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Confers Read Ability (?)</td>
              <td valign="top"><label>
                <select name="confers_read_ability" id="select">
                  <option value="false" selected="selected">false</option>
                  <option value="true">true</option>
                </select>
              </label></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Confers Write Ability (?)</td>
              <td valign="top"><select name="confers_write_ability" id="confers_read_ability">
                <option value="false" selected="selected">false</option>
                <option value="true">true</option>
              </select></td>
            </tr>
          <tr> 
            <td>&nbsp;</td>
              <td valign="top">Control Section (?)</td>
              <td valign="top"><select name="control_section" id="confers_write_ability">
                <option value="false" selected="selected">false</option>
                <option value="true">true</option>
              </select></td>
            </tr>
          <tr>
            <td>&nbsp;</td>
              <td valign="top">Creating Organization (?)</td>
              <td valign="top"><input type="text" name="creatingOrganization" value="<%= cs.getCreatingOrganization() %>" tabindex="1" /></td>
            </tr>
          <tr>
            <td>&nbsp;</td>
              <td valign="top">Directory (?)</td>
              <td valign="top"><input type="text" name="directory" value="<%= cs.getDirectory() %>" tabindex="1" /></td>
          </tr>
                    <tr>
            <td>&nbsp;</td>
              <td valign="top">hasASpecificMakePage (?)</td>
              <td valign="top"><select name="hasASpecificMakePage" id="hasASpecificMakePage">
                <option value="false" selected="selected">false</option>
                <option value="true">true</option>
              </select></td>
          </tr>
                    <tr>
            <td>&nbsp;</td>
              <td valign="top">hasCustomizer (?)</td>
              <td valign="top"><select name="hasCustomizer" id="hasCustomizer">
                <option value="false" selected="selected">false</option>
                <option value="true">true</option>
              </select></td>
          </tr>
                    <tr>
            <td>&nbsp;</td>
              <td valign="top">page_file_name (?)</td>
              <td valign="top"><input type="text" name="page_file_name" value="<%= cs.getPage_file_name() %>" tabindex="1" /></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
              <td valign="top">pageTitle (?)</td>
              <td valign="top"><input type="text" name="pageTitle" value="<%= cs.getPageTitle() %>" tabindex="1" /></td>
          </tr>          <tr>
            <td>&nbsp;</td>
              <td valign="top">rec_tab_heading (?)</td>
              <td valign="top"><input type="text" name="rec_tab_heading" value="<%= cs.getRec_tab_heading() %>" tabindex="1" /></td>
          </tr>
                      <td>&nbsp;</td>
              <td valign="top">sample_image (?)</td>
              <td valign="top"><input type="text" name="sample_image" value="<%= cs.getSample_image() %>" tabindex="1" /></td>
          </tr>
                      <td>&nbsp;</td>
              <td valign="top">specificMakePage (?)</td>
              <td valign="top"><input type="text" name="specificMakePage" value="<%= cs.getSpecificMakePage() %>" tabindex="1" /></td>
          </tr>
                    <tr>
            <td>&nbsp;</td>
              <td valign="top">uniqueName (?)</td>
              <td valign="top"><input type="text" name="uniqueName" value="<%= cs.getUniqueName() %>" tabindex="1" /></td>
          </tr>
                    <tr>
            <td>&nbsp;</td>
              <td valign="top">url (?)</td>
              <td valign="top"><input type="text" name="url" value="<%= cs.getUrl() %>" tabindex="1" /></td>
          </tr>
                    <tr>
            <td>&nbsp;</td>
              <td valign="top">version (?)</td>
              <td valign="top"><input type="text" name="version" value="<%= cs.getVersion() %>" tabindex="1" /></td>
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
			<% String x = ObjectPackager.getObjectXML(cs);
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
