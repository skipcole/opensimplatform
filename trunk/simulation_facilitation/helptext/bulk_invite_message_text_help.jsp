<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,
	java.util.*,
	java.text.*,
	org.usip.osp.networking.*,org.usip.osp.persistence.*,
	org.hibernate.*,
	org.usip.osp.baseobjects.*" 
	errorPage=""

%>
<%
	AuthorFacilitatorSessionObject afso = AuthorFacilitatorSessionObject.getAFSO(request.getSession(true));
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><!-- InstanceBegin template="/Templates/helpPageTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>Help Section Page</title>
<!-- InstanceEndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../usip_osp.css" rel="stylesheet" type="text/css">
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<style type="text/css">
<!--
body {
	background-image: url(../../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style></head>

<body bgcolor="#DDDDFF">
<!-- InstanceBeginEditable name="PageBody" -->
<h1>Bulk Invite Message </h1>
<ul>
  <li>You may edit the default message text.</li>
  <li>Please be careful not to change the exact phrase '[website]' in the message. This is where the correct link for your system will be inserted into the email.</li>
</ul>
<blockquote>
  <p>
    If you would rather just put in an email a link for people to go to get registered, here is the link: <%= afso.getAutoRegistrationBaseLink() %>
  </p>
</blockquote>
<!-- InstanceEndEditable --> 
</body>
<!-- InstanceEnd --></html>
