<%@ page 
	contentType="text/html; charset=UTF-8" 
	language="java" 
	import="java.sql.*,java.util.*" 
	errorPage="/error.jsp" %>
<%

	String show_print_button = (String) request.getParameter("show_print_button");
	
	boolean addPrintButton = false;
	
	if ((show_print_button != null) && (show_print_button.equalsIgnoreCase("true") ) ){
		addPrintButton = true;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/tutorialTemplate.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>Open Simulation Platform Tutorial Page</title>
<!-- InstanceEndEditable -->
<link href="../../usip_osp.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
	background-image: url(../../Templates/images/page_bg.png);
	background-repeat: repeat-x;
}
-->
</style>
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<STYLE TYPE="text/css">
     P.breakhere {page-break-after: always}
</STYLE> 
</head>
<body onLoad="">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="120" valign="top"><img src="../../Templates/images/logo_top.png" width="60" height="50" border="0" /></td>
    <td width="80%" valign="middle"  background="../../Templates/images/top_fade.png"><h2 class="header"><font color="#FFFFFF">&nbsp;USIP OSP Tutorial - <!-- InstanceBeginEditable name="tutorialName" -->Simulation Authoring 1.0<!-- InstanceEndEditable --></font></h2></td>
    <td align="right" background="../../Templates/images/top_fade.png" width="20%"> 

	  <div align="center"><!-- InstanceBeginEditable name="linkToHelp" --><a href="../help.jsp">Help!</a><!-- InstanceEndEditable --></div>	  </td>
  </tr>
  <tr>
    <td width="120" valign="top"><img src="../../Templates/images/logo_bot.png" width="60" height="10" /></td>
    <td height="10" colspan="2" valign="bottom" bgcolor="#475DB0">
	</td>
  </tr>
  <tr>
  	<td width="120" align="right" valign="top"></td>
    <td colspan="1" valign="top"></td>
    <td width="194" align="right" valign="top"></td>
  </tr>
</table>
<!-- InstanceBeginEditable name="pageBody" -->
<br />
<table width="100%"><tr><td>
<h1>Welcome</h1></td><td>
  <div align="center">
    <input name="button" type="button"
  onclick="window.print()"
  value="Print This Page"/>
  </div></td></tr></table>
<blockquote>
  <p>In this tutorial we  are going to set up a very simple simulation where two people can enter into to negotiate over oranges. Along the way we will discuss many core concepts of the OSP. Time to complete this tutorial is  approximately an hour.</p>
  <p>To complete this tutorial you will need access to an authoring account on an OSP server. If you need assistance getting this, please send an email to osp@usip.org.</p>
  <table align="center" width="70%" border="0" cellspacing="4" cellpadding="4">

  <tr>

    <td bgcolor="#FFFFFF"><h2>Before You Begin</h2>

      <p>It is probably worth noting here that this tool Assumes that the simulation author comes in with something in mind to work on - just as the blank page assumes that the writer has something to write about. To perform these tutorials, we have created some rather simple content, but the eventual content that you create using this platform will be (should be) much better. </p>

    <p>We are just creating the platform to allow you to become a virtuouso. If this platform needs improvement to allow your creative vision to come to fruition, send an email to osp@usip.org</p>

    <p>&nbsp; </p></td>

  </tr>

</table>

  <p>&nbsp;</p>
  <p align="center"><a href="page1.jsp">Begin</a></p>
  <p>&nbsp;</p>
  <p>If you are returning to this tutorial, you many jump ahead to any section below.</p>
  <h1><a name="toc" id="toc"></a>Table of Contents</h1>
  <ol>
    <li><a href="page1.jsp">Log on and go to the 'Create' section</a></li>
    <li><a href="page2.jsp">Create your simulation</a></li>
    <li><a href="page3.jsp">Enter your objectives</a></li>
    <li><a href="page4.jsp">Enter your audience</a></li>
    <li><a href="page5.jsp">Enter your introduction</a></li>
    <li><a href="page6.jsp">Enter your planned play ideas</a></li>
    <li><a href="page7.jsp">Skipping over creating phases</a></li>
    <li><a href="page8.jsp">Create actors</a></li>
    <li><a href="page9.jsp">Assign actors</a></li>
    <li><a href="page10.jsp">Skipping over creating injects</a></li>
    <li><a href="page11.jsp">Skipping over creating documents</a></li>
    <li><a href="page12.jsp">Creating universal sections</a></li>
    <li><a href="page13.jsp">Assigning specific sections</a></li>
    <li><a href="page14.jsp">Write  starter 'After Action Report' text</a></li>
    <li><a href="page15.jsp">Reviewing your simulation</a></li>
  </ol>

  <p>&nbsp;</p>
</blockquote>
<p>&nbsp;</p>
<!-- InstanceEndEditable -->
<table  border="0" align="center" cellspacing="5">
  <tr>
    <td><div align="center"><!-- InstanceBeginEditable name="backButton" --><!-- InstanceEndEditable --></div></td>
    <td><div align="center"><!-- InstanceBeginEditable name="tocLink" --><a href="#">table of contents</a><!-- InstanceEndEditable --></div></td>
    <td><div align="center"><!-- InstanceBeginEditable name="nextButton" --> <a href="page1.jsp">next --&gt;</a><!-- InstanceEndEditable --></div></td>
  </tr>
</table>
<p>&nbsp;</p>
<p align="center">The <a href="http://www.usip.org">USIP</a> Open Simulation Platform is a <a href="http://code.google.com/p/opensimplatform/">USIP Open Source Software Project</a>. </p>
<P CLASS="breakhere"></P>
</body>
<!-- InstanceEnd --></html>