<%@ page 
	contentType="text/html; charset=UTF-8" 
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Open Simulation Platform Control Page</title>
<link href="../usip_osp.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" bgcolor="#FFFFFF" align="left" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
              <tr>
                <td width="120"><img src="../Templates/images/white_block_120.png" /></td>
                <td width="100%"><br />
                  <h1>Model Definition Object XML Generator</h1>
                  <p>On this page one can define a model and generated the required XML file to allow it to be transported to other OSP installations.</p>
                  <p>Enter information in the fields below and then hit submit. The XML for the Model Definition Object you want to create will then be shown.</p>
                  <br />
                  <form action="model_xml_generator.jsp" method="post" name="form1" id="form1">
                    <input type="hidden" name="sending_page" value="custom_section_xml_generator" />
                    <blockquote>
                    <table width="80%" border="0" cellspacing="2" cellpadding="2">
                      <tr>
                        <td width="63%" valign="top">Model Name:</td>
                        <td width="26%" valign="top"><input type="text" name="model_name" value="<%= mdo.getModelName() %>" tabindex="1" /></td>
                      </tr>
                      <tr>
                        <td valign="top">Model Version:</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td valign="top">Run Location:</td>
                        <td valign="top">
                          <select name="select" id="select">
                            <option value="local" selected="selected">local</option>
                            <option value="remote">remote</option>
                          </select></td>
                      </tr>
                      <tr>
                        <td valign="top">If Runs Local, must fill out fields in red below</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td bgcolor="#FFCCCC">Controller Package Name</td>
                        <td bgcolor="#FFCCCC"><label>
                          <input type="text" name="textfield2" id="textfield2" />
                        </label></td>
                      </tr>
                      <tr>
                        <td bgcolor="#FFCCCC">Controller Class Name</td>
                        <td bgcolor="#FFCCCC"><label>
                          <input type="text" name="textfield3" id="textfield3" />
                        </label></td>
                      </tr>
                      <tr>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td valign="top">If Runs Remotely, must fill out fields in green below</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td valign="top" bgcolor="#CCFFCC">Communication is</td>
                        <td valign="top" bgcolor="#CCFFCC"><label>
                          <input type="radio" name="radio3" id="radio3" value="radio3" />
                          </label>
                          XML over HTTP</td>
                      </tr>
                      <tr>
                        <td valign="top" bgcolor="#CCFFCC">Model URL is</td>
                        <td valign="top" bgcolor="#CCFFCC"><label>
                          <input type="text" name="textfield" id="textfield" />
                        </label></td>
                      </tr>
                      <tr>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td valign="top">Model Parameters</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td valign="top">&nbsp;</td>
                        <td valign="top"><input type="submit" name="button" id="button" value="Add Parameter" /></td>
                      </tr>
                      <tr>
                        <td valign="top">Parameter Dependency</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td valign="top"><label></label></td>
                        <td valign="top"><input type="submit" name="button2" id="button2" value="Add Parameter Dependency" /></td>
                      </tr>
                      <tr>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td valign="top">&nbsp;</td>
                        <td valign="top"><input type="submit" name="command" value="Update XML" />                        </td>
                      </tr>
                      <tr>
                        <td valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="2" bgcolor="#FFFF99"><% String x = ObjectPackager.getObjectXML(mdo);
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
                  </blockquote></td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td><p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>.</p></td>
        </tr>
      </table></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">&nbsp;</p>
</body>
</html>
<%
	
%>
